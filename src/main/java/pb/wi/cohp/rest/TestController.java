package pb.wi.cohp.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.config.jwt.service.UserDetailsImpl;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestDTO;
import pb.wi.cohp.domain.test.TestService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tests")
@CrossOrigin
@Validated
public class TestController {

    final TestService testService;

    final ParameterService parameterService;

    final ModelMapper modelMapper;

    public TestController(TestService testService, ParameterService parameterService, ModelMapper modelMapper) {
        this.testService = testService;
        this.parameterService = parameterService;
        this.modelMapper = modelMapper;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{testName}")
    public ResponseEntity<?> createTest(
            @PathVariable String testName,
            @Valid @RequestBody List<Parameter> parameterList){
        Test test = testService.createTest(testName);
        parameterService.createParameter(parameterList, test.getId(), false, true);
        return ResponseEntity
                .ok(convertToDto(testService.findTestById(test.getId())));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/users/{testName}")
    public ResponseEntity<?> createTestByUser(
            @PathVariable String testName,
            @Valid @RequestBody List<Parameter> parameterList){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        Test test = testService.createTest(testName, username);
        parameterService.createParameter(parameterList, test.getId(), false , true);
        return ResponseEntity
                .ok(convertToDto(testService.findTestById(test.getId())));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{testId}")
    public void removeTest(@PathVariable Long testId){
        testService.deleteTestById(testId);
    }

    @PreAuthorize("#username == authentication.principal.username")
    @DeleteMapping("/users/{username}/{testId}")
    public void removeTestByUser(@PathVariable Long testId,
                                 @PathVariable String username){
        testService.deleteTestById(testId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<?> editTest(@Valid @RequestBody TestDTO test){
        return ResponseEntity.ok(convertToDto(testService.editTest(test, true)));
    }

    @PreAuthorize("#username == authentication.principal.username")
    @PutMapping("/users/{username}")
    public ResponseEntity<?> editTest(@Valid @RequestBody TestDTO test,
                                      @PathVariable String username){
        return ResponseEntity.ok(convertToDto(testService.editTest(test, false)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getTests(){
        return ResponseEntity.ok(convertToDto(testService.getTests()));
    }

    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping("/users/{username}")
    public ResponseEntity<?> getTests(
            @PathVariable String username
    ){
        return ResponseEntity.ok(convertToDto(testService.getTests(username)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{testId}")
    public ResponseEntity<?> getTest(@PathVariable Long testId){
        return ResponseEntity.ok(convertToDto(testService.findTestById(testId)));
    }

    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping("/users/{username}/{testId}")
    public ResponseEntity<?> getTestByUser(@PathVariable Long testId,
                                           @PathVariable String username){
        return ResponseEntity.ok(convertToDto(testService.findTestByIdAndUser(testId, username)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{testId}")
    public ResponseEntity<?> addParametersToTest(@PathVariable Long testId,
                                                 @Valid @RequestBody List<Parameter> parameterList){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        if(roles.get(0).equals("ROLE_ADMIN")){
            parameterService.createParameter(parameterList, testId,true,false);
        }
        else{
            parameterService.createParameter(parameterList, testId,false,false);
        }
        return ResponseEntity
                .ok(
                        convertToDto(testService
                                .findTestById(testId))
                );
    }

    private TestDTO convertToDto(Test test){
        return modelMapper.map(test, TestDTO.class);
    }

    private List<TestDTO> convertToDto(List<Test> tests){
        List<TestDTO> result = new ArrayList<>();
        for(Test test: tests){
            result.add(modelMapper.map(test, TestDTO.class));
        }
        return result;
    }
}

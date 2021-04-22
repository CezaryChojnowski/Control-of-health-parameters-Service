package pb.wi.cohp.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestDTO;
import pb.wi.cohp.domain.test.TestService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
        parameterService.createParameter(parameterList, test.getId());
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
        parameterService.createParameter(parameterList, test.getId());
        return ResponseEntity
                .ok(convertToDto(testService.findTestById(test.getId())));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{testId}")
    public void removeTest(@PathVariable Long testId){
        testService.deleteTestById(testId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<?> editTest(@Valid @RequestBody TestDTO test){
        return ResponseEntity.ok(convertToDto(testService.editTest(test)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getTests(){
        return ResponseEntity.ok(convertToDto(testService.getTests()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{testId}")
    public ResponseEntity<?> getTest(@PathVariable Long testId){
        return ResponseEntity.ok(convertToDto(testService.findTestById(testId)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{testId}")
    public ResponseEntity<?> addParametersToTest(@PathVariable Long testId,
                                                 @Valid @RequestBody List<Parameter> parameterList){
        parameterService.createParameter(parameterList, testId);
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

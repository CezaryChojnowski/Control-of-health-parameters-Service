package pb.wi.cohp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tests")
@CrossOrigin
public class TestController {

    final TestService testService;

    final ParameterService parameterService;

    public TestController(TestService testService, ParameterService parameterService) {
        this.testService = testService;
        this.parameterService = parameterService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{testName}")
    public ResponseEntity<?> createTest(
            @PathVariable String testName,
            @Valid @RequestBody List<Parameter> parameterList){
        Test test = testService.createTest(testName);
        parameterService.createParameter(parameterList, test.getId());
        return ResponseEntity
                .ok(
                        testService
                                .findTestById(
                                        test
                                                .getId()
                                )
                );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{testId}")
    public void removeTest(@PathVariable Long testId){
        testService.deleteTestById(testId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<?> editTest(@Valid @RequestBody Test test){
        return ResponseEntity.ok(testService.editTest(test));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getTests(){
        return ResponseEntity.ok(testService.getTests());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{testId}")
    public ResponseEntity<?> getTest(@PathVariable Long testId){
        return ResponseEntity.ok(testService.findTestById(testId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{testId}")
    public ResponseEntity<?> addParametersToTest(@PathVariable Long testId,
                                                 @Valid @RequestBody List<Parameter> parameterList){
        parameterService.createParameter(parameterList, testId);
        return ResponseEntity
                .ok(
                        testService
                                .findTestById(testId)
                );
    }
}

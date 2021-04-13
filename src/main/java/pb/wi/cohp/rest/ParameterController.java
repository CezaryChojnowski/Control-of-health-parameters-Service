package pb.wi.cohp.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;

@RestController
@RequestMapping("/parameters")
@CrossOrigin
public class ParameterController {

    final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{parameterId}")
    public void removeParameter(@PathVariable Long parameterId){
        parameterService.deleteParameter(parameterId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<?> editParameter(@RequestBody Parameter parameter){
        return ResponseEntity.ok(parameterService.editParameter(parameter));
    }

}

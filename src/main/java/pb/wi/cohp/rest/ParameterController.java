package pb.wi.cohp.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.parameter.ParameterService;

@RestController
@RequestMapping("/parameters")
@CrossOrigin
@Validated
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

}

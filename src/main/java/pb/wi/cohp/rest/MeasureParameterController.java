package pb.wi.cohp.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.measureParameter.dto.MeasureParameterDTO;
import pb.wi.cohp.domain.measureParameter.MeasureParameterService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/measuresParameter")
public class MeasureParameterController {

    final MeasureParameterService measureParameterService;

    public MeasureParameterController(MeasureParameterService measureParameterService) {
        this.measureParameterService = measureParameterService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> createMeasureParameter(@RequestBody MeasureParameterDTO measureParameterDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedUser_Username = authentication.getName();
        measureParameterService.createMeasureParameter(
                measureParameterDTO.getValues(),
                currentLoggedUser_Username,
                measureParameterDTO.getDate(),
                measureParameterDTO.getNote(),
                measureParameterDTO.getTestId()
        );
        List<String> result = measureParameterService.checkIfMeasuresIsOverRange(measureParameterDTO.getValues(), currentLoggedUser_Username);
        return ResponseEntity.ok(result);
    }

}

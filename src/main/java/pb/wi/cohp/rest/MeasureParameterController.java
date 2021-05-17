package pb.wi.cohp.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.measureParameter.dto.MeasureParameterDTO;
import pb.wi.cohp.domain.measureParameter.MeasureParameterService;

@RestController
@RequestMapping("/measuresParameter")
public class MeasureParameterController {

    final MeasureParameterService measureParameterService;

    public MeasureParameterController(MeasureParameterService measureParameterService) {
        this.measureParameterService = measureParameterService;
    }

    @PreAuthorize("#username == authentication.principal.username")
    @PostMapping("/{username}")
    public ResponseEntity<?> createMeasureParameter(@RequestBody MeasureParameterDTO measureParameterDTO,
                                                    @PathVariable String username){
        measureParameterService.createMeasureParameter(
                measureParameterDTO.getValues(),
                username,
                measureParameterDTO.getDate(),
                measureParameterDTO.getNote(),
                measureParameterDTO.getTestId()
        );
        return ResponseEntity.ok(200);
    }

}

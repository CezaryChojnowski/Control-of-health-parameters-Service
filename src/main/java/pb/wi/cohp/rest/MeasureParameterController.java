package pb.wi.cohp.rest;


import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.measureParameter.MeasureParameter;
import pb.wi.cohp.domain.measureParameter.dto.MeasureParameterDTO;
import pb.wi.cohp.domain.measureParameter.MeasureParameterService;
import pb.wi.cohp.domain.measureParameter.dto.MeasuresToChartDTO;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/measuresParameter")
public class MeasureParameterController {

    final MeasureParameterService measureParameterService;

    final ModelMapper modelMapper;

    public MeasureParameterController(MeasureParameterService measureParameterService, ModelMapper modelMapper) {
        this.measureParameterService = measureParameterService;
        this.modelMapper = modelMapper;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<?> editMeasureParameter(@RequestBody MeasureParameterDTO measureParameterDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedUser_Username = authentication.getName();
        measureParameterService.editMeasureParameter(measureParameterDTO);
        List<String> result = measureParameterService.checkIfMeasuresIsOverRange(measureParameterDTO.getValues(), currentLoggedUser_Username);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getValuesToChart(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedUser_Username = authentication.getName();
         return ResponseEntity.ok(convertToDto(measureParameterService.getDataMeasuresByParameterIdAndOwner(id, currentLoggedUser_Username)));
    }

    private MeasuresToChartDTO convertToDto(MeasureParameter measureParameter){
        return modelMapper.map(measureParameter, MeasuresToChartDTO.class);
    }

    private List<MeasuresToChartDTO> convertToDto(List<MeasureParameter> measureParameters){
        List<MeasuresToChartDTO> result = new ArrayList<>();
        for(MeasureParameter measureParameter: measureParameters){
            result.add(modelMapper.map(measureParameter, MeasuresToChartDTO.class));
        }
        return result;
    }



}

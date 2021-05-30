package pb.wi.cohp.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.domain.range.EditRangeDTO;
import pb.wi.cohp.domain.range.Range;
import pb.wi.cohp.domain.range.RangeDTO;
import pb.wi.cohp.domain.range.RangeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ranges")
public class RangeController {
    final RangeService rangeService;

    final ModelMapper modelMapper;

    public RangeController(RangeService rangeService, ModelMapper modelMapper) {
        this.rangeService = rangeService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getRanges(@RequestParam(required = false) Integer page){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedUser_Username = authentication.getName();
        return ResponseEntity.ok(
                convertToDto(rangeService
                        .getRangesByUsername(
                                currentLoggedUser_Username,
                                page != null && page>=0 ? page : 0
                        )
                )
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRanges(@PathVariable Long id){
        return ResponseEntity.ok(
                convertToDto(rangeService
                        .getById(id)
                )
        );
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<?> editRange(@RequestBody EditRangeDTO editRangeDTO){
        return ResponseEntity.ok(
                convertToDto(
                        rangeService.editRange(editRangeDTO)
                )
        );
    }

    private RangeDTO convertToDto(Range range){
        return modelMapper.map(range, RangeDTO.class);
    }

    private List<RangeDTO> convertToDto(List<Range> ranges){
        List<RangeDTO> result = new ArrayList<>();
        for(Range range: ranges){
            result.add(modelMapper.map(range, RangeDTO.class));
        }
        return result;
    }
}

package pb.wi.cohp.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pb.wi.cohp.Mapper;
import pb.wi.cohp.domain.measure.MeasureService;


@RestController
@RequestMapping("/measures")
public class MeasureController {
    final MeasureService measureService;

    final Mapper mapper;

    public MeasureController(MeasureService measureService, Mapper mapper) {
        this.measureService = measureService;
        this.mapper = mapper;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public void deleteMeasure(@PathVariable Long id){
        measureService.removeMeasure(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<?> getTests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedUser_Username = authentication.getName();
        return ResponseEntity.ok(
                mapper.
                        convertToMeasureDTOList(
                                measureService.
                                        getMeasuresByUser(
                                                currentLoggedUser_Username
                                        )
                        )
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTest(@PathVariable Long id){
        return ResponseEntity.ok(mapper.convertToUserMeasureDetailsDTO (measureService.getMeasureById(id)));
    }


}

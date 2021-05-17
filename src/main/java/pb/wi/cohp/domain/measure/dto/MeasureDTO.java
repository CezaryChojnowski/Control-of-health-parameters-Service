package pb.wi.cohp.domain.measure.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasureDTO {
    Long id;
    LocalDate date;
    String testName;
}

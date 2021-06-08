package pb.wi.cohp.domain.measureParameter.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasuresToChartDTO {
    public Double value;
    public LocalDate measureDate;
}

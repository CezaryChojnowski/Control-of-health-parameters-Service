package pb.wi.cohp.domain.measureParameter.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasureParameterDTO {
    public HashMap<Long, Double> values;
    public LocalDate date;
    public String note;
    public Long testId;
}

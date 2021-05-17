package pb.wi.cohp.domain.measureParameter.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasureValuesDTO {
    public Long id;
    public Double value;
    public String parameterName;
}

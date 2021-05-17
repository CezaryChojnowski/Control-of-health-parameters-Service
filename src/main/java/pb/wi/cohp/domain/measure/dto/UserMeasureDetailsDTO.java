package pb.wi.cohp.domain.measure.dto;

import lombok.*;
import pb.wi.cohp.domain.measureParameter.dto.MeasureValuesDTO;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMeasureDetailsDTO {
    public String testName;
    public LocalDate date;
    public String note;
    public List<MeasureValuesDTO> measureValuesDTOList;
}

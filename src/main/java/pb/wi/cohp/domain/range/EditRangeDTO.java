package pb.wi.cohp.domain.range;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class EditRangeDTO {
    @Setter
    private Long id;

    @Setter
    private Double minValue;

    @Setter
    private Double maxValue;
}

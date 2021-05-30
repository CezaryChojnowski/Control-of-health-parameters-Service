package pb.wi.cohp.domain.test;


import lombok.*;
import pb.wi.cohp.domain.parameter.Parameter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@EqualsAndHashCode
public class TestDTO {
    private Long id;
    private String name;
    List<Parameter> parameters;
    boolean owner;
}

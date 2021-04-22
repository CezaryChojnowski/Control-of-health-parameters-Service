package pb.wi.cohp.domain.parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pb.wi.cohp.domain.measureParameter.MeasureParameter;
//import pb.wi.cohp.domain.range.Range;
import pb.wi.cohp.domain.test.Test;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotEmpty(message = "{parameter.name.notEmpty}")
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Test.class)
    @JoinColumn(name="id_test")
    @JsonIgnore
    private Test test;

//    @OneToOne(mappedBy = "parameter")
//    @JsonIgnore
//    private Range range;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_measureparameter", referencedColumnName = "id")
    @JsonIgnore
    private MeasureParameter measureParameter;
}

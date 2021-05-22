package pb.wi.cohp.domain.measureParameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pb.wi.cohp.domain.measure.Measure;
import pb.wi.cohp.domain.parameter.Parameter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
@Builder
public class MeasureParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Double value;

    @Setter
    private Boolean hidden;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Measure.class, cascade = CascadeType.ALL)
    @JoinColumn(name="id_measure")
    @JsonIgnore
    private Measure measure;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = pb.wi.cohp.domain.parameter.Parameter.class, cascade = CascadeType.ALL)
    @JoinColumn(name="id_parameter")
    @JsonIgnore
    private Parameter parameter;

}

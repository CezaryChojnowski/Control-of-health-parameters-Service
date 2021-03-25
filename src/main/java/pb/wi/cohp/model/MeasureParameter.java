package pb.wi.cohp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeasureParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Double value;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Measure.class)
    @JoinColumn(name="id_measure")
    @JsonIgnore
    private Measure measure;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Parameter.class)
    @JoinColumn(name="id_parameter")
    @JsonIgnore
    private Parameter parameter;

}

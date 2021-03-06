package pb.wi.cohp.domain.parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pb.wi.cohp.domain.measureParameter.MeasureParameter;
//import pb.wi.cohp.domain.range.Range;
import pb.wi.cohp.domain.range.Range;
import pb.wi.cohp.domain.test.Test;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotEmpty(message = "{parameter.name.notEmpty}")
    private String name;

    @Setter
    @Column(columnDefinition = "boolean default false")
    private Boolean hidden;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Test.class)
    @JoinColumn(name="id_test")
    @JsonIgnore
    private Test test;

//    @OneToMany(mappedBy = "parameter")
//    @JsonIgnore
//    private Range range;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_measureparameter", referencedColumnName = "id")
//    @JsonIgnore
//    private MeasureParameter measureParameter;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parameter")
    @JsonIgnore
    private List<Range> ranges = new ArrayList<>();
}

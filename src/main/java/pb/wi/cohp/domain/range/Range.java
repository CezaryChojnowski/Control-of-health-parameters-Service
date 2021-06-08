package pb.wi.cohp.domain.range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "paramRange")
@Builder
@EqualsAndHashCode
public class Range {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Double minValue;

    @Setter
    private Double maxValue;

    @Setter
    private boolean hidden;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="id_user")
    @JsonIgnore
    private User user;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_parameter", referencedColumnName = "id")
//    private Parameter parameter;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Parameter.class)
    @JoinColumn(name="id_parameter")
    @JsonIgnore
    private Parameter parameter;
}

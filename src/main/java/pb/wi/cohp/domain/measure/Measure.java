package pb.wi.cohp.domain.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.measureParameter.MeasureParameter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private LocalDate date;

    @Setter
    private String note;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="id_user")
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "measure")
    @JsonIgnore
    private List<MeasureParameter> measureParameterList = new ArrayList<>();

}

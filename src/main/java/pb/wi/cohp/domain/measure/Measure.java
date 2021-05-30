package pb.wi.cohp.domain.measure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pb.wi.cohp.domain.test.Test;
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
@EqualsAndHashCode
@Builder
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private LocalDate date;

    @Setter
    private String note;

    @Setter
    @Column(columnDefinition = "boolean default false")
    private Boolean hidden;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="id_user")
    @JsonIgnore
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Test.class)
    @JoinColumn(name="id_test")
    @JsonIgnore
    private Test test;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, targetEntity = MeasureParameter.class)
    @JsonIgnore
    private List<MeasureParameter> measureParameterList;

}

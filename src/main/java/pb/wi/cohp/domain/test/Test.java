package pb.wi.cohp.domain.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pb.wi.cohp.domain.disease.Disease;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.reminder.Reminder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="id_user")
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "test")
    private Reminder reminder;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Disease.class)
    @JoinColumn(name="id_disease")
    @JsonIgnore
    private Disease disease;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "test")
    @JsonIgnore
    private List<Parameter> parameters = new ArrayList<>();


}
package pb.wi.cohp.domain.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pb.wi.cohp.domain.disease.Disease;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.reminder.Reminder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotEmpty(message = "{test.name.notEmpty}")
    private String name;

    boolean owner = false;

    @Setter
    @Column(columnDefinition = "boolean default false")
    private Boolean hidden;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name="id_user")
    @JsonIgnore
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "test")
    private List<Reminder> reminders = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Disease.class)
    @JoinColumn(name="id_disease")
    @JsonIgnore
    private Disease disease;

    @Setter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "test")
    private List<Parameter> parameters = new ArrayList<>();
}

package pb.wi.cohp.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pb.wi.cohp.config.validator.ValidEmail;
import pb.wi.cohp.config.validator.ValidPersonalIdNumber;
import pb.wi.cohp.domain.disease.Disease;
import pb.wi.cohp.domain.measure.Measure;
import pb.wi.cohp.domain.range.Range;
import pb.wi.cohp.domain.reminder.Reminder;
import pb.wi.cohp.domain.role.Role;
import pb.wi.cohp.domain.test.Test;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
@PropertySource("classpath:PL.validation.messages.properties")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotEmpty(message = "{user.username.notEmpty}")
    private String username;

    @Setter
    @NotEmpty(message = "{user.firstName.notEmpty}")
    private String firstName;

    @Setter
    @NotEmpty(message = "{user.lastName.notEmpty}")
    private String lastName;

    @Setter
    @ValidPersonalIdNumber
    private String personalIdNumber;

    @Setter
    @ValidEmail
    private String email;

    @Setter
    private String password;

    @Setter
    private boolean active;

    @Setter
    private String token;

    @Setter
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Measure> measurement = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Reminder> reminders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Disease> diseases = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Test> tests = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Range> ranges = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<Role> roles = new HashSet<>();

}
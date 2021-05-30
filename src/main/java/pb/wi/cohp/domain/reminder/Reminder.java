package pb.wi.cohp.domain.reminder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull(message = "{reminder.date.notEmpty}")
    private LocalDate date;

    @Setter
    @NotNull(message = "{reminder.time.notEmpty}")
    private LocalTime time;

    @Setter
    private String note;

    @Setter
    private boolean emailReminder = false;

    @Setter
    private boolean smsReminder = false;

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
}

package pb.wi.cohp.domain.reminder;


import org.springframework.data.repository.CrudRepository;
import pb.wi.cohp.domain.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {
    List<Reminder> findAllByUserAndHiddenFalse(User user);

    List<Reminder> findAllByDateAndTimeAndHiddenFalse(LocalDate date, LocalTime time);

    Optional<Reminder> findByIdAndHiddenFalse(Long id);
}

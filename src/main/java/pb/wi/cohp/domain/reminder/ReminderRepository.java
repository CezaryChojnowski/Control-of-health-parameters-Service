package pb.wi.cohp.domain.reminder;


import org.springframework.data.repository.CrudRepository;
import pb.wi.cohp.domain.user.User;

import java.util.List;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {
    List<Reminder> findAllByUser(User user);
}

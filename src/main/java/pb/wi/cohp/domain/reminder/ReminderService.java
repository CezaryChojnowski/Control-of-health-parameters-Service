package pb.wi.cohp.domain.reminder;

import org.springframework.stereotype.Service;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestService;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.user.UserService;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReminderService {

    final ReminderRepository reminderRepository;

    final UserService userService;

    final TestService testService;

    public ReminderService(ReminderRepository reminderRepository, UserService userService, TestService testService) {
        this.reminderRepository = reminderRepository;
        this.userService = userService;
        this.testService = testService;
    }

    public Reminder createReminder(LocalDate date,
                                   LocalTime time,
                                   String note,
                                   boolean emailReminder,
                                   boolean smsReminder,
                                   String username,
                                   Long idTest){
        User user = userService.getUserByUsername(username);
        Test test = testService.findTestById(idTest);
        return reminderRepository
                    .save(
                            new Reminder
                                    .ReminderBuilder()
                                    .date(date)
                                    .time(time)
                                    .note(note)
                                    .emailReminder(emailReminder)
                                    .smsReminder(smsReminder)
                                    .user(user)
                                    .test(test)
                                    .build());
    }
}

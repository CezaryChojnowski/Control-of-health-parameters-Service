package pb.wi.cohp.domain.reminder;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.InvalidDataException;
import pb.wi.cohp.config.error.exception.ObjectNotFoundException;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestService;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.user.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@PropertySource("classpath:en.exception.messages.properties")
public class ReminderService {

    final Environment env;

    final ReminderRepository reminderRepository;

    final UserService userService;

    final TestService testService;

    public ReminderService(Environment env, ReminderRepository reminderRepository, UserService userService, TestService testService) {
        this.env = env;
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
        if(!validDateAndTime(date, time)){
            throw new InvalidDataException(env.getProperty("invalidReminderDataAndTime"));
        }
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

    public boolean validDateAndTime(LocalDate date, LocalTime time){
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        System.out.println(currentLocalDateTime);
        LocalDateTime dataTime = LocalDateTime.of(date, time);
        System.out.println(dataTime);
        return dataTime.isAfter(currentLocalDateTime);
    }

    public void deleteReminder(Long reminderId){
        try{
            reminderRepository.deleteById(reminderId);
        }catch (Exception exception){
            throw new ObjectNotFoundException(env.getProperty("reminderNotFound"));
        }
    }

    public List<Reminder> getReminders(String username){
        return reminderRepository.findAllByUser(userService.getUserByUsername(username));
    }
}

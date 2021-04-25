package pb.wi.cohp;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pb.wi.cohp.domain.email.EmailService;
import pb.wi.cohp.domain.reminder.Reminder;
import pb.wi.cohp.domain.reminder.ReminderService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ScheduledTask {

    final ReminderService reminderService;

    final EmailService emailService;

    public ScheduledTask(ReminderService reminderService, EmailService emailService) {
        this.reminderService = reminderService;
        this.emailService = emailService;
    }

    @Transactional
    @Scheduled(cron = "0 * * ? * *")
    public void sendMessageWithReminder(){
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.of(LocalTime.now().getHour(),
                LocalTime.now().getMinute(),
                0,
                0);
        List<Reminder> reminders = reminderService.getReminderByDateAndTime(currentDate,
                currentTime);
        if(!reminders.isEmpty()){
            for(Reminder reminder: reminders){
                emailService.sendEmailWithReminder(
                        reminder.getUser().getEmail(),
                        reminder.getDate(),
                        reminder.getTime(),
                        reminder.getTest().getName()
                );
                reminderService.deleteReminder(reminder.getId());
            }
        }
    }
}

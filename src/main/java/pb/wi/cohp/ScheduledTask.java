package pb.wi.cohp;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
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
                if(reminder.isEmailReminder()) {
                    emailService.sendEmailWithReminder(
                            reminder.getUser().getEmail(),
                            reminder.getDate(),
                            reminder.getTime(),
                            reminder.getTest().getName(),
                            reminder.getNote()
                    );
                }
                if(reminder.isSmsReminder() && !reminder.getUser().getPhoneNumber().isEmpty()){
                    String phoneNumber = reminder.getUser().getPhoneNumber();
                    if(!(phoneNumber.length()=="+48730301753".length())){
                        String prefix = "+48";
                        phoneNumber = prefix + phoneNumber;
                    }
                    Twilio.init("ACb2bcda9ff7e45b210431398763a2894f", "d2e9df8d174a2d2121a48a2066190153");
                    Message message = Message.creator(
                            new PhoneNumber(phoneNumber),
                            new PhoneNumber("+12156420894"),
                            "Don't forget to measure " + reminder.getTest().getName() + " " + reminder.getDate() + " " + reminder.getTime() + "|"+ reminder.getNote()
                    ).create();
                }
                reminderService.deleteReminder(reminder.getId());
            }
        }
    }
}

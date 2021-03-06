package pb.wi.cohp.domain.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pb.wi.cohp.domain.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    final JavaMailSender emailSender;

    final UserRepository userRepository;

    public EmailService(JavaMailSender emailSender, UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    public void sendEmailWithToken(String email){
        String token = userRepository.findUserByEmail(email).get().getToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(email);
        message.setSubject("Your token to activate account");
        message.setText("https://cohp.herokuapp.com/auth/activate-account/"+email+"/"+token);
        emailSender.send(message);
    }

    public void sendEmailWithPasswordAndLogin(String password, String username, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(email);
        message.setSubject("Your password and username");
        message.setText("Username: " + username +"\n" + "Password: " + password);
        emailSender.send(message);
    }

    public void sendEmailWithTokenToResetPassword(String email) {
        String token = userRepository.findUserByEmail(email).get().getResetPasswordToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(email);
        message.setSubject("Your token to reset password account");
        message.setText("https://cohp.herokuapp.com/auth/reset-password/"+email+"/"+token);
        emailSender.send(message);
    }

    public void sendEmailWithReminder(String email, LocalDate date, LocalTime time, String testName, String note) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(email);
        message.setSubject("Don't forget to measure " + testName);
        message.setText(date + " " + time + " " + testName + "|"+ note);
        emailSender.send(message);
    }
}

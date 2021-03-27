package pb.wi.cohp.domain.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pb.wi.cohp.domain.user.UserRepository;

@Service
public class EmailService {

    final JavaMailSender emailSender;

    final UserRepository userRepository;

    public EmailService(JavaMailSender emailSender, UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    public void sendEmailWithToken(String email){
            String token = userRepository.findUserByEmail(email).get().getToken();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your token to activate account");
            message.setText("http://localhost:8080/users/"+email+"/"+token);
            emailSender.send(message);

    }
}

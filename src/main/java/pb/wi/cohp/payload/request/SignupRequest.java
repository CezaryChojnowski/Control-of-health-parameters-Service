package pb.wi.cohp.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.PropertySource;
import pb.wi.cohp.config.validator.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@ToString
public class SignupRequest {
    @NotEmpty(message = "{user.username.notEmpty}")
    private String firstName;

    @NotEmpty(message = "{user.lastName.notEmpty}")
    private String lastName;

    @ValidEmail(message = "{user.email.valid}")
    @UniqueEmail(message = "{user.email.unique}")
    private String email;

    @UniqueUsername(message = "{user.username.unique}")
    @NotEmpty(message = "{user.username.notEmpty}")
    private String username;

    @ValidPassword(message = "{user.password.valid}")
    private String password;

    @ValidPersonalIdNumber(message = "{user.personalIdNumber.valid}")
    @UniquePersonalIdNumber(message = "{user.personalIdNumber.unique}")
    private String personalIdNumber;

    @ValidPhoneNumber(message = "{user.phoneNumber.valid}")
    private String phoneNumber;
}

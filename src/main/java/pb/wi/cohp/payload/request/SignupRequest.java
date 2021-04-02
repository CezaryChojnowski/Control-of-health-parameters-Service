package pb.wi.cohp.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.PropertySource;

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
    private String email;
    private String username;
    private String password;
    private String personalIdNumber;
//    private Set<String> roles;
}

package pb.wi.cohp.payload.request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pb.wi.cohp.config.validator.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class SignupRequestAdmin {
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

    @ValidPersonalIdNumber(message = "{user.personalIdNumber.valid}")
    @UniquePersonalIdNumber(message = "{user.personalIdNumber.unique}")
    private String personalIdNumber;
}

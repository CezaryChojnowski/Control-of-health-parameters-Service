package pb.wi.cohp.domain.user;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String personalIdNumber;
    private String email;
    private boolean active;
}


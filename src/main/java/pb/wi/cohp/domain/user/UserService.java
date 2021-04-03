package pb.wi.cohp.domain.user;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.IncorrectTokenException;
import pb.wi.cohp.config.error.exception.UserNotFoundException;
import pb.wi.cohp.domain.role.ERole;
import pb.wi.cohp.domain.role.Role;
import pb.wi.cohp.domain.role.RoleRepository;
import pb.wi.cohp.payload.response.MessageResponse;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@PropertySource("classpath:en.exception.messages.properties")
@PropertySource("classpath:en.validation.messages.properties")
public class UserService {
    final UserRepository userRepository;

    final Environment env;

    final PasswordEncoder encoder;

    final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, Environment env, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.env = env;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(String username,
                           String firstName,
                           String lastName,
                           String personalIdNumber,
                           String email,
                           String password,
                           String token,
                           boolean active){
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER.name());
        roles.add(userRole);
        User user = new User.UserBuilder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .email(email)
                .personalIdNumber(personalIdNumber)
                .roles(roles)
                .active(active)
                .token(token)
                .build();
        user.setPassword(encoder.encode(password));
        return userRepository.save(user);
    }

    public boolean checkIfUserHasActivatedAccount(String username){
        return userRepository.findUserByUsername(username).get().isActive();
    }

    public String generateTokenToActiveAccount(){
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 20;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public User getUserByEmail(String email){
        return userRepository.findUserByEmail(email).get();
    }

    public ResponseEntity<?> activateAccount(String email,
                                   String token){
        try{
            User user = getUserByEmail(email);
            if(token.equals(user.getToken())){
                user.setActive(true);
                user.setToken("");
                userRepository.save(user);
                return ResponseEntity.ok(new MessageResponse(env.getProperty("successfulActivateAccount")));
            }
        }catch (Exception ignored){
            throw new UserNotFoundException(env.getProperty("emailNotFound"));
        }
        throw new IncorrectTokenException(env.getProperty("badToken"));
    }
}

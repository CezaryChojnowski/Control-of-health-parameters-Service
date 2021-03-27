package pb.wi.cohp.rest;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import pb.wi.cohp.config.error.exception.UserDoesNotHaveActiveAccountException;
import pb.wi.cohp.config.jwt.JwtUtils;
import pb.wi.cohp.config.jwt.service.UserDetailsImpl;
import pb.wi.cohp.domain.email.EmailService;
import pb.wi.cohp.payload.request.LoginRequest;
import pb.wi.cohp.payload.response.JwtResponse;
import pb.wi.cohp.payload.response.MessageResponse;
import pb.wi.cohp.domain.role.RoleRepository;
import pb.wi.cohp.domain.user.UserService;
import pb.wi.cohp.payload.request.SignupRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:PL.exception.messages.properties")
public class UserController {

    final UserService userService;

    final RoleRepository roleRepository;

    final Environment env;

    final JwtUtils jwtUtils;

    final AuthenticationManager authenticationManager;

    final EmailService emailService;

    public UserController(UserService userService, RoleRepository roleRepository, Environment env, JwtUtils jwtUtils, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.env = env;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest){
        userService.createUser(
                signupRequest.getUsername(),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getPersonalIdNumber(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                userService.generateTokenToActiveAccount(),
                false
        );
        emailService.sendEmailWithToken(signupRequest.getEmail());

        return ResponseEntity.ok(new MessageResponse(env.getProperty("successfulRegistration")));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        if(!userService.checkIfUserHasActivatedAccount(loginRequest.getUsername())){
            throw new UserDoesNotHaveActiveAccountException(env.getProperty("inactiveAccount"));
        }
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @GetMapping("/{email}/{token}")
    public ResponseEntity<?> authenticateUser(@PathVariable String email,
                                              @PathVariable String token) {
        return userService.activateAccount(email,
                token);
    }

}

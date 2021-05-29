package pb.wi.cohp.rest;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import pb.wi.cohp.config.error.exception.UserDoesNotHaveActiveAccountException;
import pb.wi.cohp.config.jwt.JwtUtils;
import pb.wi.cohp.config.jwt.service.UserDetailsImpl;
import pb.wi.cohp.domain.email.EmailService;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.user.UserDTO;
import pb.wi.cohp.payload.request.LoginRequest;
import pb.wi.cohp.payload.request.SignupRequestAdmin;
import pb.wi.cohp.payload.response.CurrentUser;
import pb.wi.cohp.payload.response.JwtResponse;
import pb.wi.cohp.payload.response.MessageResponse;
import pb.wi.cohp.domain.role.RoleRepository;
import pb.wi.cohp.domain.user.UserService;
import pb.wi.cohp.payload.request.SignupRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:en.exception.messages.properties")
@CrossOrigin
@Validated
public class UserController {

    final UserService userService;

    final RoleRepository roleRepository;

    final Environment env;

    final JwtUtils jwtUtils;

    final AuthenticationManager authenticationManager;

    final EmailService emailService;

    final ModelMapper modelMapper;

    public UserController(UserService userService, RoleRepository roleRepository, Environment env, JwtUtils jwtUtils, AuthenticationManager authenticationManager, EmailService emailService, ModelMapper modelMapper) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.env = env;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signupRequest){
        userService.createUser(
                signupRequest.getUsername(),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getPersonalIdNumber(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                userService.generateSomeToken(),
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
    public ResponseEntity<?> activeAccount(@PathVariable String email,
                                              @PathVariable String token) {
        return userService.activateAccount(email,
                token);
    }

    @PutMapping("/token/{email}")
     public void setTokenToResetPassword(@PathVariable String email){
        userService.setTokenToResetPassword(email);
    }

    @PutMapping("/token/{email}/{token}/{password}")
    public ResponseEntity<?> changePassword(@PathVariable String email,
                                  @PathVariable String token,
                                  @PathVariable String password){
        User user = userService.changePassword(email, token, password);
        return ResponseEntity.ok(convertToDto(user));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getUsers();

        return ResponseEntity.ok(users
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    private UserDTO convertToDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{email}")
    public ResponseEntity<?> activeAccount(@PathVariable String email){
        return ResponseEntity
                .ok(
                        convertToDto(
                                userService
                                .activateAccount(
                                        email
                                )
                        )
                );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createAdminAccount(@Valid @RequestBody SignupRequestAdmin admin){
        User user = userService.createUser(
                admin.getUsername(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getPersonalIdNumber(),
                admin.getEmail()
        );
        return ResponseEntity.ok(
                convertToDto(user)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/{email}")
    public ResponseEntity<?> changeRole(@PathVariable String email){
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(
                convertToDto(userService.changeRole(user))
        );
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getInfoAboutCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();

        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        return ResponseEntity.ok(new CurrentUser(
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
}

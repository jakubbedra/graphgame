package pl.edu.pg.eti.graphgame.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.users.UserRole;
import pl.edu.pg.eti.graphgame.users.dto.CreateUserRequest;
import pl.edu.pg.eti.graphgame.users.dto.GetUserResponse;
import pl.edu.pg.eti.graphgame.users.dto.GetUsersResponse;
import pl.edu.pg.eti.graphgame.users.dto.UpdateUserRequest;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import java.util.Optional;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserRole DEFAULT_USER_ROLE;

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final UserService userService;

    @Autowired
    public UserController(
            JavaMailSender mailSender,
            UserService userService
    ) {
        this.DEFAULT_USER_ROLE = UserRole.STUDENT;
        this.mailSender = mailSender;
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder(11);
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserRequest request) {
        User user = User.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(DEFAULT_USER_ROLE)
                .build();
        try {
            userService.registerNewUserAccount(user);
            sendAccountActivationEmail(user);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("id") Long id) {
        return userService.findUser(id)
                .map(value -> ResponseEntity.ok(GetUserResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsers() {
        return ResponseEntity.ok(
                GetUsersResponse.entityToDtoMapper().apply(userService.findAllUsers())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest request, @PathVariable("id") Long id) {
        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            UpdateUserRequest.dtoToEntityUpdater().apply(user.get(), request);
            userService.updateUser(user.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        Optional<User> user = userService.findUser(id);
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void sendAccountActivationEmail(User user) {

    }

}

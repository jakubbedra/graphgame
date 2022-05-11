package pl.edu.pg.eti.graphgame.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import pl.edu.pg.eti.graphgame.exceptions.EmailAlreadyInUseException;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.exceptions.UserSessionTokenAlreadyExistsException;
import pl.edu.pg.eti.graphgame.users.dto.*;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.entity.UserSession;
import pl.edu.pg.eti.graphgame.users.service.UserService;
import pl.edu.pg.eti.graphgame.users.service.UserSessionService;

import java.util.*;

@RequestMapping("/api/users")
@RestController
public class UserController {


    private final String DEFAULT_USER_ROLE;

    private final JavaMailSender mailSender;

    private final UserService userService;
    private final UserSessionService userSessionService;

    @Autowired
    public UserController(
            JavaMailSender mailSender,
            UserService userService,
            UserSessionService userSessionService
    ) {
        this.DEFAULT_USER_ROLE = "ROLE_USER";
        this.mailSender = mailSender;
        this.userService = userService;
        this.userSessionService = userSessionService;
    }



    @GetMapping("/session")
    public ResponseEntity<String> getSession(@RequestParam("token") String token) {
        Optional<UserSession> session = userSessionService.findSessionByToken(token);
        if(session.isEmpty())
            return ResponseEntity.notFound().build();
        String resp = token + " : " + session.get().getUser().getEmail() + " ; " + session.get().getExpirationDatetime();
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/prolong_session")
    public ResponseEntity<Void> prolongToken(@RequestParam("token") String token) {
        Optional<UserSession> session = userSessionService.findSessionByToken(token);
        if(session.isEmpty())
            return ResponseEntity.notFound().build();
        userSessionService.prolongUserSession(session.get());
        return ResponseEntity.accepted().build();
    }


    @DeleteMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestParam("token") String token) {
        Optional<UserSession> session = userSessionService.findSessionByToken(token);
        if(session.isEmpty())
            return ResponseEntity.notFound().build();
        userSessionService.logoutUserFromSession(session.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest request)
        throws UserSessionTokenAlreadyExistsException {
		Optional<UserSession> userSession = userService.loginUserWithPassword(request.getEmail(), request.getPassword());
        return userSession.map(session -> ResponseEntity
                .ok(LoginUserResponse.builder()
                    .username(session.getUser().getLogin())
                    ._token(session.getToken())
                    ._tokenExpirationTime(UserSessionService.DEFAULT_SESSION_TOKEN_EXPIRATION_TIME_SECONDS+"")
                    .user_id(session.getUser().getId())
                    .build()))
                .orElse(null);
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserRequest request) {
        User user = User.builder()
                .login(request.getUsername())
                .email(request.getEmail())
                .roles(DEFAULT_USER_ROLE)
                .build();
        try {
            userService.registerNewUserAccountWithPassword(user, request.getPassword());
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
    public ResponseEntity<Void> updateUser(@RequestBody UpdateUserRequest request, @PathVariable("id") Long id)
        throws EmailAlreadyInUseException {
        Optional<User> user = userService.findUser(id);
        // TODO: check for session token and current user permissions
        if (user.isPresent()) {
            if(request.getPassword() != null) {
                if(!request.getPassword().equals(""))
                    userService.updatePassword(user.get(), request.getPassword());
            }
            if(request.getUsername() != null) {
                if(!request.getUsername().equals(""))
                    userService.updateLogin(user.get(), request.getUsername());
            }
            if(request.getEmail() != null) {
                if(!request.getEmail().equals(""))
                    userService.updateEmail(user.get(), request.getEmail());
            }
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

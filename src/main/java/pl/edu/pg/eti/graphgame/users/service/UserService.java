package pl.edu.pg.eti.graphgame.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.repository.UserRepository;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository
    ) {
        this.passwordEncoder = new BCryptPasswordEncoder(11);
        this.userRepository = userRepository;
    }

    public void registerNewUserAccount(User user) {

    }

}

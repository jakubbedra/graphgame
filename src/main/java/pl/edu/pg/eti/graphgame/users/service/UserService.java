package pl.edu.pg.eti.graphgame.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> findUserByName(String name) {
        return userRepository.findByLogin(name);
    }

    @Transactional
    public void registerNewUserAccount(User user) throws UserAlreadyExistsException {
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistsException("A user with the given email already exists.");
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}

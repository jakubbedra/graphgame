package pl.edu.pg.eti.graphgame.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.pg.eti.graphgame.exceptions.EmailAlreadyInUseException;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.exceptions.UserSessionTokenAlreadyExistsException;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.entity.UserSession;
import pl.edu.pg.eti.graphgame.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
	
	private final UserSessionService userSessionService;


	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

    @Autowired
    public UserService(
            UserRepository userRepository,
			UserSessionService userSessionService
    ) {
        this.userRepository = userRepository;
		this.userSessionService = userSessionService;
        this.passwordEncoder = new BCryptPasswordEncoder(11);
    }
	
	public Optional<UserSession> loginUserWithPassword(String email, String password) throws
		UserSessionTokenAlreadyExistsException {
		Optional<User> user = findUserByEmail(email);
		if(user.isEmpty())
			return Optional.empty();
		if(!passwordEncoder.matches(password, user.get().getPasswordEncoded()))
			return Optional.empty();
		return userSessionService.createNewSessionForUser(user.get());
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
    public void registerNewUserAccountWithPassword(User user, String password) throws UserAlreadyExistsException {
        if(emailExists(user.getEmail())) {
            throw new UserAlreadyExistsException("A user with the given email already exists.");
        }
		user.setPasswordEncoded(passwordEncoder.encode(password));
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

    private Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	@Transactional
	public void updatePassword(User user, String password) {
		user.setPasswordEncoded(encodePassword(password));
		updateUser(user);
	}

	@Transactional
	public void updateEmail(User user, String email)
		throws EmailAlreadyInUseException {
		if(emailExists(user.getEmail())) {
			throw new EmailAlreadyInUseException("New email is already in use.");
		}
		user.setEmail(email);
		updateUser(user);
	}

	public void updateLogin(User user, String login) {
		user.setLogin(login);
		updateUser(user);
	}
}

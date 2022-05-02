package pl.edu.pg.eti.graphgame.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.exceptions.*;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.entity.UserSession;
import pl.edu.pg.eti.graphgame.users.repository.UserSessionRepository;

import java.util.Optional;
import java.util.UUID;

import java.time.*;

@Service
public class UserSessionService {
	
    public static final int DEFAULT_SESSION_TOKEN_EXPIRATION_TIME_SECONDS = 3600;
	private final UserSessionRepository userSessionRepository;

    @Autowired
    public UserSessionService(
			UserSessionRepository userSessionRepository
    ) {
		this.userSessionRepository = userSessionRepository;
    }
	
	public String getCurrentSessionExpirationDatetime() {
		return (LocalDateTime.now().plusHours(1)).toString();
	}
	
	public boolean isDatetimeValid(String expirationDatetime) {
		return LocalDateTime.parse(expirationDatetime)
			.isBefore(LocalDateTime.now());
	}
	
	@Transactional
	public Optional<UserSession> createNewSessionForUser(User user) throws
		UserSessionTokenAlreadyExistsException {
		String token = UUID.randomUUID().toString();
		UserSession userSession = UserSession.builder()
			.token(token)
			.user(user)
			.expirationDatetime(getCurrentSessionExpirationDatetime())
			.build();
		saveUserSessionIfCanBeSaved(userSession);
		return Optional.of(userSession);
	}
	
	@Transactional
	public void saveUserSessionIfCanBeSaved(UserSession userSession) throws
		UserSessionTokenAlreadyExistsException {
		if(tokenExists(userSession.getToken())) {
			throw new UserSessionTokenAlreadyExistsException("Token already exists.");
		}
		userSessionRepository.save(userSession);
	}

	@Transactional
	public void prolongUserSession(UserSession session) {
		session.setExpirationDatetime(getCurrentSessionExpirationDatetime());
		userSessionRepository.save(session);
	}
	
	public Optional<UserSession> findSessionByToken(String token) {
		return userSessionRepository.findById(token);
	}
	
	public boolean tokenExists(String token) {
		return userSessionRepository.findById(token).isPresent();
	}

	@Transactional
	public void logoutUserFromSession(UserSession session) {
		userSessionRepository.delete(session);
	}
}


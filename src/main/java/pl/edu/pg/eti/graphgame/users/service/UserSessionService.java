package pl.edu.pg.eti.graphgame.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.exceptions.*;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.entity.UserSession;
import pl.edu.pg.eti.graphgame.users.repository.UserSessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.time.*;

@Service
public class UserSessionService {
	
    public static final int DEFAULT_SESSION_TOKEN_EXPIRATION_TIME_SECONDS = 3600;
	private final UserSessionRepository userSessionRepository;
	private final TaskService taskService;

	@Autowired
    public UserSessionService(
			UserSessionRepository userSessionRepository,
			TaskService taskService) {
		this.userSessionRepository = userSessionRepository;
		this.taskService = taskService;
	}

	public String getCurrentSessionExpirationDatetime() {
		return (LocalDateTime.now().plusSeconds(DEFAULT_SESSION_TOKEN_EXPIRATION_TIME_SECONDS)).toString();
	}
	
	public boolean isDatetimeValid(String expirationDatetime) {
		return LocalDateTime.now().toString().compareTo(expirationDatetime) < 0;
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
		session.setExpirationDatetime(
			getCurrentSessionExpirationDatetime());
		userSessionRepository.save(session);
	}
	
	public Optional<UserSession> findSessionByToken(String token) {
		return userSessionRepository.findById(token);
	}
	
	public boolean tokenExists(String token) {
		return userSessionRepository.findById(token).isPresent();
	}

	public void logoutUserFromSession(UserSession session) {
		safelyDeleteSession(session);
	}

	@Transactional
	private void safelyDeleteSession(UserSession session) {
		if(userSessionRepository.findById(session.getToken()).isPresent()) {
			userSessionRepository.delete(session);
		}
	}

	@Transactional
	private void safelyDeleteSession(String token) {
		if(userSessionRepository.findById(token).isPresent()) {
			userSessionRepository.deleteById(token);
		}
	}

	@Transactional
	public void clearAllExpiredSessions() {
		userSessionRepository.deleteAllExpiredBefore(LocalDateTime.now().toString());
	}



	public boolean hasAccess(String token, Long userId) {
		return getResponseTokenAccessUser(token, userId)==null;
	}
	public ResponseEntity getResponseTokenAccessUser(String token, Long userId) {
		if(token == null)
			return ResponseEntity.status(401).body("Access denied: token is needed");
		if(userId == null)
			return ResponseEntity.status(401).body("Access denied: access only for specific user is allowed");
		Optional<UserSession> session = findSessionByToken(token);
		if(session.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		if(session.get().getUser() == null) {
			return ResponseEntity.status(401).body("Access denied: User not logged in");
		}
		if(!session.get().getUser().getId().equals(userId)) {
			return ResponseEntity.status(401).body("Access denied: userId=" + userId);
		}
		return null;
	}

	public boolean hasTaskAccess(String token, String taskUuid) {
		return getResponseTokenAccessTask(token, taskUuid)==null;
	}
	public ResponseEntity getResponseTokenAccessTask(String token, String taskUuid) {
		if(taskUuid == null)
			return ResponseEntity.status(404).body("task uuid = null");
		Optional<Task> task = taskService.findTask(taskUuid);
		if(task.isEmpty())
			return ResponseEntity.status(404).body("no task with given uuid: " + taskUuid);
		if(task.get().getUser() == null) {
			// TODO: does it mean that not logged in user is doing a task?
			return null;
		}
		return getResponseTokenAccessUser(token, task.get().getUser().getId());
	}
}


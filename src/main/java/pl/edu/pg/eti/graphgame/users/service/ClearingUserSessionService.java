package pl.edu.pg.eti.graphgame.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ClearingUserSessionService {

	private final UserSessionService userSessionService;

	@Autowired
    public ClearingUserSessionService(UserSessionService userSessionService) {
		this.userSessionService = userSessionService;
	}

	@Scheduled(fixedRate = 600000L)
	public void clearExpiredSessions() {
		userSessionService.clearAllExpiredSessions();
	}
}


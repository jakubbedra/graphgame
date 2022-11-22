package pl.edu.pg.eti.graphgame.redirect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import pl.edu.pg.eti.graphgame.exceptions.UsernameAlreadyInUseException;
import pl.edu.pg.eti.graphgame.exceptions.UserAlreadyExistsException;
import pl.edu.pg.eti.graphgame.exceptions.UserSessionTokenAlreadyExistsException;
import pl.edu.pg.eti.graphgame.users.dto.*;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.entity.UserSession;
import pl.edu.pg.eti.graphgame.users.service.UserService;
import pl.edu.pg.eti.graphgame.users.service.UserSessionService;

import java.io.IOException;
import java.util.*;

@RestController
public class RedirectController {

	@Value("${:classpath:/public/index.html}")
	private Resource index;

	@GetMapping(value = {"/game", "/auth", "/top-charts", "/my-progress"}, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public ResponseEntity actions() throws IOException {
		return ResponseEntity.ok(new InputStreamResource(index.getInputStream()));
	}
}


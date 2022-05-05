package pl.edu.pg.eti.graphgame.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.users.dto.*;
import pl.edu.pg.eti.graphgame.users.entity.User;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequestMapping("/api/users/topChart")
@RestController
public class UserTopChartsController {
	private static final int MAX_USERS_PER_PAGE = 10;

	private final UserService userService;
	private final StatsService statsService;

	@Autowired
	public UserTopChartsController(
		UserService userService,
		StatsService statsService
	) {
		this.userService = userService;
		this.statsService = statsService;
	}

	@GetMapping("/overall/{page}")
	public ResponseEntity<GetTopUsersResponse> getTopUsersAlltime(@PathVariable("page") Integer page) {
		List<Stats> selectedStats = getTopUserStatsPage(page, Optional.empty(), false);
		if (selectedStats.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(
			GetTopUsersResponse
				.statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
				.apply(selectedStats)
		);
	}

	@GetMapping("/{taskName}/{page}")
	public ResponseEntity<GetTopUsersResponse> getTopUsersAlltime(
		@PathVariable("page") Integer page,
		@PathVariable("taskName") String taskName
//		@PathVariable("taskId") Long taskId
	) {
		GraphTaskSubject taskSubject = GraphTaskSubject.valueOf(taskName);
//		Optional<TaskSubject> task = taskService.findTaskById(taskId);
		if (taskSubject == null) {
			return ResponseEntity.notFound().build();
		}
		List<Stats> selectedStats = getTopUserStatsPage(page, Optional.of(taskSubject), false);
		if (selectedStats.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(
			GetTopUsersResponse
				.statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
				.apply(selectedStats)
		);
	}

	@GetMapping("/overall/{page}/today")
	public ResponseEntity<GetTopUsersResponse> getTopUsersToday(
		@PathVariable("page") Integer page
	) {
		List<Stats> selectedStats = getTopUserStatsPage(page, Optional.empty(), true);
		if (selectedStats.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(
			GetTopUsersResponse
				.statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
				.apply(selectedStats)
		);
	}

	@GetMapping("/{taskName}/{page}/today")
	public ResponseEntity<GetTopUsersResponse> getTopUsersToday(
		@PathVariable("page") Integer page,
		@PathVariable("taskName") String taskName
		//@PathVariable("taskId") Long taskId
	) {
		GraphTaskSubject taskSubject = GraphTaskSubject.valueOf(taskName);
		if (taskSubject == null) {
			return ResponseEntity.notFound().build();
		}
		List<Stats> selectedStats = getTopUserStatsPage(page, Optional.of(taskSubject), true);
		if (selectedStats.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(
			GetTopUsersResponse
				.statsListToTopUsersMapper((page - 1) * MAX_USERS_PER_PAGE + 1, page)
				.apply(selectedStats)
		);
	}

	private List<Stats> getTopUserStatsPage(int page, Optional<GraphTaskSubject> task, boolean today) {
		List<User> users = new ArrayList<>(userService.findAllUsers());

		if (users.size() - (page - 1) * MAX_USERS_PER_PAGE < 0) {
			return Collections.emptyList();
		}
		List<Stats> userStatsSummed = users.stream()
			.map(u -> getUserScoreSummed(u, task, today))
			.sorted(Comparator.comparingInt(u -> calculateScore(u.getCorrect(), u.getWrong())))
			.collect(Collectors.toList());

		List<Stats> selectedStats = new LinkedList<>();
		for (
			int i = (page - 1) * MAX_USERS_PER_PAGE;
			i < page * MAX_USERS_PER_PAGE && i < userStatsSummed.size();
			i++
		) {
			selectedStats.add(userStatsSummed.get(i));
		}
		return selectedStats;
	}

	private Stats getUserScoreSummed(User user, Optional<GraphTaskSubject> task, boolean today) {
		List<Stats> stats;
		if (!today) {
			if (task.isEmpty()) {
				stats = statsService.findAllStatsByUser(user);
			} else {
				stats = statsService.findAllStatsByUserAndTask(user, task.get());
			}
		} else {
			Date todayDate = new Date(System.currentTimeMillis());
			if (task.isEmpty()) {
				stats = statsService.findAllStatsByUserAndDate(user, todayDate);
			} else {
				stats = statsService.findAllStatsByUserAndTaskAndDate(user, task.get(), todayDate);
			}
		}
		AtomicInteger correct = new AtomicInteger();
		AtomicInteger wrong = new AtomicInteger();
		stats.forEach(s -> {
			correct.addAndGet(s.getCorrect());
			wrong.addAndGet(s.getWrong());
		});
		return Stats.builder()
			.correct(correct.get())
			.wrong(wrong.get())
			.user(user)
			.build();
	}

	private int calculateScore(int correctAnswers, int wrongAnswers) {
		if (correctAnswers == 0 && wrongAnswers == 0) {
			return Integer.MAX_VALUE;
		}
		return wrongAnswers - correctAnswers;
	}

}

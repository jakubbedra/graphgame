package pl.edu.pg.eti.graphgame.stats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.eti.graphgame.stats.dto.CreateStatsRequest;
import pl.edu.pg.eti.graphgame.stats.dto.GetStatsListResponse;
import pl.edu.pg.eti.graphgame.stats.dto.GetStatsResponse;
import pl.edu.pg.eti.graphgame.stats.dto.UpdateStatsRequest;
import pl.edu.pg.eti.graphgame.stats.enitity.Stats;
import pl.edu.pg.eti.graphgame.stats.service.StatsService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;
import pl.edu.pg.eti.graphgame.users.service.UserService;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/stats")
@RestController
public class StatsController {

    private final StatsService statsService;
    private final UserService userService;

    @Autowired
    public StatsController(
            StatsService statsService,
            UserService userService
    ) {
        this.statsService = statsService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createStats(@RequestBody CreateStatsRequest request) {
        statsService.saveStats(
                CreateStatsRequest.dtoToEntityMapper(
                        (id) -> userService.findUser(id).orElseThrow(),
                        GraphTaskSubject::valueOf,
                        () -> new Date(System.currentTimeMillis())
                ).apply(request)
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<GetStatsListResponse> getStatsList() {
        return ResponseEntity.ok(
                GetStatsListResponse.entityToDtoMapper().apply(statsService.findAllStats())
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<GetStatsResponse> getStats(@PathVariable("uuid") String uuid) {
        return statsService.findStatsByUuid(uuid)
                .map(value -> ResponseEntity.ok(GetStatsResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Void> updateStats(@RequestBody UpdateStatsRequest request, @PathVariable("uuid") String uuid) {
        Optional<Stats> stats = statsService.findStatsByUuid(uuid);
        if (stats.isPresent()) {
            UpdateStatsRequest.dtoToEntityUpdater().apply(stats.get(), request);
            statsService.updateStats(stats.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteStats(@PathVariable("uuid") String uuid) {
        Optional<Stats> stats = statsService.findStatsByUuid(uuid);
        if (stats.isPresent()) {
            statsService.deleteStats(stats.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

package pl.edu.pg.eti.graphgame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;
import pl.edu.pg.eti.graphgame.tasks.service.TaskService;

import javax.annotation.PostConstruct;

@Component
public class TasksConfig {

    private TaskSubject s1;
    private TaskSubject s2;

    private final TaskService taskService;

    @Autowired
    public TasksConfig(
            TaskService taskService
    ) {
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        s1 = TaskSubject.builder()
                .name(GraphTaskSubject.BFS.label)
                .build();
        s2 = TaskSubject.builder()
                .name(GraphTaskSubject.COMPLETE_GRAPHS.label)
                .build();
        taskService.saveTask(s1);
        taskService.saveTask(s2);
    }

}

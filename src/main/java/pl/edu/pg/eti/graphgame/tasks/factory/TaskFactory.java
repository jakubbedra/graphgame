package pl.edu.pg.eti.graphgame.tasks.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.eti.graphgame.config.Constants;
import pl.edu.pg.eti.graphgame.exceptions.UnsupportedTaskSubjectException;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskType;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.Random;
import java.util.UUID;

@Component
public class TaskFactory {

    private static final Random RANDOM = new Random();

    @Autowired
    public TaskFactory() {
    }

    /**
     * Creates a new task for the given user
     *
     * @param user user who requested a new Task
     * @return a new Task for the user
     */
    public Task createRandomTask(User user) {
        return createTask(GraphTaskSubject.randomSubject(), user);
    }

    private Task createTask(GraphTaskSubject subject, User user) {
        switch (subject) {
            case COMPLETE_GRAPHS:
                return createCompleteGraphTask(user);
            case BFS:
                return createBFSTask(user);
            default:
                throw new UnsupportedTaskSubjectException("");
        }
    }

    private Task createCompleteGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(Constants.MAX_GRAPH_VERTICES);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.COMPLETE_GRAPHS)
                .type(GraphTaskType.DRAW)
                .build();
    }

    private Task createBFSTask(User user) {
        int graphVertices = RANDOM.nextInt(Constants.MAX_GRAPH_VERTICES);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.BFS)
                .type(GraphTaskType.VERTEX_SELECTION)
                .build();
    }

}

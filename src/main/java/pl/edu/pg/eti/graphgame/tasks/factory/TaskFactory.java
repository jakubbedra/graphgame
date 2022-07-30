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
            case PATH_GRAPHS:
                return createPathGraphTask(user);
            case CYCLE_GRAPHS:
                return createCycleGraphTask(user);
            case STAR_GRAPHS:
                return createStarGraphTask(user);
            case WHEEL_GRAPHS:
                return createWheelGraphTask(user);
            default:
                throw new UnsupportedTaskSubjectException("");
            case BFS:
                return createBFSTask(user);
            case DFS:
                return createDFSTask(user);
        }
    }

    private Task createCompleteGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_COMPLETE_GRAPH_VERTICES - Constants.MIN_COMPLETE_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.COMPLETE_GRAPHS)
                .type(GraphTaskType.DRAW)
                .build();
    }

    private Task createPathGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.PATH_GRAPHS)
                .type(GraphTaskType.DRAW)
                .build();
    }

    private Task createCycleGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.CYCLE_GRAPHS)
                .type(GraphTaskType.DRAW)
                .build();
    }

    private Task createStarGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.STAR_GRAPHS)
                .type(GraphTaskType.DRAW)
                .build();
    }

    private Task createWheelGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.WHEEL_GRAPHS)
                .type(GraphTaskType.DRAW)
                .build();
    }

    private Task createBFSTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.BFS)
                .type(GraphTaskType.VERTEX_SELECTION)
                .build();
    }

    private Task createDFSTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.DFS)
                .type(GraphTaskType.VERTEX_SELECTION)
                .build();
    }

}

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
            case HYPERCUBES:
                return createHypercubesTask(user);
            case REGULAR_GRAPHS:
                return createRegularGraphTask(user);
            default:
                throw new UnsupportedTaskSubjectException("");
            case BFS:
                return createBFSTask(user);
            case DFS:
                return createDFSTask(user);
            case MAX_CLIQUE:
                return createMaxCliqueTask(user);
            case MAX_INDEPENDENT_SET:
                return createMaxIndependentSetTask(user);
            case MIN_VERTEX_COVER:
                return createMinVertexCoverTask(user);
            case EULER_CYCLE:
                return createEulerCycleTask(user);
        }
    }

    private Task createCompleteGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_COMPLETE_GRAPH_VERTICES - Constants.MIN_COMPLETE_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .graphEdges(graphEdges)
                .subject(GraphTaskSubject.COMPLETE_GRAPHS)
                .type(GraphTaskType.DRAW)
                .specialValues("")
                .build();
    }

    private Task createPathGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.PATH_GRAPHS)
                .type(GraphTaskType.DRAW)
                .specialValues("")
                .build();
    }

    private Task createCycleGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.CYCLE_GRAPHS)
                .type(GraphTaskType.DRAW)
                .specialValues("")
                .build();
    }

    private Task createStarGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.STAR_GRAPHS)
                .type(GraphTaskType.DRAW)
                .specialValues("")
                .build();
    }

    private Task createWheelGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.WHEEL_GRAPHS)
                .type(GraphTaskType.DRAW)
                .specialValues("")
                .build();
    }

    private Task createHypercubesTask(User user) {
        int graphVertices = Constants.HYPERCUBE_VERTICES[RANDOM.nextInt(Constants.HYPERCUBE_VERTICES.length)];
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.HYPERCUBES)
                .type(GraphTaskType.DRAW)
                .specialValues("")
                .build();
    }

    private Task createRegularGraphTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int k = RANDOM.nextInt(
                Constants.MAX_REGULAR_GRAPH_K - Constants.MIN_REGULAR_GRAPH_K
        ) + Constants.MIN_REGULAR_GRAPH_K;
        if (k >= graphVertices) {
            graphVertices += 2;
        }
        if (k * graphVertices % 2 != 0) {
            graphVertices++;
        }
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .subject(GraphTaskSubject.REGULAR_GRAPHS)
                .type(GraphTaskType.DRAW)
                .specialValues(k + ";")
                .build();
    }

    private Task createBFSTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .graphEdges(graphEdges)
                .subject(GraphTaskSubject.BFS)
                .type(GraphTaskType.VERTEX_SELECTION)
                .specialValues("")
                .build();
    }

    private Task createDFSTask(User user) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_GRAPH_VERTICES - Constants.MIN_GRAPH_VERTICES
        ) + Constants.MIN_GRAPH_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .graphEdges(graphEdges)
                .subject(GraphTaskSubject.DFS)
                .type(GraphTaskType.VERTEX_SELECTION)
                .specialValues("")
                .build();
    }

    private Task createVertexSetSelectionTask(User user, GraphTaskSubject subject) {
        int graphVertices = RANDOM.nextInt(
                Constants.MAX_MAX_CLIQUE_VERTICES - Constants.MIN_MAX_CLIQUE_VERTICES
        ) + Constants.MIN_MAX_CLIQUE_VERTICES;
        int graphEdges = RANDOM.nextInt(
                (graphVertices * graphVertices - graphVertices) / 2 - (graphVertices - 1)
        ) + (graphVertices - 1);
        graphEdges -= Math.max(RANDOM.nextInt(Constants.MIN_MAX_CLIQUE_VERTICES), graphVertices - 1);
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .graphVertices(graphVertices)
                .graphEdges(graphEdges)
                .subject(subject)
                .type(GraphTaskType.VERTEX_SELECTION)
                .specialValues("")
                .build();
    }

    private Task createMaxCliqueTask(User user) {
        return createVertexSetSelectionTask(user, GraphTaskSubject.MAX_CLIQUE);
    }

    private Task createMaxIndependentSetTask(User user) {
        return createVertexSetSelectionTask(user, GraphTaskSubject.MAX_INDEPENDENT_SET);
    }

    private Task createMinVertexCoverTask(User user) {
        return createVertexSetSelectionTask(user, GraphTaskSubject.MIN_VERTEX_COVER);
    }

    private Task createEulerCycleTask(User user) {
        return Task.builder()
                .uuid(UUID.randomUUID())
                .user(user)
                .subject(GraphTaskSubject.EULER_CYCLE)
                .type(GraphTaskType.VERTEX_SELECTION)
                .specialValues("")
                .build();
    }

}

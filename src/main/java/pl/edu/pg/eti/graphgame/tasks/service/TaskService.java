package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.graphs.service.GraphService;
import pl.edu.pg.eti.graphgame.tasks.GraphTaskSubject;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.factory.TaskFactory;
import pl.edu.pg.eti.graphgame.tasks.repository.TaskRepository;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final GraphService graphService;

    private final TaskFactory taskFactory;

     public Iterable<Task> findAll() {
         return taskRepository.findAll();
     }

    @Autowired
    public TaskService(
            TaskRepository taskRepository,
            GraphService graphService,
            TaskFactory taskFactory
    ) {
        this.taskRepository = taskRepository;
        this.graphService = graphService;
        this.taskFactory = taskFactory;
    }

    public Optional<Task> findTask(String uuid) {
        return taskRepository.findById(uuid);
    }

    public Optional<Task> findTaskOfUser(User user) {
        return taskRepository.findFirstByUser(user);
    }

    public Task createAndSaveTaskForUser(User user) {
        Task task = taskFactory.createRandomTask(user);
        taskRepository.save(task);
        return task;
    }

    public Task createAndSaveTaskForUser(User user, GraphTaskSubject subject) {
        Task task = taskFactory.createNotRandomTask(user, subject);
        taskRepository.save(task);
        return task;
    }

    public void deleteTask(Task task) {
        graphService.deleteGraphsOfTask(task);
        taskRepository.delete(task);
    }

    @Transactional
    public void deleteAllUserTasks(User user) {
        graphService.deleteGraphsOfUser(user);
        taskRepository.deleteAllByUser(user);
    }

}

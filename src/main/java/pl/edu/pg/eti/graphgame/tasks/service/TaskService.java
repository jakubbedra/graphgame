package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.tasks.entity.Task;
import pl.edu.pg.eti.graphgame.tasks.factory.TaskFactory;
import pl.edu.pg.eti.graphgame.tasks.repository.TaskRepository;
import pl.edu.pg.eti.graphgame.users.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskFactory taskFactory;

     public Iterable<Task> findAll() {
         return taskRepository.findAll();
     }

    @Autowired
    public TaskService(
            TaskRepository taskRepository,
            TaskFactory taskFactory
    ) {
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
    }

    public Optional<Task> findTask(UUID uuid) {
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

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Transactional
    public void deleteAllUserTasks(User user) {
        taskRepository.deleteAllByUser(user);
    }

}

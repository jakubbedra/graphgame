package pl.edu.pg.eti.graphgame.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pg.eti.graphgame.tasks.entity.TaskSubject;
import pl.edu.pg.eti.graphgame.tasks.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(
            TaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }

    public Optional<TaskSubject> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Optional<TaskSubject> findTaskByName(String name) {
        return taskRepository.findAllByName(name).stream().findAny();
    }

    public List<TaskSubject> findAllTasks() {
        return (List<TaskSubject>) taskRepository.findAll();
    }

    @Transactional
    public void saveTask(TaskSubject taskSubject) {
        taskRepository.save(taskSubject);
    }

    @Transactional
    public void updateTask(TaskSubject taskSubject) {
        taskRepository.save(taskSubject);
    }

    @Transactional
    public void deleteTask(TaskSubject taskSubject) {
        taskRepository.delete(taskSubject);
    }

}

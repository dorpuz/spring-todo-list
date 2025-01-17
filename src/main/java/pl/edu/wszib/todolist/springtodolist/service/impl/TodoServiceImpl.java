package pl.edu.wszib.todolist.springtodolist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wszib.todolist.springtodolist.dao.TodoDao;
import pl.edu.wszib.todolist.springtodolist.dto.TodoDTO;
import pl.edu.wszib.todolist.springtodolist.model.Status;
import pl.edu.wszib.todolist.springtodolist.model.Todo;
import pl.edu.wszib.todolist.springtodolist.service.TodoService;
import pl.edu.wszib.todolist.springtodolist.utils.ConverterComponent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {


    @Autowired
    private TodoDao todoDao;

    @Autowired
    private ConverterComponent converterComponent;

    @Override
    public List<TodoDTO> findAll() {
        return todoDao.findAll()
                .stream()
                .map(todo -> converterComponent.convert(todo)) //.map(converterComponent::convert)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDTO find(Integer id) {
        return todoDao.findById(id)
                .map(converterComponent::convert)
                .orElse(null);
    }

    @Override
    public TodoDTO add(TodoDTO dto) {
        dto.status = Status.NEW.toString();
        Todo todo = converterComponent.convert(dto);
        return converterComponent.convert
                (todoDao.save(todo));
    }

    @Override
    public TodoDTO update(TodoDTO dto) {
        Todo todo = todoDao.findById(dto.id)
                .orElse(new Todo());
        Todo converted = converterComponent.convert(dto);

        todo.setStatus(converted.getStatus());
        todo.setTitle(converted.getTitle());
        todo.setDueDate(converted.getDueDate());

        return converterComponent.convert(todoDao.save(todo));
    }

    @Override
    public TodoDTO remove(Integer id) {
        Optional<Todo> todo = todoDao.findById(id);
        todo.ifPresent(t -> todoDao.delete(t));
        return todo
                .map(converterComponent::convert)
                .orElse(null);
    }

    @Override
    public List<TodoDTO> findUpcoming() {

        return todoDao.findTop5ByStatusIsNotOrderByDueDate(Status.COMPLETED)
                .stream()
                .map(converterComponent::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Integer countStatuses(Status status) {
        return todoDao.countTodoByStatus(status);
    }

    @Override
    public List<TodoDTO> search(Status status) {
        return todoDao.findAllByStatus(status)
                .stream()
                .map(converterComponent::convert)
                .collect(Collectors.toList());
    }
}

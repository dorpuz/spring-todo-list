package pl.edu.wszib.todolist.springtodolist.utils;

import org.springframework.stereotype.Component;
import pl.edu.wszib.todolist.springtodolist.dto.TodoDTO;
import pl.edu.wszib.todolist.springtodolist.model.Status;
import pl.edu.wszib.todolist.springtodolist.model.Todo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class ConverterComponent {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Todo convert(TodoDTO todoDTO){
        Todo todo = new Todo();
        todo.setId(todoDTO.id);
        todo.setTitle(todoDTO.title);
        todo.setStatus(Status.valueOf(todoDTO.status));
        try {
            todo.setDueDate(dateFormat.parse(todoDTO.dueDate));
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
            todo.setDueDate(calendar.getTime());
        }
        return todo;
    }

   public TodoDTO convert(Todo todo){
        TodoDTO dto = new TodoDTO();
        dto.id = todo.getId();
        dto.title = todo.getTitle();
        dto.status = todo.getStatus().toString();
        dto.dueDate = dateFormat.format(todo.getDueDate());
        return dto;
    }

}

package com.example.student.component;

import com.example.student.model.Student;
import com.example.student.repo.StudentRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class StudentEditor extends VerticalLayout implements KeyNotifier {

    private final StudentRepository studentRepository;

    private Student student;

    TextField surname = new TextField("Surname");
    TextField name = new TextField("Name");

    private final Button save = new Button("Save", VaadinIcon.CHECK.create());
    private final Button cancel = new Button("Cancel");
    private final Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private final HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private final Binder<Student> binder = new Binder<>(Student.class);

    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public StudentEditor(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        add(surname, name, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editStudent(student));
        setVisible(false);

    }

    private void delete() {
        studentRepository.delete(student);
        changeHandler.onChange();
    }

    private void save() {
        studentRepository.save(student);
        changeHandler.onChange();
    }

    public void editStudent(Student newStudent) {
        if (newStudent == null) {
            setVisible(false);
            return;
        }
        if (newStudent.getId() != null) {
            student = studentRepository.findById(newStudent.getId()).orElse(newStudent);
        } else {
            student = newStudent;
        }
        binder.setBean(student);
        setVisible(true);
        surname.focus();
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }
}

package com.example.student.view;

import com.example.student.component.StudentEditor;
import com.example.student.model.Student;
import com.example.student.repo.StudentRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;


@Route
public class MainView extends VerticalLayout {
    private final StudentRepository studentRepository;
    private final Grid<Student> grid;

    private final TextField textField = new TextField("", "Type to filer");
    private final Button button = new Button("Add new");

    private final HorizontalLayout toolBar = new HorizontalLayout(textField, button);
    private final StudentEditor editor;

    public MainView(StudentRepository studentRepository, StudentEditor editor) {
        this.studentRepository = studentRepository;
        this.editor = editor;
        this.grid = new Grid<>(Student.class);
        add(toolBar, grid, editor);

        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(e -> showStudent(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editStudent(e.getValue());
        });

        button.addClickListener(e -> editor.editStudent(new Student()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            showStudent(textField.getValue());
        });

        showStudent("");
    }

    private void showStudent(String name) {
        if (name.isEmpty()) {
            grid.setItems(studentRepository.findAll());
        }
        grid.setItems(studentRepository.findByName(name));
    }
}

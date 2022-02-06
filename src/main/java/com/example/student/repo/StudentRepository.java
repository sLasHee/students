package com.example.student.repo;

import com.example.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("from Student e where concat(e.surname, ' ', e.name, ' ') like concat('%', :name, '%')")
    List<Student>findByName(@Param("name") String name);
}

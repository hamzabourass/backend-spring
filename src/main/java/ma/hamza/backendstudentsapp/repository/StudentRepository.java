package ma.hamza.backendstudentsapp.repository;

import ma.hamza.backendstudentsapp.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,String> {
    Student findByCode(String code);

}

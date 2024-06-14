package ma.hamza.backendstudentsapp.repository;


import ma.hamza.backendstudentsapp.entities.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

@DataJpaTest // Used to test jpa repository
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // In memory database for testing
public class StudentsRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;

    @MockBean
    private CommandLineRunner commandLineRunner;

    @BeforeEach
    void setUp() {
        studentRepository.save(Student.builder()
                .id("STU_" + UUID.randomUUID().toString())
                .code("18795")
                .programId("IIR")
                .firstName("Hamza")
                .lastName("Bouras")
                .programId("PROG123")
                .email("hellohmz@gmail.com")
                .build());
        studentRepository.save(Student.builder()
                .id("STU_" + UUID.randomUUID().toString())
                .code("112005")
                .programId("F&A")
                .firstName("Youssef")
                .lastName("Bouras")
                .programId("PROG333")
                .email("helloyssf@gmail.com")
                .build());
        studentRepository.save(Student.builder()
                .id("STU_" + UUID.randomUUID().toString())
                .code("75980")
                .programId("MIAGE")
                .firstName("Oussama")
                .lastName("Bouras")
                .email("helloossm@gmail.com")
                .build());
        studentRepository.save(Student.builder()
                .id("STU_" + UUID.randomUUID().toString())
                .code("88898")
                .programId("MIAGE")
                .firstName("Majdoub")
                .lastName("Bouras")
                .email("hellomjd@gmail.com")
                .build());
    }

    @Test
    public void testSaveStudentToRepository() {

        // Arrange
        Student student = Student.builder()
                .id("STU_" + UUID.randomUUID().toString())
                .code("112255")
                .programId("IIR")
                .firstName("Malika")
                .lastName("allali")
                .email("hellomlk@gmail.com")
                .build();
        // Act
        Student savedStudent = studentRepository.save(student);

        // Assert
        Assertions.assertNotNull(savedStudent);
        Assertions.assertEquals(student.getId(), savedStudent.getId());

    }

    @Test
    public void TestFindAllStudentsFromRepository() {

        // Act
        List<Student> students;
        students = studentRepository.findAll();

        // Assert
        Assertions.assertNotNull(students);
        Assertions.assertEquals(4,students.size());

    }

    @Test
    public void TestFindStudentByIdFromRepository() {

        // Arrange
        Student student = Student.builder()
                .id("STU_" + UUID.randomUUID().toString())
                .code("670955")
                .programId("IIR")
                .firstName("Oussama")
                .lastName("Bouras")
                .programId("PROG123")
                .email("hello@gmail.com")
                .build();

        // Act
        studentRepository.save(student);
        Student student2 = studentRepository.findById(student.getId()).get();

        // Assert
        Assertions.assertNotNull(student2);
        Assertions.assertEquals( student2.getId() , student.getId());

    }

    @Test
    public void TestFindStudentByCodeFromRepository() {

        Student student = studentRepository.findByCode("18795");

        Assertions.assertNotNull(student);
        Assertions.assertEquals("18795", student.getCode());
    }

    @Test
    public void TestFindStudentByProgramIdFromRepository() {

        List<Student> students = studentRepository.findByProgramId("MIAGE");
        Assertions.assertNotNull(students);
        Assertions.assertEquals(2, students.size());
    }

    @Test
    public void TestDeleteStudentFromRepository() {

        studentRepository.deleteByCode("18795");
        // Assert
        Student student = studentRepository.findByCode("18795");
        Assertions.assertNull(student);
    }

    @Test
    public void TestUpdateStudentFromRepository() {

        Student student = studentRepository.findByCode("112005");
        student.setFirstName("Youssofita");

        studentRepository.save(student);
        Student updatedStudent = studentRepository.findById(student.getId()).get();

        Assertions.assertNotNull(updatedStudent);
        Assertions.assertEquals("Youssofita", updatedStudent.getFirstName());
    }

}

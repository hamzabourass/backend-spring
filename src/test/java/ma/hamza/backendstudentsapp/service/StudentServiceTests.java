package ma.hamza.backendstudentsapp.service;


import ma.hamza.backendstudentsapp.repository.StudentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    @Mock
    StudentRepository studentRepository;
}

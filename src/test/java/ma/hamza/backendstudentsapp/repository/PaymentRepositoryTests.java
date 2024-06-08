package ma.hamza.backendstudentsapp.repository;

import jakarta.transaction.Transactional;
import ma.hamza.backendstudentsapp.entities.Payment;
import ma.hamza.backendstudentsapp.entities.Student;
import ma.hamza.backendstudentsapp.enums.PaymentStatus;
import ma.hamza.backendstudentsapp.enums.PaymentType;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PaymentRepositoryTests {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @MockBean
    private CommandLineRunner commandLineRunner;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        paymentRepository.deleteAll();

        Student student1 = studentRepository.save(Student.builder().id("STU_1").code("112255").programId("IIR").firstName("Hamza").build());
        Student student2 = studentRepository.save(Student.builder().id("STU_2").code("546008").programId("MIAGE").firstName("Youssef").build());
        Student student3 = studentRepository.save(Student.builder().id("STU_3").code("67690").programId("GC").firstName("Oussama").build());
        Student student4 = studentRepository.save(Student.builder().id("STU_4").code("67694").programId("GC").firstName("Mohamed").build());

        savePaymentsForStudent(student1);
        savePaymentsForStudent(student2);
        savePaymentsForStudent(student3);
        savePaymentsForStudent(student4);
    }

    private void savePaymentsForStudent(Student student) {
        for (int i = 0; i < 10; i++) {
            Payment payment = Payment.builder()
                    .amount(10000 + (int) (Math.random() * 10000))
                    .date(LocalDate.now())
                    .type(PaymentType.CASH)
                    .status(PaymentStatus.CREATED)
                    .file(UUID.randomUUID().toString() + ".pdf")
                    .student(student)
                    .build();
            paymentRepository.save(payment);
        }
    }

    @Test
    @Transactional
    public void findPaymentById() {
        Payment payment = paymentRepository.findAll().get(0); // Get any payment ID from saved payments
        Long paymentId = payment.getId();
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        Assertions.assertTrue(paymentOpt.isPresent(), "Payment should exist");
        Assertions.assertEquals(paymentId, paymentOpt.get().getId());
    }

    @Test
    @Transactional
    public void findPaymentByStudentCode(){
        List<Payment> payments = paymentRepository.findByStudentCode("112255");
        Assertions.assertNotNull(payments);
        Assertions.assertEquals(payments.size(),10);
    }

    @Test
    @Transactional

    public void findPaymentByStatus(){
        List<Payment> payments = paymentRepository.findByStatus(PaymentStatus.CREATED);
        Assertions.assertNotNull(payments);
        Assertions.assertEquals(payments.size(),40);
    }

    @Test
    @Transactional

    public void findPaymentByType(){
        List<Payment> payments = paymentRepository.findByType(PaymentType.CASH);
        Assertions.assertNotNull(payments);
        Assertions.assertEquals(payments.size(),40);

    }


}




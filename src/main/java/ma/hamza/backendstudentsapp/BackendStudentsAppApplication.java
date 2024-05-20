package ma.hamza.backendstudentsapp;

import ma.hamza.backendstudentsapp.entities.Payment;
import ma.hamza.backendstudentsapp.entities.Student;
import ma.hamza.backendstudentsapp.enums.PaymentStatus;
import ma.hamza.backendstudentsapp.enums.PaymentType;
import ma.hamza.backendstudentsapp.repository.PaymentRepository;
import ma.hamza.backendstudentsapp.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class BackendStudentsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendStudentsAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, PaymentRepository paymentRepository){
        return args -> {
            studentRepository.save(Student.builder().id("STU_" + UUID.randomUUID().toString()).code("112255").programId("IIR").firstName("Hamza").build());
            studentRepository.save(Student.builder().id("STU_" + UUID.randomUUID().toString()).code("546008").programId("MIAGE").firstName("Youssef").build());
            studentRepository.save(Student.builder().id("STU_" + UUID.randomUUID().toString()).code("67690").programId("GC").firstName("Oussama").build());
            studentRepository.save(Student.builder().id("STU_" + UUID.randomUUID().toString()).code("67694").programId("GC").firstName("Mohamed").build());

            PaymentType[] paymentTypes = PaymentType.values();
            Random random = new Random();
            studentRepository.findAll().forEach(student -> {

                int indexType = random.nextInt(paymentTypes.length);

                for(int i = 0 ; i < 10 ; i++){
                    Payment payment = Payment.builder()
                            .amount(10000 + (int)(Math.random()*10000))
                            .date(LocalDate.now())
                            .type(paymentTypes[indexType])
                            .status(PaymentStatus.CREATED)
                            .file(UUID.randomUUID().toString() + ".pdf")
                            .student(student)
                            .build();

                    paymentRepository.save(payment);
                }
            });
        };
    }

}

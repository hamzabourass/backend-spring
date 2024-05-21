package ma.hamza.backendstudentsapp.service;

import lombok.AllArgsConstructor;
import ma.hamza.backendstudentsapp.dtos.PaymentDTO;
import ma.hamza.backendstudentsapp.entities.Payment;
import ma.hamza.backendstudentsapp.entities.Student;
import ma.hamza.backendstudentsapp.enums.PaymentStatus;
import ma.hamza.backendstudentsapp.enums.PaymentType;
import ma.hamza.backendstudentsapp.repository.PaymentRepository;
import ma.hamza.backendstudentsapp.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service @Transactional
@AllArgsConstructor
public class PaymentService {
    private PaymentRepository paymentRepository;
    private StudentRepository studentRepository;

    public Payment savePayment(MultipartFile file,
                               PaymentDTO paymentDTO) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"),"students-app-files","payments");
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        String fileId = "payment-" + UUID.randomUUID().toString();
        Path filePath = Paths.get(System.getProperty("user.home"),"students-app-files","payments",fileId+".pdf");
        Files.copy(file.getInputStream(),filePath);

        Student student = studentRepository.findByCode(paymentDTO.getStudentCode());
        Payment payment = Payment.builder()
                .type(paymentDTO.getType())
                .amount(paymentDTO.getAmount())
                .student(student)
                .status(PaymentStatus.CREATED)
                .file(filePath.toUri().toString())
                .build();

        return paymentRepository.save(payment);

    }

    public byte[] loadPaymentFile(Long paymentId) throws IOException {
        Payment payment = paymentRepository.findById(paymentId).get();
        String filePath = payment.getFile();
        return Files.readAllBytes(Path.of(URI.create(filePath)));
    }
}

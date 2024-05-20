package ma.hamza.backendstudentsapp.web;

import lombok.AllArgsConstructor;
import ma.hamza.backendstudentsapp.entities.Payment;
import ma.hamza.backendstudentsapp.entities.Student;
import ma.hamza.backendstudentsapp.enums.PaymentStatus;
import ma.hamza.backendstudentsapp.enums.PaymentType;
import ma.hamza.backendstudentsapp.repository.PaymentRepository;
import ma.hamza.backendstudentsapp.repository.StudentRepository;
import ma.hamza.backendstudentsapp.service.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class StudentRestController {

    private StudentRepository studentRepository;
    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @GetMapping("/payments")
    public List<Payment> allPayments(){
        return paymentRepository.findAll();
    }

    @GetMapping("/payments/{id}")
    public Payment findPaymentById(@PathVariable Long id){
        return paymentRepository.findById(id).get();
    }

    @GetMapping("/students")
    public List<Student> allStudents(){
        return studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    public Student findStudentById(@PathVariable String id){
        return studentRepository.findById(id).get();
    }

    @GetMapping("/studentsByProgramId")
    public List<Student> listStudentByProgramId(@RequestParam String programId){
        return studentRepository.findByProgramId(programId);
    }
    @GetMapping("/students/{code}/payments")
    public List<Payment> findPaymentByStudentCode(@PathVariable String code){
        return paymentRepository.findByStudentCode(code);
    }

    @PostMapping(value = "/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment savePayments(@RequestParam MultipartFile file,
                                LocalDate date,
                                double amount,
                                PaymentType paymentType,
                                String studentCode) throws IOException {

        return paymentService.savePayment(file,date,amount,paymentType,studentCode);

    }

    @GetMapping(value = "paymentFile/{paymentId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getPaymentFile(@PathVariable Long paymentId) throws IOException {
       return paymentService.loadPaymentFile(paymentId);
    }

    @GetMapping("/payments/byStatus")
    public List<Payment> paymentsByStatus(@RequestParam PaymentStatus status){
        return paymentRepository.findByStatus(status);
    }
    @GetMapping("/payments/byType")
    public List<Payment> paymentsByType(@RequestParam PaymentType type){
        return paymentRepository.findByType(type);
    }

    @PutMapping("/payments/updateStatus/{paymentId}")
    public Payment updatePaymentStatus(@RequestParam PaymentStatus paymentStatus,@PathVariable Long paymentId){

        Payment payment = paymentRepository.findById(paymentId).get();
        payment.setStatus(paymentStatus);
        return paymentRepository.save(payment);

    }


}

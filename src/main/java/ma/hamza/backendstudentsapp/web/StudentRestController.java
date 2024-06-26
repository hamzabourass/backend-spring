package ma.hamza.backendstudentsapp.web;

import lombok.AllArgsConstructor;
import ma.hamza.backendstudentsapp.dtos.PaymentDTO;
import ma.hamza.backendstudentsapp.entities.Payment;
import ma.hamza.backendstudentsapp.entities.Student;
import ma.hamza.backendstudentsapp.enums.PaymentStatus;
import ma.hamza.backendstudentsapp.enums.PaymentType;
import ma.hamza.backendstudentsapp.repository.PaymentRepository;
import ma.hamza.backendstudentsapp.repository.StudentRepository;
import ma.hamza.backendstudentsapp.service.PaymentService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from this origin

public class StudentRestController implements HealthIndicator {

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
    public Payment savePayments(@RequestParam("file") MultipartFile file,
                                PaymentDTO paymentDTO) throws IOException {

        return paymentService.savePayment(file,paymentDTO);

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

    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }
    @Override
    public Health health() {

        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}

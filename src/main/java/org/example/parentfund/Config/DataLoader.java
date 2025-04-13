package org.example.parentfund.Config;

import org.example.parentfund.Entity.Parent;
import org.example.parentfund.Entity.Payment;
import org.example.parentfund.Entity.Student;
import org.example.parentfund.Enum.ParentStatus;
import org.example.parentfund.Enum.PaymentStatus;
import org.example.parentfund.Enum.StudentStatus;
import org.example.parentfund.Repository.ParentRepository;
import org.example.parentfund.Repository.PaymentRepository;
import org.example.parentfund.Repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {



    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PaymentRepository paymentRepository;



    public DataLoader(StudentRepository studentRepository, ParentRepository parentRepository, PaymentRepository paymentRepository) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void run(String... args) throws Exception {

            System.out.println("DataLoader is running!");

        Parent parent1 = Parent.builder()
                .firstName("Collins")
                .lastName("Okafor")
                .email("collinsdaberechi20@gmail.com")
                .phoneNumber("08168889426")
                .balance(new BigDecimal("1000.00"))
                .parentStatus(ParentStatus.ACTIVE)
                .build();

        Parent parent2 = Parent.builder()
                .firstName("Doris")
                .lastName("Okafor")
                .email("doris@gmail.com")
                .phoneNumber("08168889426")
                .balance(new BigDecimal("1500.00"))
                .parentStatus(ParentStatus.ACTIVE)
                .build();

        parentRepository.saveAll(Arrays.asList(parent1, parent2));

        Student student1 = Student.builder()
                .firstName("Lyra")
                .lastName("Okafor")
                .studentClass("10th Grade")
                .isShared(true)
                .balance(new BigDecimal("500.00"))
                .studentStatus(StudentStatus.ENROLLED)
                .parents(Arrays.asList(parent1, parent2))
                .build();

        Student student2 = Student.builder()
                .firstName("Diva")
                .lastName("Okafor")
                .studentClass("11th Grade")
                .isShared(false)
                .balance(new BigDecimal("300.00"))
                .studentStatus(StudentStatus.ENROLLED)
                .parents(Arrays.asList(parent1))
                .build();

        studentRepository.saveAll(Arrays.asList(student1, student2));

        Payment payment1 = Payment.builder()
                .originalAmount(new BigDecimal("200.00"))
                .adjustedAmount(new BigDecimal("180.00"))
                .feeRate(0.10)
                .paymentDescription("Payment for Alice's tuition")
                .paymentDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.PENDING)
                .parent(parent1)
                .student(student1)
                .build();

        Payment payment2 = Payment.builder()
                .originalAmount(new BigDecimal("150.00"))
                .adjustedAmount(new BigDecimal("130.00"))
                .feeRate(0.12)
                .paymentDescription("Payment for Bob's tuition")
                .paymentDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.APPROVED)
                .parent(parent1)
                .student(student2)
                .build();

        paymentRepository.saveAll(Arrays.asList(payment1, payment2));
        System.out.println("Inserted payment data");

    }
}

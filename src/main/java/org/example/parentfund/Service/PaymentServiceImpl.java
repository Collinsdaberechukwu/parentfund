package org.example.parentfund.Service;

import lombok.RequiredArgsConstructor;
import org.example.parentfund.DTOS.PaymentDTO;
import org.example.parentfund.Entity.Parent;
import org.example.parentfund.Entity.Payment;
import org.example.parentfund.Entity.Student;
import org.example.parentfund.Enum.ParentStatus;
import org.example.parentfund.Enum.PaymentStatus;
import org.example.parentfund.Exception.ParentNotFoundException;
import org.example.parentfund.Exception.StudentNotFoundException;
import org.example.parentfund.Repository.ParentRepository;
import org.example.parentfund.Repository.PaymentRepository;
import org.example.parentfund.Repository.StudentRepository;
import org.example.parentfund.Exception.UnauthorizedAccessException;
import org.example.parentfund.utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final JwtUtil jwtUtil;



    @Transactional
    @Override
    public void processPayment(PaymentDTO paymentRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        boolean hasAdminRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!hasAdminRole) {
            throw new UnauthorizedAccessException("User does not have the required role.");
        }

        Parent parent = parentRepository.findById(paymentRequest.getParentId())
                .orElseThrow(() -> new ParentNotFoundException("Parent not found"));

        Student student = studentRepository.findById(paymentRequest.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        BigDecimal paymentAmount = paymentRequest.getPaidAmount();
        double dynamicPaymentRate = 0.05;
        BigDecimal adjustedPaymentAmount = paymentAmount.multiply(BigDecimal.valueOf(1 + dynamicPaymentRate));

        student.setBalance(student.getBalance().add(adjustedPaymentAmount));
        studentRepository.save(student);

        List<Parent> parents = student.getParents();
        if (parents == null || parents.isEmpty()) {
            throw new IllegalStateException("Student must have at least one parent associated.");
        }

        BigDecimal sharePerParent = adjustedPaymentAmount.divide(BigDecimal.valueOf(parents.size()), RoundingMode.HALF_UP);
        for (Parent parent1 : parents) {
            parent1.setParentStatus(ParentStatus.ACTIVE);

            parent1.setBalance(parent1.getBalance().subtract(sharePerParent));
            parentRepository.save(parent1);
        }

        Payment newPayment = Payment.builder()
                .originalAmount(paymentAmount)
                .adjustedAmount(adjustedPaymentAmount)
                .paymentDate(LocalDateTime.ofEpochSecond(Instant.now().getEpochSecond(), 0, ZoneOffset.UTC))
                .paymentStatus(PaymentStatus.APPROVED)
                .paymentDescription(String.format("Processed payment for student %s %s", student.getFirstName(), student.getLastName()))
                .feeRate(dynamicPaymentRate)
                .parent(parent)
                .student(student)
                .build();

        paymentRepository.save(newPayment);
    }


}

package org.example.parentfund;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.example.parentfund.DTOS.PaymentDTO;
import org.example.parentfund.Entity.Parent;
import org.example.parentfund.Entity.Payment;
import org.example.parentfund.Entity.Student;
import org.example.parentfund.Exception.ParentNotFoundException;
import org.example.parentfund.Exception.StudentNotFoundException;
import org.example.parentfund.Exception.UnauthorizedAccessException;
import org.example.parentfund.Repository.ParentRepository;
import org.example.parentfund.Repository.PaymentRepository;
import org.example.parentfund.Repository.StudentRepository;
import org.example.parentfund.Service.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class PaymentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Parent parentA;
    private Parent parentB;
    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        parentA = new Parent();
        parentA.setId(1L);
        parentA.setBalance(BigDecimal.valueOf(1000));

        parentB = new Parent();
        parentB.setId(2L);
        parentB.setBalance(BigDecimal.valueOf(1000));

        student = new Student();
        student.setId(1L);
        student.setBalance(BigDecimal.valueOf(500));
        student.setParents(Arrays.asList(parentA, parentB));
    }

    @Test
    void testProcessPaymentForSharedChild() {
        PaymentDTO paymentDTO = new PaymentDTO(1L, 1L, BigDecimal.valueOf(200)); // Parent A paying for shared student
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parentA));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenReturn(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        paymentService.processPayment(paymentDTO);

        verify(studentRepository, times(1)).save(student);
        verify(parentRepository, times(2)).save(any(Parent.class)); // Both Parent A and Parent B should be updated
        verify(paymentRepository, times(1)).save(any(Payment.class)); // Payment record saved

        assertEquals(BigDecimal.valueOf(900), parentA.getBalance());
        assertEquals(BigDecimal.valueOf(900), parentB.getBalance());

        assertEquals(BigDecimal.valueOf(700), student.getBalance());
    }

    @Test
    void testUnauthorizedAccess() {

        PaymentDTO paymentDTO = new PaymentDTO(1L, 1L, BigDecimal.valueOf(200));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(parentRepository.findById(1L)).thenReturn(Optional.of(parentA));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenReturn(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class, () -> {
            paymentService.processPayment(paymentDTO);
        });

        assertEquals("User does not have the required role.", exception.getMessage());
    }

    @Test
    void testParentNotFound() {
        PaymentDTO paymentDTO = new PaymentDTO(1L, 1L, BigDecimal.valueOf(200));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(parentRepository.findById(1L)).thenReturn(Optional.empty());  // Parent not found

        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenReturn(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ParentNotFoundException exception = assertThrows(ParentNotFoundException.class, () -> {
            paymentService.processPayment(paymentDTO);
        });

        assertEquals("Parent not found", exception.getMessage());
    }

    @Test
    void testStudentNotFound() {
        PaymentDTO paymentDTO = new PaymentDTO(1L, 1L, BigDecimal.valueOf(200));
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());  // Student not found

        Authentication authentication = mock(Authentication.class);
        when(authentication.getAuthorities()).thenReturn(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            paymentService.processPayment(paymentDTO);
        });

        assertEquals("Student not found", exception.getMessage());
    }
}

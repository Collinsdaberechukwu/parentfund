package org.example.parentfund.Service;

import org.example.parentfund.DTOS.PaymentDTO;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {
    @Transactional
    void processPayment (PaymentDTO paymentRequest);
}

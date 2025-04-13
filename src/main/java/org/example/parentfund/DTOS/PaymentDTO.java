package org.example.parentfund.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentDTO {

    private Long parentId;
    private Long studentId;
    private BigDecimal paidAmount;


//    public Long getParentId() {
//        return parentId;
//    }
//
//    public Long getStudentId() {
//        return studentId;
//    }
//
//    public BigDecimal getPaidAmount() {
//        return paidAmount;
//    }
}

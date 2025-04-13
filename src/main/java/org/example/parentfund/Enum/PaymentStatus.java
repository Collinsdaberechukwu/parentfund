package org.example.parentfund.Enum;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    PENDING("Pending Transaction"),
    APPROVED("Approved Transaction"),
    REJECTED("Rejected Transaction"),
    FAILED("Failed Transaction");


    private final String transactionStatus;

    PaymentStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}

package org.example.parentfund.Enum;

import lombok.Getter;

@Getter
public enum StudentStatus {

    ENROLLED("Enrolled"),
    GRADUATED("Graduated"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended");

    private final String studentStatus;

    StudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }
}



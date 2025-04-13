package org.example.parentfund.Enum;

import lombok.Getter;

@Getter
public enum ParentStatus {

    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    INACTIVE("Inactive"),
    PENDING("Pending");

    private final String parentsStatus;

    ParentStatus(String parentsStatus) {
        this.parentsStatus = parentsStatus;
    }
}

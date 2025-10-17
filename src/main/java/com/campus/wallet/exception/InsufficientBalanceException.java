package com.campus.wallet.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String admissionNo) {
        super("Insufficient balance for student: " + admissionNo);
    }
}

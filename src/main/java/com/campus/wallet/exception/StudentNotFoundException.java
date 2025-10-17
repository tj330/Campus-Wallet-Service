package com.campus.wallet.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String admissionNo) {
        super("Student not found with admissionNo: " + admissionNo);
    }
}

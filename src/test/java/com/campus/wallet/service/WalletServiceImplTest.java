package com.campus.wallet.service;

import com.campus.wallet.dto.TransactionDTO;
import com.campus.wallet.entity.Student;
import com.campus.wallet.entity.Store;
import com.campus.wallet.entity.Transaction;
import com.campus.wallet.entity.Transaction.TxnType;
import com.campus.wallet.exception.InsufficientBalanceException;
import com.campus.wallet.exception.StudentNotFoundException;
import com.campus.wallet.exception.StoreNotFoundException;
import com.campus.wallet.repository.StudentRepository;
import com.campus.wallet.repository.StoreRepository;
import com.campus.wallet.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .admissionNo("ST001")
                .name("John Doe")
                .department("CS")
                .email("john@campus.edu")
                .balance(1000.0)
                .build();
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        walletService.deposit("ST001", 500.0);

        assertEquals(1500.0, student.getBalance());
        verify(studentRepository).save(student);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void deposit_shouldThrowWhenStudentNotFound() {
        when(studentRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> 
            walletService.deposit("INVALID", 500.0));
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        walletService.withdraw("ST001", 300.0);

        assertEquals(700.0, student.getBalance());
        verify(studentRepository).save(student);
    }

    @Test
    void withdraw_shouldThrowWhenInsufficientBalance() {
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));

        assertThrows(InsufficientBalanceException.class, () -> 
            walletService.withdraw("ST001", 2000.0));
    }

    @Test
    void withdraw_shouldThrowWhenStudentNotFound() {
        when(studentRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> 
            walletService.withdraw("INVALID", 100.0));
    }

    @Test
    void pay_shouldDecreaseBalanceAndRecordTransaction() {
        Store store = Store.builder().storeId(1).storeName("Campus Store").category("Food").build();
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        walletService.pay("ST001", 1, 200.0);

        assertEquals(800.0, student.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void pay_shouldThrowWhenInsufficientBalance() {
        Store store = Store.builder().storeId(1).storeName("Campus Store").category("Food").build();
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));

        assertThrows(InsufficientBalanceException.class, () -> 
            walletService.pay("ST001", 1, 2000.0));
    }

    @Test
    void pay_shouldThrowWhenStoreNotFound() {
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));
        when(storeRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> 
            walletService.pay("ST001", 999, 100.0));
    }

    @Test
    void checkBalance_shouldReturnBalance() {
        when(studentRepository.findById("ST001")).thenReturn(Optional.of(student));

        Double balance = walletService.checkBalance("ST001");

        assertEquals(1000.0, balance);
    }

    @Test
    void checkBalance_shouldThrowWhenStudentNotFound() {
        when(studentRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> 
            walletService.checkBalance("INVALID"));
    }

    @Test
    void transactionHistory_shouldReturnTransactions() {
        Transaction txn = Transaction.builder()
                .txnId(1L)
                .student(student)
                .txnType(TxnType.DEPOSIT)
                .amount(500.0)
                .build();
        when(transactionRepository.findByStudentAdmissionNo("ST001"))
                .thenReturn(List.of(txn));

        List<TransactionDTO> history = walletService.transactionHistory("ST001");

        assertEquals(1, history.size());
        assertEquals("DEPOSIT", history.get(0).getTxnType());
    }
}
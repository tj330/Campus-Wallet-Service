package com.campus.wallet.service;

import com.campus.wallet.dto.TransactionDTO;
import com.campus.wallet.entity.Student;
import com.campus.wallet.entity.Store;
import com.campus.wallet.entity.Transaction;
import com.campus.wallet.entity.Transaction.TxnType;
import com.campus.wallet.exception.StudentNotFoundException;
import com.campus.wallet.exception.InsufficientBalanceException;
import com.campus.wallet.exception.StoreNotFoundException;
import com.campus.wallet.repository.StudentRepository;
import com.campus.wallet.repository.StoreRepository;
import com.campus.wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final StudentRepository studentRepository;
    private final StoreRepository storeRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void deposit(String admissionNo, Double amount) {
        Student student = studentRepository.findById(admissionNo)
                .orElseThrow(() -> new StudentNotFoundException(admissionNo));
        student.setBalance(student.getBalance() + amount);
        studentRepository.save(student);
        Transaction txn = Transaction.builder()
                .student(student)
                .txnType(TxnType.DEPOSIT)
                .amount(amount)
                .txnTime(LocalDateTime.now())
                .build();
        transactionRepository.save(txn);
    }

    @Transactional
    public void withdraw(String admissionNo, Double amount) {
        Student student = studentRepository.findById(admissionNo)
                .orElseThrow(() -> new StudentNotFoundException(admissionNo));
        if (student.getBalance() < amount) {
            throw new InsufficientBalanceException(admissionNo);
        }
        student.setBalance(student.getBalance() - amount);
        studentRepository.save(student);
        Transaction txn = Transaction.builder()
                .student(student)
                .txnType(TxnType.WITHDRAW)
                .amount(amount)
                .txnTime(LocalDateTime.now())
                .build();
        transactionRepository.save(txn);
    }

    @Transactional
    public void pay(String admissionNo, Integer storeId, Double amount) {
        Student student = studentRepository.findById(admissionNo)
                .orElseThrow(() -> new StudentNotFoundException(admissionNo));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
        if (student.getBalance() < amount) {
            throw new InsufficientBalanceException(admissionNo);
        }
        student.setBalance(student.getBalance() - amount);
        studentRepository.save(student);
        Transaction txn = Transaction.builder()
                .student(student)
                .store(store)
                .txnType(TxnType.PAYMENT)
                .amount(amount)
                .txnTime(LocalDateTime.now())
                .build();
        transactionRepository.save(txn);
    }

    public Double checkBalance(String admissionNo) {
        Student student = studentRepository.findById(admissionNo)
                .orElseThrow(() -> new StudentNotFoundException(admissionNo));
        return student.getBalance();
    }

    public List<TransactionDTO> transactionHistory(String admissionNo) {
        List<Transaction> txns = transactionRepository.findByStudentAdmissionNo(admissionNo);
        return txns.stream().map(txn -> TransactionDTO.builder()
                .txnId(txn.getTxnId())
                .admissionNo(txn.getStudent().getAdmissionNo())
                .storeId(txn.getStore() != null ? txn.getStore().getStoreId() : null)
                .txnType(txn.getTxnType().name())
                .amount(txn.getAmount())
                .txnTime(txn.getTxnTime())
                .build()).collect(Collectors.toList());
    }
}

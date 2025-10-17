package com.campus.wallet.service;

import com.campus.wallet.dto.TransactionDTO;
import java.util.List;

public interface WalletService {
    void deposit(String admissionNo, Double amount);
    void withdraw(String admissionNo, Double amount);
    void pay(String admissionNo, Integer storeId, Double amount);
    Double checkBalance(String admissionNo);
    List<TransactionDTO> transactionHistory(String admissionNo);
}

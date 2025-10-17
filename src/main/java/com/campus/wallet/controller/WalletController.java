package com.campus.wallet.controller;

import com.campus.wallet.dto.TransactionDTO;
import com.campus.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestParam String admissionNo, @RequestParam Double amount) {
        walletService.deposit(admissionNo, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestParam String admissionNo, @RequestParam Double amount) {
        walletService.withdraw(admissionNo, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pay")
    public ResponseEntity<Void> pay(@RequestParam String admissionNo, @RequestParam Integer storeId, @RequestParam Double amount) {
        walletService.pay(admissionNo, storeId, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{admissionNo}")
    public ResponseEntity<Double> balance(@PathVariable String admissionNo) {
        return ResponseEntity.ok(walletService.checkBalance(admissionNo));
    }

    @GetMapping("/history/{admissionNo}")
    public ResponseEntity<List<TransactionDTO>> history(@PathVariable String admissionNo) {
        return ResponseEntity.ok(walletService.transactionHistory(admissionNo));
    }
}

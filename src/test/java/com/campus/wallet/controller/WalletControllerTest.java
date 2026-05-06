package com.campus.wallet.controller;

import com.campus.wallet.dto.TransactionDTO;
import com.campus.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
@Import(com.campus.wallet.config.SecurityConfig.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    @WithMockUser(roles = "STUDENT")
    void deposit_shouldReturnOk() throws Exception {
        doNothing().when(walletService).deposit("ST001", 500.0);

        mockMvc.perform(post("/wallet/deposit")
                .param("admissionNo", "ST001")
                .param("amount", "500.0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void withdraw_shouldReturnOk() throws Exception {
        doNothing().when(walletService).withdraw("ST001", 100.0);

        mockMvc.perform(post("/wallet/withdraw")
                .param("admissionNo", "ST001")
                .param("amount", "100.0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void pay_shouldReturnOk() throws Exception {
        doNothing().when(walletService).pay("ST001", 1, 50.0);

        mockMvc.perform(post("/wallet/pay")
                .param("admissionNo", "ST001")
                .param("storeId", "1")
                .param("amount", "50.0"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void balance_shouldReturnBalance() throws Exception {
        when(walletService.checkBalance("ST001")).thenReturn(1000.0);

        mockMvc.perform(get("/wallet/balance/ST001"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.0"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void history_shouldReturnTransactionList() throws Exception {
        TransactionDTO txn = TransactionDTO.builder()
                .txnId(1L)
                .admissionNo("ST001")
                .txnType("DEPOSIT")
                .amount(500.0)
                .txnTime(LocalDateTime.now())
                .build();
        when(walletService.transactionHistory("ST001")).thenReturn(List.of(txn));

        mockMvc.perform(get("/wallet/history/ST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].txnType").value("DEPOSIT"));
    }

    @Test
    void balance_shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/wallet/balance/ST001"))
                .andExpect(status().isUnauthorized());
    }
}
package com.fintech.service;

import com.fintech.domain.Account;
import com.fintech.domain.Transfer;

import java.math.BigDecimal;

public interface TransactionService {

    Account deposit(String destinationAccountId, BigDecimal amount);

    void transfer(Transfer transfer);

}

package com.fintech.service;

import com.fintech.domain.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account addAccount(BigDecimal amount);

    Account getAccount(String id);

    List<Account> getAccounts();

    Account updateAccount(Account account);

    void updateAccounts(List<Account> accounts);
}

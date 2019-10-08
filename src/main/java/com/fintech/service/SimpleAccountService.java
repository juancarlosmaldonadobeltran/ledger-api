package com.fintech.service;

import com.fintech.domain.Account;
import com.fintech.exception.AccountNotFoundException;
import com.fintech.repository.AccountRepository;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class SimpleAccountService implements AccountService {

    private AccountRepository repository;

    @Inject
    public SimpleAccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account addAccount(BigDecimal balance) {
        UUID uuid = UUID.randomUUID();
        return repository.save(Account.builder().id(uuid.toString()).balance(balance).build());
    }

    @Override
    public Account getAccount(String id) {
        return repository.findOne(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public List<Account> getAccounts() {
        return repository.findAll();
    }

    @Override
    public Account updateAccount(Account account) {
        return repository.save(account);
    }

    @Override
    public void updateAccounts(List<Account> accounts) {
        repository.save(accounts);
    }

}

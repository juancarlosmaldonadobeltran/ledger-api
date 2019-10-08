package com.fintech.repository;

import com.fintech.domain.Account;

import java.util.*;

public class AccountRepository {

    private Map<String, Account> accounts;

    public AccountRepository() {
        this.accounts = new HashMap<>();
    }

    public Account save(Account account) {
        this.accounts.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findOne(String id) {
        return Optional.ofNullable(this.accounts.get(id));
    }

    public List<Account> findAll() {
        return new ArrayList<>(this.accounts.values());
    }

    public void removeAll() {
        this.accounts = new HashMap<>();
    }

    public void save(List<Account> accounts) {
        accounts.forEach(account -> this.accounts.put(account.getId(), account));
    }

}

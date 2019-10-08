package com.fintech.service;

import com.fintech.domain.Account;
import com.fintech.domain.Transfer;
import com.fintech.exception.InsufficientFoundsException;
import com.google.inject.Inject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleTransactionService implements TransactionService {

    private AccountService accountService;

    @Inject
    public SimpleTransactionService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Account deposit(String accountId, BigDecimal amount) {

        Account account = accountService.getAccount(accountId);

        synchronized (account.getId()) {
            account.setBalance(account.getBalance().add(amount));
            return accountService.updateAccount(account);
        }
    }

    @Override
    public void transfer(Transfer transfer) {

        // Always locks the accounts in the same order to avoid dead lock.
        List<String> sortedAccountIds = Stream.of(transfer.getSource(), transfer.getDestination())
                .sorted()
                .collect(Collectors.toList());

        synchronized (sortedAccountIds.get(0)) {
            synchronized (sortedAccountIds.get(1)) {

                Account source = accountService.getAccount(transfer.getSource());
                Account destination = accountService.getAccount(transfer.getDestination());
                if (source.getBalance().compareTo(transfer.getAmount()) < 0) {
                    throw new InsufficientFoundsException();
                }
                source.setBalance(source.getBalance().subtract(transfer.getAmount()));
                destination.setBalance(destination.getBalance().add(transfer.getAmount()));
                this.accountService.updateAccounts(Arrays.asList(source,destination));
            }
        }
    }

}

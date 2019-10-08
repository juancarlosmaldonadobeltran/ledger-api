package com.fintech.service;

import com.fintech.domain.Account;
import com.fintech.exception.AccountNotFoundException;
import com.fintech.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class SimpleAccountServiceTest {

    private AccountService service;

    @Mock
    private AccountRepository repository;

    @Captor
    ArgumentCaptor<Account> accountCaptor;

    @Captor
    ArgumentCaptor<String> accountIdCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.service = new SimpleAccountService(repository);

    }

    @Test
    public void addAccount() {
        // given
        BigDecimal expectedBalance = BigDecimal.ONE;
        // when
        this.service.addAccount(expectedBalance);
        // then
        verify(repository, times(1)).save(accountCaptor.capture());
        assertEquals(expectedBalance, accountCaptor.getValue().getBalance());
        assertNotNull(accountCaptor.getValue().getId());

    }

    @Test(expected = Exception.class)
    public void addAccountRepositoryFails() {
        // given
        BigDecimal expectedBalance = BigDecimal.ONE;
        // when
        when(repository.save(any(Account.class))).thenThrow(new Exception());
        this.service.addAccount(expectedBalance);
        // then
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    public void getAccount() {
        // given
        String expectedAccountId = "foo";
        // when
        Account account = Account.builder().id(expectedAccountId).balance(BigDecimal.ONE).build();
        when(repository.findOne(any())).thenReturn(Optional.of(account));
        this.service.getAccount(expectedAccountId);
        // then
        verify(repository, times(1)).findOne(accountIdCaptor.capture());
        assertEquals(expectedAccountId, accountIdCaptor.getValue());
    }

    @Test(expected = AccountNotFoundException.class)
    public void accountNotFound() {
        // given
        String expectedAccountId = "foo";
        // when
        when(repository.findOne(any())).thenReturn(Optional.empty());
        this.service.getAccount(expectedAccountId);
        // then
        verify(repository, times(1)).findOne(any());
    }

    @Test(expected = Exception.class)
    public void getAccountRepositoryFails() {
        // given
        String expectedAccountId = "foo";
        // when
        when(repository.findOne(any())).thenThrow(new Exception());
        this.service.getAccount(expectedAccountId);
        // then
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    public void getAccounts() {
        // when
        this.service.getAccounts();
        // then
        verify(repository, times(1)).findAll();
    }

    @Test(expected = Exception.class)
    public void getAccountsRepositoryFails() {
        // when
        when(repository.findAll()).thenThrow(new Exception());
        this.service.getAccounts();
        // then
        verify(repository, times(1)).findAll();
    }

    @Test
    public void updateAccount() {
        // given
        Account expected = Account.builder().id("foo").balance(BigDecimal.ONE).build();
        // when
        this.service.updateAccount(expected);
        // then
        verify(repository, times(1)).save(accountCaptor.capture());
        assertEquals(expected.getId(), accountCaptor.getValue().getId());
        assertEquals(expected.getBalance(), accountCaptor.getValue().getBalance());
    }

    @Test(expected = Exception.class)
    public void updateAccountRepositoryFails() {
        // given
        Account expected = Account.builder().id("foo").balance(BigDecimal.ONE).build();
        // when
        when(repository.save(any(Account.class))).thenThrow(new Exception());
        this.service.updateAccount(expected);
        // then
        verify(repository, times(1)).save(any(Account.class));
    }

}

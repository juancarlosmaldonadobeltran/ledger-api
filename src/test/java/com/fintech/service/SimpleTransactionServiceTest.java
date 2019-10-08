package com.fintech.service;

import com.fintech.domain.Account;
import com.fintech.domain.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SimpleTransactionServiceTest {

    private TransactionService service;

    @Mock
    private AccountService accountService;

    @Captor
    ArgumentCaptor<Account> accountCaptor;

    @Captor
    ArgumentCaptor<List<Account>> accountsCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.service = new SimpleTransactionService(accountService);
    }

    @Test
    public void deposit() {
        // given
        String expectedAccountId = "foo";
        BigDecimal expectedAmount = BigDecimal.ONE;
        Account account = Account.builder().id(expectedAccountId).balance(BigDecimal.ZERO).build();
        // when
        when(this.accountService.getAccount(expectedAccountId)).thenReturn(account);
        this.service.deposit(expectedAccountId, expectedAmount);
        // then
        verify(accountService, times(1)).getAccount(any());
        verify(accountService, times(1)).updateAccount(accountCaptor.capture());
        assertEquals(expectedAccountId, accountCaptor.getValue().getId());
        assertEquals(expectedAmount, accountCaptor.getValue().getBalance());

    }

    @Test(expected = Exception.class)
    public void depositGetAccountFails() {
        // given
        String accountId = "foo";
        BigDecimal amount = BigDecimal.ONE;
        // when
        when(this.accountService.getAccount(accountId)).thenThrow(new Exception());
        this.service.deposit(accountId, amount);
        // then
        verify(accountService, times(1)).getAccount(any());
        verify(accountService, times(0)).updateAccount(any());
    }

    @Test(expected = Exception.class)
    public void depositUpdateAccountFails() {
        // given
        String accountId = "foo";
        BigDecimal amount = BigDecimal.ONE;
        Account account = Account.builder().id(accountId).balance(BigDecimal.ZERO).build();
        // when
        when(this.accountService.getAccount(accountId)).thenReturn(account);
        when(this.accountService.updateAccount(any())).thenThrow(new Exception());
        this.service.deposit(accountId, amount);
        // then
        verify(accountService, times(1)).getAccount(any());
        verify(accountService, times(1)).updateAccount(any());
    }

    @Test
    public void transfer() {
        // given
        String sourceAccountId = "foo";
        String destinationAccountId = "bar";
        BigDecimal toTransfer = BigDecimal.ONE;
        BigDecimal expectedSourceBalance = BigDecimal.ZERO;
        BigDecimal expectedDestinationBalance = BigDecimal.ONE;
        Account sourceBefore = Account.builder().id(sourceAccountId).balance(BigDecimal.ONE).build();
        Account destinationBefore = Account.builder().id(destinationAccountId).balance(BigDecimal.ZERO).build();
        Transfer transfer = Transfer.builder().source(sourceAccountId).destination(destinationAccountId).amount(toTransfer).build();
        when(accountService.getAccount(sourceAccountId)).thenReturn(sourceBefore);
        when(accountService.getAccount(destinationAccountId)).thenReturn(destinationBefore);
        // when
        this.service.transfer(transfer);
        // then
        verify(accountService, times(1)).getAccount(sourceAccountId);
        verify(accountService, times(1)).getAccount(destinationAccountId);
        verify(accountService, times(1)).updateAccounts(accountsCaptor.capture());
        int expectedUpdatedAccounts = 2;
        List<Account> actualAccounts = accountsCaptor.getValue();
        assertEquals(expectedUpdatedAccounts, actualAccounts.size());
        Account sourceAfter = actualAccounts.stream().filter(account -> account.getId().equals(sourceAccountId)).findFirst().get();
        Account destinationAfter = actualAccounts.stream().filter(account -> account.getId().equals(destinationAccountId)).findFirst().get();
        assertEquals(expectedSourceBalance, sourceAfter.getBalance());
        assertEquals(expectedDestinationBalance, destinationAfter.getBalance());
    }

    @Test(expected = Exception.class)
    public void transferGetSourceAccountFails() {
        // given
        String sourceAccountId = "foo";
        String destinationAccountId = "bar";
        BigDecimal toTransfer = BigDecimal.ONE;
        Transfer transfer = Transfer.builder().source(sourceAccountId).destination(destinationAccountId).amount(toTransfer).build();
        when(accountService.getAccount(sourceAccountId)).thenThrow(new Exception());
        // when
        this.service.transfer(transfer);
        // then
        verify(accountService, times(1)).getAccount(sourceAccountId);
        verify(accountService, times(0)).getAccount(destinationAccountId);
        verify(accountService, times(0)).updateAccounts(any());
    }

    @Test(expected = Exception.class)
    public void transferGetDestinationAccountFails() {
        // given
        String sourceAccountId = "foo";
        String destinationAccountId = "bar";
        BigDecimal toTransfer = BigDecimal.ONE;
        Account sourceBefore = Account.builder().id(sourceAccountId).balance(BigDecimal.ONE).build();
        Transfer transfer = Transfer.builder().source(sourceAccountId).destination(destinationAccountId).amount(toTransfer).build();
        when(accountService.getAccount(sourceAccountId)).thenReturn(sourceBefore);
        when(accountService.getAccount(destinationAccountId)).thenThrow(new Exception());
        // when
        this.service.transfer(transfer);
        // then
        verify(accountService, times(1)).getAccount(sourceAccountId);
        verify(accountService, times(1)).getAccount(destinationAccountId);
        verify(accountService, times(0)).updateAccounts(any());
    }

    @Test(expected = Exception.class)
    public void transferUpdateAccountsFails() {
        // given
        String sourceAccountId = "foo";
        String destinationAccountId = "bar";
        BigDecimal toTransfer = BigDecimal.ONE;
        Account sourceBefore = Account.builder().id(sourceAccountId).balance(BigDecimal.ONE).build();
        Account destinationBefore = Account.builder().id(destinationAccountId).balance(BigDecimal.ZERO).build();
        Transfer transfer = Transfer.builder().source(sourceAccountId).destination(destinationAccountId).amount(toTransfer).build();
        when(accountService.getAccount(sourceAccountId)).thenReturn(sourceBefore);
        when(accountService.getAccount(destinationAccountId)).thenReturn(destinationBefore);
        doThrow(Exception.class).when(accountService).updateAccounts(anyList());
        // when
        this.service.transfer(transfer);
        // then
        verify(accountService, times(1)).getAccount(sourceAccountId);
        verify(accountService, times(1)).getAccount(destinationAccountId);
        verify(accountService, times(1)).updateAccounts(anyList());
    }

    @Test
    public void concurrentTransfers() {

    }

}

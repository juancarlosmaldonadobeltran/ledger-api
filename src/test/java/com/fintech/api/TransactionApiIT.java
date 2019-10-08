package com.fintech.api;

import com.fintech.domain.Account;
import com.fintech.domain.Transfer;
import com.fintech.service.AccountService;
import com.fintech.service.SimpleAccountService;
import com.fintech.service.SimpleTransactionService;
import com.fintech.service.TransactionService;
import com.fintech.util.IntegrationTest;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static com.fintech.common.ApiResponse.StatusResponse;
import static com.fintech.util.TestUtil.TestResponse;
import static org.junit.Assert.assertEquals;
import static spark.Spark.awaitInitialization;

public class TransactionApiIT extends IntegrationTest {

    private static final AccountService accountService = new SimpleAccountService(repository);
    private static final TransactionService service = new SimpleTransactionService(accountService);

    @BeforeClass
    public static void setUpBeforeClass() {
        TransactionApi api = new TransactionApi(service, transformer);
        api.routes();
        ErrorApi errorApi = new ErrorApi(transformer);
        errorApi.routes();
        awaitInitialization();
    }

    @Test
    public void deposit() throws IOException {
        // given
        BigDecimal amount = BigDecimal.ONE;
        Account accountBefore = accountService.addAccount(BigDecimal.ZERO);
        // when
        String path = "/api/accounts/" + accountBefore.getId() + "/deposit";
        TestResponse res = testUtil.post(path, transformer.render(amount));
        // then
        assertEquals(HttpStatus.OK_200, res.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, res.getBody().getStatus());
        Account accountAfter = transformer.fromJson(res.getBody().getData().toString(), Account.class);
        assertEquals(accountAfter.getBalance(), amount);

    }

    @Test
    public void createTransfer() throws IOException {
        // given
        BigDecimal amount = BigDecimal.ONE;
        Account sourceBefore = accountService.addAccount(amount);
        Account destinationBefore = accountService.addAccount(BigDecimal.ZERO);
        // when
        String path = "/api/transfers";
        Transfer transfer =  Transfer.builder()
                .source(sourceBefore.getId())
                .destination(destinationBefore.getId())
                .amount(BigDecimal.ONE).build();
        TestResponse res = testUtil.post(path, transformer.render(transfer));
        // then
        assertEquals(HttpStatus.OK_200, res.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, res.getBody().getStatus());
        Account sourceAfter = accountService.getAccount(transfer.getSource());
        Account destinationAfter = accountService.getAccount(transfer.getDestination());
        assertEquals(BigDecimal.ZERO, sourceAfter.getBalance());
        assertEquals(amount, destinationAfter.getBalance());
    }


    @Test
    public void insufficientFounds() throws IOException {
        // given
        BigDecimal amount = BigDecimal.ONE;
        Account sourceBefore = accountService.addAccount(BigDecimal.ZERO);
        Account destinationBefore = accountService.addAccount(BigDecimal.ZERO);
        // when
        String path = "/api/transfers";
        Transfer transfer =  Transfer.builder()
                .source(sourceBefore.getId())
                .destination(destinationBefore.getId())
                .amount(BigDecimal.ONE).build();
        TestResponse res = testUtil.post(path, transformer.render(transfer));
        // then
        assertEquals(HttpStatus.BAD_REQUEST_400, res.getStatusCode());
        assertEquals(StatusResponse.ERROR, res.getBody().getStatus());
        assertEquals("Insufficient founds", res.getBody().getMessage());
    }



}
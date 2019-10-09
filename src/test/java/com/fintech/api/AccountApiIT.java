package com.fintech.api;

import com.fintech.domain.Account;
import com.fintech.domain.NewAccount;
import com.fintech.service.AccountService;
import com.fintech.service.SimpleAccountService;
import com.fintech.util.IntegrationTest;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.fintech.common.ApiResponse.StatusResponse;
import static com.fintech.util.TestUtil.TestResponse;
import static org.junit.Assert.*;
import static spark.Spark.awaitInitialization;

public class AccountApiIT extends IntegrationTest {

    private static final AccountService service = new SimpleAccountService(repository);

    @BeforeClass
    public static void setUpBeforeClass() {
        AccountApi api = new AccountApi(service, transformer);
        api.routes();
        ErrorApi errorApi = new ErrorApi(transformer);
        errorApi.routes();
        awaitInitialization();
    }

    @Test
    public void createAccount() throws IOException {

        // given
        BigDecimal expectedBalance = BigDecimal.ZERO;
        // when
        String path = "/api/accounts";
        TestResponse res = testUtil.post(path, transformer.render(NewAccount.builder().balance(expectedBalance).build()));
        // then
        assertEquals(HttpStatus.CREATED_201, res.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, res.getBody().getStatus());
        assertEquals(StatusResponse.SUCCESS, res.getBody().getStatus());
        Account actual = transformer.fromJson(res.getBody().getData().toString(), Account.class);
        assertEquals(expectedBalance, actual.getBalance());
        assertNotNull(actual.getId());
        String expectedLocationHeader = "/api/accounts/" + actual.getId();
        assertEquals("/api/accounts/" + actual.getId(), res.getHeaders().get("Location"));
    }

    @Test
    public void getAccount() throws IOException {
        // given
        Account expected = service.addAccount(BigDecimal.ZERO);
        // when
        String path = "/api/accounts/" + expected.getId();
        TestResponse res = testUtil.get(path);
        // then
        assertEquals(HttpStatus.OK_200, res.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, res.getBody().getStatus());
        Account actual = transformer.fromJson(res.getBody().getData().toString(), Account.class);
        assertEquals(expected.getBalance(), actual.getBalance());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void accountNotFound() throws IOException {
        // given
        String notExistentAccountId = "foo";
        // when
        String path = "/api/accounts/" + notExistentAccountId;
        TestResponse res = testUtil.get(path);
        // then
        assertEquals(HttpStatus.NOT_FOUND_404, res.getStatusCode());
        assertEquals(StatusResponse.ERROR, res.getBody().getStatus());
        assertEquals("Account not found", res.getBody().getMessage());
    }

    @Test
    public void getAccounts() throws IOException {

        // given
        Account expected = service.addAccount(BigDecimal.ZERO);
        // when
        String path = "/api/accounts";
        TestResponse res = testUtil.get(path);
        // then
        assertEquals(HttpStatus.OK_200, res.getStatusCode());
        assertEquals(StatusResponse.SUCCESS, res.getBody().getStatus());
        List<Account> accounts = Arrays.asList(transformer.fromJson(res.getBody().getData().getAsJsonArray().toString(), Account[].class));
        assertTrue(accounts.contains(expected));
        int expectedSize = 1;
        assertEquals(expectedSize, accounts.size());
        Account actual = accounts.stream().findFirst().get();
        assertEquals(expected.getBalance(), actual.getBalance());
        assertEquals(expected.getId(), actual.getId());
    }

}
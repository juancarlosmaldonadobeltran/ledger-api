package com.fintech.api;

import com.fintech.common.ApiResponse;
import com.fintech.common.JsonTransformer;
import com.fintech.domain.Account;
import com.fintech.service.AccountService;
import com.google.inject.Inject;

import java.math.BigDecimal;

import static spark.Spark.*;

public class AccountApi implements Api {

    private final AccountService accountService;
    private final JsonTransformer transformer;

    @Inject
    AccountApi(AccountService accountService, JsonTransformer transformer) {

        this.accountService = accountService;
        this.transformer = transformer;

    }

    @Override
    public void routes() {

        path("/api", () -> {

            post("/accounts", (req, res) -> {
                BigDecimal amount = transformer.fromJson(req.body(), BigDecimal.class);
                res.status(201);
                Account account = accountService.addAccount(amount);
                res.header("Location", "/api/accounts/" + account.getId());
                return ApiResponse.success(transformer.toJsonTree(account));
            }, transformer);

            get("/accounts/:id", (req, res) ->
                    ApiResponse.success(transformer.toJsonTree(accountService.getAccount(req.params(":id"))))
                    , transformer);

            get("/accounts", (req, res) -> ApiResponse.success(transformer.toJsonTree(accountService.getAccounts()))
                    , transformer);
        });
    }

}

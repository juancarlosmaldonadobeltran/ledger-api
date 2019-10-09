package com.fintech.api;

import com.fintech.common.ApiResponse;
import com.fintech.common.JsonTransformer;
import com.fintech.domain.Deposit;
import com.fintech.domain.Transfer;
import com.fintech.service.TransactionService;
import com.google.inject.Inject;

import static spark.Spark.path;
import static spark.Spark.post;

public class TransactionApi implements Api {

    private final TransactionService transactionService;
    private final JsonTransformer transformer;


    @Inject
    TransactionApi(TransactionService transferService, JsonTransformer transformer) {

        this.transactionService = transferService;
        this.transformer = transformer;

    }

    public void routes() {

        path("/api", () -> {

            post("/transfers", (req, res) -> {
                Transfer transfer = transformer.fromJson(req.body(), Transfer.class);
                transactionService.transfer(transfer);
                return ApiResponse.success();
            }, transformer);

            post("/accounts/:id/deposit", (req, res) -> {
                Deposit deposit = transformer.fromJson(req.body(), Deposit.class);
                return ApiResponse.success(transformer.toJsonTree(transactionService.deposit(req.params(":id"), deposit.getAmount())));
            }, transformer);

        });
    }

}

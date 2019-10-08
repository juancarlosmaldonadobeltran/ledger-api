package com.fintech.api;

import com.fintech.common.ApiResponse;
import com.fintech.common.JsonTransformer;
import com.fintech.exception.AccountNotFoundException;
import com.fintech.exception.InsufficientFoundsException;
import com.google.inject.Inject;

import static spark.Spark.*;

public class ErrorApi implements Api {

    private final JsonTransformer transformer;

    @Inject
    ErrorApi(JsonTransformer transformer) {
        this.transformer = transformer;
    }

    public void routes() {

        post("/error", (req, res) -> ApiResponse.error(req.body()), transformer);

        exception(AccountNotFoundException.class, (e, req, res) -> {
            res.status(404);
            res.body(transformer.render(ApiResponse.error(e.getMessage())));
        });

        exception(InsufficientFoundsException.class, (e, req, res) -> {
            res.status(400);
            res.body(transformer.render(ApiResponse.error(e.getMessage())));
        });

        notFound((req, res) -> transformer.render(ApiResponse.error("Route not found")));

        internalServerError((req, res) -> transformer.render(ApiResponse.error("Internal server error")));

    }

}






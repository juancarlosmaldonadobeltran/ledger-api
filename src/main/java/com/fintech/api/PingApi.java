package com.fintech.api;

import com.fintech.common.ApiResponse;
import com.fintech.common.JsonTransformer;
import com.google.inject.Inject;

import static spark.Spark.get;

public class PingApi implements Api {

    private final JsonTransformer transformer;

    @Inject
    public PingApi(JsonTransformer transformer) {
        this.transformer = transformer;
    }

    public void routes() {

        get("/ping", (req, res) -> ApiResponse.success(transformer.toJsonTree("PONG")), transformer);
    }

}






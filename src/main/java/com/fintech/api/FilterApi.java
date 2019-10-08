package com.fintech.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.after;
import static spark.Spark.before;

public class FilterApi implements Api {

    private static final Logger LOG = LoggerFactory.getLogger(FilterApi.class);

    public void routes() {

        before("/*", (req, res) -> LOG.info(String.format("%s  %s %s", req.requestMethod(), req.uri(), req.body())));
        after("/*", (req, res) -> LOG.info(res.body()));
    }

}






package com.fintech.config;

import com.fintech.api.Api;

import java.util.ArrayList;
import java.util.List;

public abstract class ApiConfigBase implements ApiConfig {

    private List<Api> apis;

    ApiConfigBase() {
        this.apis = new ArrayList<>();
    }

    void register(Api api) {
        this.apis.add(api);
    }

    @Override
    public void configure() {
        this.apis.forEach(Api::routes);
    }
}

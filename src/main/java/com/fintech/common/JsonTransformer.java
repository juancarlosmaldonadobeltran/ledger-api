package com.fintech.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import spark.ResponseTransformer;

import java.lang.reflect.Type;

public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

    public <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }

}
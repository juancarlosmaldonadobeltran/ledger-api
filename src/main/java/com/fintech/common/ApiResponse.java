package com.fintech.common;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class ApiResponse {

    @NonNull
    private StatusResponse status;
    private String message;
    private JsonElement data;

    public static ApiResponse success() {
        return ApiResponse.builder().status(StatusResponse.SUCCESS).build();
    }

    public static ApiResponse success(JsonElement data) {
        return ApiResponse.builder().status(StatusResponse.SUCCESS).data(data).build();
    }

    public static ApiResponse error(String message) {
        return ApiResponse.builder().status(StatusResponse.ERROR).message(message).build();
    }

    @AllArgsConstructor
    public enum StatusResponse {

        SUCCESS("Success"),
        ERROR("Error");

        private String status;
    }

}

package com.fintech.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@Builder
public class Account {

    @NonNull
    private String id;

    @NonNull
    private BigDecimal balance;

}

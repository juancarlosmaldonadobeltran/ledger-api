package com.fintech.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@Builder
public class Deposit {

    @NonNull
    private BigDecimal amount;

}

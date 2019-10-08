package com.fintech.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@Builder
public class Transfer {

    @NonNull
    private String source;

    @NonNull
    private String destination;

    @NonNull
    protected BigDecimal amount;

}

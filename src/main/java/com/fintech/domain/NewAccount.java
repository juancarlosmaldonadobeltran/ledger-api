package com.fintech.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class NewAccount {

    private BigDecimal balance;

}

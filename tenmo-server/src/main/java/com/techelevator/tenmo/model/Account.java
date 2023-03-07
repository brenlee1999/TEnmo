package com.techelevator.tenmo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class Account {
    @NotNull
    private int accountId;
    @NotNull
    private int userId;
    @NotNull
    private BigDecimal balance;
}

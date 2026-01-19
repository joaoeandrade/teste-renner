package br.com.teste.renner.feature.Account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull @PositiveOrZero BigDecimal initialBalance
) {}
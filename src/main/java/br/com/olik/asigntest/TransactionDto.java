package br.com.olik.asigntest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionDto {
    private Long userId;
    private BigDecimal amount;
}

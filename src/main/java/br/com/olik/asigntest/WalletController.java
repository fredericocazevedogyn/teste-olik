package br.com.olik.asigntest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/amount")
    public BigDecimal getAmount(Long userId) {
        return walletService.getAmount(userId);
    }

    @PostMapping("/transaction")
    public BigDecimal transaction(@RequestBody TransactionDto transactionDto) {
        return walletService.transaction(transactionDto);
    }
}

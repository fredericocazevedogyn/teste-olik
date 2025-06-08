package br.com.olik.asigntest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class WalletService {


    private final WalletRepository walletRepository;

    public BigDecimal getAmount(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);
        return wallet.getAmount();
    }

    public BigDecimal transaction(@RequestBody TransactionDto transactionDto) {
        log.info("Start Transaction {}", transactionDto);
        Wallet wallet = walletRepository.findByUserId(transactionDto.getUserId());

        wallet.setAmount(wallet.getAmount().add(transactionDto.getAmount()));

        if(wallet.getType() == Wallet.WalletType.ITAU) {
            getSaldoItau(wallet);
        } else {
            persistirWallet(wallet);
        }

        log.info("Wallet {}", wallet);
        return wallet.getAmount();
    }

    private void getSaldoItau(Wallet wallet) {
        if(wallet.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletException("Saldo insuficiente! ");
        }
        persistirWallet(wallet);
    }

    private void persistirWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

}

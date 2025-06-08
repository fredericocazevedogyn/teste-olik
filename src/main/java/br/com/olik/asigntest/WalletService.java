package br.com.olik.asigntest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

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
        BigDecimal novoSaldo = wallet.getAmount().add(transactionDto.getAmount());
        wallet.setAmount(novoSaldo);

        validarSaldo(wallet);
        persistirWallet(wallet);

        log.info("Wallet atualizada: {}", wallet);
        return wallet.getAmount();
    }

    private void validarSaldo(Wallet wallet) {
        if (wallet.getType() == Wallet.WalletType.ITAU && wallet.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletException("Saldo insuficiente para realizar a operação.");
        }
    }

    private void persistirWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }


}

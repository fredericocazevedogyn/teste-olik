package br.com.olik.asigntest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    private WalletRepository walletRepository;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        walletService = new WalletService(walletRepository);
    }

    @Test
    void deveRetornarSaldoDoUsuario() {
        Wallet wallet = Wallet.builder()
                .userId(1L)
                .amount(new BigDecimal("100.00"))
                .type(Wallet.WalletType.ITAU)
                .build();

        when(walletRepository.findByUserId(1L)).thenReturn(wallet);

        BigDecimal saldo = walletService.getAmount(1L);
        assertEquals(new BigDecimal("100.00"), saldo);
    }

    @Test
    void deveRealizarTransacaoComSaldoPositivo() {
        Wallet wallet = Wallet.builder()
                .userId(1L)
                .amount(new BigDecimal("50.00"))
                .type(Wallet.WalletType.ITAU)
                .build();

        TransactionDto dto = new TransactionDto(1L, new BigDecimal("25.00"));

        when(walletRepository.findByUserId(1L)).thenReturn(wallet);

        BigDecimal resultado = walletService.transaction(dto);

        assertEquals(new BigDecimal("75.00"), resultado);
        verify(walletRepository).save(wallet);
    }

    @Test
    void deveLancarExcecaoQuandoSaldoNegativoParaItau() {
        Wallet wallet = Wallet.builder()
                .userId(1L)
                .amount(new BigDecimal("-10.00"))
                .type(Wallet.WalletType.ITAU)
                .build();

        TransactionDto dto = new TransactionDto(1L, BigDecimal.ZERO);

        when(walletRepository.findByUserId(1L)).thenReturn(wallet);

        WalletException exception = assertThrows(WalletException.class, () -> {
            walletService.transaction(dto);
        });

        assertEquals("Saldo insuficiente para realizar a operação.", exception.getMessage());
        verify(walletRepository, never()).save(any());
    }
}


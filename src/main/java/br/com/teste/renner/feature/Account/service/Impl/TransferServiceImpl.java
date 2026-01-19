package br.com.teste.renner.feature.Account.service.Impl;

import br.com.teste.renner.feature.Account.Entity.Account;
import br.com.teste.renner.feature.Account.Repository.AccountRepository;
import br.com.teste.renner.feature.Account.TransferRequest;
import br.com.teste.renner.feature.notification.Impl.EmailNotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class TransferServiceImpl implements Transferservice {

    private final AccountRepository accountRepository;
    private final EmailNotificationService emailNotificationService;
    @Override
    @Transactional
    public void transfer(TransferRequest request) {
        validateRequest(request);

        // Evita deadlock: sempre buscar/lockar na menor id primeiro
        Long firstId = request.fromAccountId() < request.toAccountId()
                ? request.fromAccountId()
                : request.toAccountId();

        Long secondId = request.fromAccountId() < request.toAccountId()
                ? request.toAccountId()
                : request.fromAccountId();

        Account first = accountRepository.findById(firstId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + firstId));

        Account second = accountRepository.findById(secondId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + secondId));

        Account from = request.fromAccountId().equals(first.getId()) ? first : second;
        Account to = request.toAccountId().equals(first.getId()) ? first : second;

        BigDecimal amount = request.amount(); // ✅ deve ser BigDecimal no DTO

        // Regra de negócio na entidade
        from.debit(amount);
        to.credit(amount);

        // Save explícito (opcional, mas deixa claro)
        accountRepository.save(from);
        accountRepository.save(to);

        // Envia e-mail após sucesso
        sendEmails(from, to, amount);
    }

    private void sendEmails(Account from, Account to, BigDecimal amount) {
        String subjectFrom = "Transferência realizada";
        String bodyFrom = "Olá, " + from.getName() + "!\n\n"
                + "Sua transferência foi concluída com sucesso.\n"
                + "Valor: R$ " + amount + "\n"
                + "Destino (conta): " + to.getId() + "\n\n"
                + "Atenciosamente,\nBanco Digital";

        emailNotificationService.sendEmail(from.getEmail(), subjectFrom, bodyFrom);

        String subjectTo = "Você recebeu uma transferência";
        String bodyTo = "Olá, " + to.getName() + "!\n\n"
                + "Você recebeu uma transferência.\n"
                + "Valor: R$ " + amount + "\n"
                + "Origem (conta): " + from.getId() + "\n\n"
                + "Atenciosamente,\nBanco Digital";

        emailNotificationService.sendEmail(to.getEmail(), subjectTo, bodyTo);
    }

    private void validateRequest(TransferRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.fromAccountId() == null || request.toAccountId() == null) {
            throw new IllegalArgumentException("Conta origem e destino são obrigatórias");
        }
        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new IllegalArgumentException("Conta origem e destino devem ser diferentes");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }
}
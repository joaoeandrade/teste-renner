package br.com.teste.renner.feature.Account.service.Impl;

import br.com.teste.renner.feature.Account.CreateAccountRequest;
import br.com.teste.renner.feature.Account.Entity.Account;
import br.com.teste.renner.feature.Account.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account create(CreateAccountRequest request) {
        Account account = new Account();
        account.setName(request.name());
        account.setEmail(request.email());
        account.setBalance(request.initialBalance());
        account.setVersion(0L);

        return accountRepository.save(account);
    }



    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }
}

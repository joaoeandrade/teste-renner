package br.com.teste.renner.feature.Account.service.Impl;

import br.com.teste.renner.feature.Account.CreateAccountRequest;
import br.com.teste.renner.feature.Account.Entity.Account;

import java.util.List;

public interface AccountService {
    Account create(CreateAccountRequest request);

    List<Account> findAll();

    Account findById(Long id);



}

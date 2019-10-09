package com.fintech.config;

import com.fintech.App;
import com.fintech.repository.AccountRepository;
import com.fintech.service.AccountService;
import com.fintech.service.SimpleAccountService;
import com.fintech.service.SimpleTransactionService;
import com.fintech.service.TransactionService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class AppModuleConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(App.class).in(Singleton.class);
        bind(ApiConfig.class).to(LedgerApiConfig.class);
        bind(AccountRepository.class).in(Singleton.class);
        bind(AccountService.class).to(SimpleAccountService.class);
        bind(TransactionService.class).to(SimpleTransactionService.class);
    }

}

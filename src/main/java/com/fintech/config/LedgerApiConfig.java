package com.fintech.config;

import com.fintech.api.*;
import com.google.inject.Inject;

public class LedgerApiConfig extends ApiConfigBase {

    @Inject
    public LedgerApiConfig(AccountApi accountApi,
                           TransactionApi transferApi,
                           FilterApi filterApi,
                           PingApi pingApi,
                           ErrorApi errorApi) {
        this.register(accountApi);
        this.register(transferApi);
        this.register(filterApi);
        this.register(pingApi);
        this.register(errorApi);
    }

}

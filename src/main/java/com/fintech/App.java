package com.fintech;

import com.fintech.config.ApiConfig;
import com.fintech.config.AppModuleConfig;
import com.google.inject.Guice;
import com.google.inject.Inject;

public class App {

    private final ApiConfig apiConfig;

    @Inject
    public App(final ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    private void run() {
        apiConfig.configure();
    }

    public static void main( String[] args )
    {
        Guice.createInjector(new AppModuleConfig())
                .getInstance(App.class)
                .run();
    }

}

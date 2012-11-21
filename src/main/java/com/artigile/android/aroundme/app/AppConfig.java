package com.artigile.android.aroundme.app;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

/**
 * @author IoaN, 11/20/12 11:34 PM
 */
public class AppConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).asEagerSingleton();
    }
}

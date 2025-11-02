package com.prm.carrental.core;

import android.app.Application;

import com.prm.carrental.core.di.ServiceLocator;

/**
 * Application entry point for initializing shared services.
 */
public class CarRentalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceLocator.init(this);
    }
}

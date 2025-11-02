package com.prm.carrental.core.di;

import android.content.Context;

import com.prm.carrental.core.network.ApiClient;
import com.prm.carrental.core.network.service.AuthService;
import com.prm.carrental.core.network.service.StationsService;
import com.prm.carrental.core.session.SessionManager;

/**
 * Simple service locator to avoid adding DI frameworks in phase 1.
 */
public final class ServiceLocator {

    private static SessionManager sessionManager;
    private static ApiClient apiClient;
    private static AuthService authService;
    private static StationsService stationsService;

    private ServiceLocator() {
        // no-op
    }

    public static void init(Context context) {
        sessionManager = new SessionManager(context.getApplicationContext());
        apiClient = new ApiClient(sessionManager);
        authService = apiClient.createService(AuthService.class);
        stationsService = apiClient.createService(StationsService.class);
    }

    public static SessionManager sessionManager() {
        return sessionManager;
    }

    public static ApiClient apiClient() {
        return apiClient;
    }

    public static AuthService authService() {
        return authService;
    }

    public static StationsService stationsService() {
        return stationsService;
    }
}

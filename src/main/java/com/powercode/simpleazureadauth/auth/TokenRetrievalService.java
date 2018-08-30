package com.powercode.simpleazureadauth.auth;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class TokenRetrievalService {
    static String OUTLOOK_RESOURCE = "https://outlook.office365.com/";

    // tenant AD, or DIRECTORY_ID in Azure
    @Value("${AD_AUTHORITY}")
    private String authorityUri;

    // ApplicationID in Azure
    @Value("${AD_CLIENT_ID}")
    private String clientId;

    public Future<AuthenticationResult> getSecurityToken(String username, String password) {
        ExecutorService service = Executors.newFixedThreadPool(1);
        AuthenticationContext context = null;
        try {
            context = new AuthenticationContext(authorityUri, true, service);
        } catch (MalformedURLException e) {
            // TODO: handle exception
        }

        Future<AuthenticationResult> authRes = context.acquireToken(OUTLOOK_RESOURCE, clientId, username, password, null);

        return authRes;
    }

}

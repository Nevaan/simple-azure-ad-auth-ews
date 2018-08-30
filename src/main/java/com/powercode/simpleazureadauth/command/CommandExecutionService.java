package com.powercode.simpleazureadauth.command;


import com.microsoft.aad.adal4j.AuthenticationResult;
import com.powercode.simpleazureadauth.auth.TokenRetrievalService;
import com.powercode.simpleazureadauth.ews.EwsManagementService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class CommandExecutionService {

    private final TokenRetrievalService tokenRetrievalService;
    private final EwsManagementService ewsManagementService;

    @Autowired
    public CommandExecutionService(final TokenRetrievalService tokenRetrievalService, final EwsManagementService ewsManagementService) {
        this.tokenRetrievalService = tokenRetrievalService;
        this.ewsManagementService = ewsManagementService;
    }

    public ExchangeService authenticate(String username, String password) {
        Future<AuthenticationResult> asyncAuthenticationResult = tokenRetrievalService.getSecurityToken(username, password);

        try {
            AuthenticationResult authenticationResult = asyncAuthenticationResult.get();
            String token = authenticationResult.getAccessToken();
            return ewsManagementService.getService(token);
        } catch (InterruptedException e) {
            // TODO: exception handling
        } catch (ExecutionException e) {
            // TODO: exception handling
        }
        return null;
    }

    public void listInboxFolders(ExchangeService exchangeService) {
        //TODO: verify if token still alive
        FindFoldersResults results = ewsManagementService.getInboxFolders(exchangeService, null);
        results.getFolders().forEach(f -> {
            try {
                System.out.println(f.getDisplayName());
            } catch (ServiceLocalException e) {
            // TODO: exception handling
            }
        });

    }

}

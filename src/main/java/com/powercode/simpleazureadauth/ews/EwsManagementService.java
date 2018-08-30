package com.powercode.simpleazureadauth.ews;

import com.powercode.simpleazureadauth.auth.OAuthTokenCredentials;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FolderView;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class EwsManagementService {

    //TODO: parameter? this is public, default EWS URI
    private static String EWS_URI = "https://outlook.office365.com/EWS/Exchange.asmx";

    public ExchangeService getService(String token) {
        ExchangeService exchangeService = new ExchangeService();
        ExchangeCredentials credentials = null;
        try {
            credentials = new OAuthTokenCredentials(token);
        } catch (Exception e) {
            // TODO: handle exception
        }
        exchangeService.setCredentials(credentials);
        try {
            exchangeService.setUrl(new URI(EWS_URI));
        } catch (URISyntaxException e) {
            // TODO: handle exception
        }

        return exchangeService;
    }

    public FindFoldersResults getInboxFolders(ExchangeService exchangeService, Integer pageSize) {
        Integer requestedPageSize = Optional.ofNullable(pageSize).orElse(50);
        FolderView folderView = new FolderView(requestedPageSize);
        try {
            return exchangeService.findFolders(WellKnownFolderName.Inbox, folderView);
        } catch (Exception e) {
            throw new RuntimeException("Cannot retrieve folders", e);
        }
    }

}

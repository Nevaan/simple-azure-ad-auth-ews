package com.powercode.simpleazureadauth.command;

import microsoft.exchange.webservices.data.core.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellCommandsComponent {

    @Autowired
    private CommandExecutionService commandExecutionService;

    private ExchangeService ewsService;

    @ShellMethod(value =  "User authetication", key = "login")
    public void login(String user, String password) {
        //TODO: change in way that password is acquired secretely
        ewsService = commandExecutionService.authenticate(user, password);
    }

    // TODO: not working while running with java -jar ... ? tested&working in IJ
    @ShellMethod(value = "Listing folders user is authenticated", key = "list")
    public void list() {
        commandExecutionService.listInboxFolders(ewsService);
    }

}

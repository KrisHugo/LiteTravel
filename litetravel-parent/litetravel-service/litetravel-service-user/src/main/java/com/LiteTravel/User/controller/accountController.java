package com.LiteTravel.User.controller;

import com.LiteTravel.User.service.AccountService;
import com.LiteTravel.User.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class accountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private InfoService infoService;

    


}

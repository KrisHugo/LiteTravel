package com.LiteTravel.controller;

import com.LiteTravel.service.AccountService;
import com.LiteTravel.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class accountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private InfoService infoService;

    


}

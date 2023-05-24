package com.example.accounts.controller;


import com.example.accounts.config.AccountsServiceConfig;
import com.example.accounts.model.*;
import com.example.accounts.repository.AccountsRepository;
import com.example.accounts.service.client.CardsFeignClient;
import com.example.accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountsController {

    private final AccountsRepository accountsRepository;
    private final AccountsServiceConfig accountsConfig;
    private final CardsFeignClient cardsFeignClient;
    private final LoansFeignClient loansFeignClient;

    public AccountsController(AccountsRepository accountsRepository, AccountsServiceConfig accountsServiceConfig,
                              CardsFeignClient cardsFeignClient, LoansFeignClient loansFeignClient) {
        this.accountsRepository = accountsRepository;
        this.accountsConfig = accountsServiceConfig;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
    }

    @PostMapping("/myAccount")
    public Accounts getAccountDetails(@RequestBody Customer customer) {
        return accountsRepository.findByCustomerId(customer.getCustomerId());
    }

    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
                accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
        return ow.writeValueAsString(properties);
    }

    @PostMapping("/myCustomerDetails")
    public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(customer);
        List<Cards> cards = cardsFeignClient.getCardDetails(customer);
        return new CustomerDetails(accounts, loans, cards);
    }
}
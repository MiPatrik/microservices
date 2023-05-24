package com.example.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetails {
    private Accounts accounts;
    private List<Loans> loans;
    private List<Cards> cards;
}

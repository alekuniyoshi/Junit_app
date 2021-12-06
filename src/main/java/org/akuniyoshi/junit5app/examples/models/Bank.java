package org.akuniyoshi.junit5app.examples.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public Bank() {
        accounts = new ArrayList<>();
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setBank(this);
    }

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void transfer(Account origin, Account destiny, BigDecimal amount) {
        origin.debit(amount);
        destiny.credit(amount);
    }
}

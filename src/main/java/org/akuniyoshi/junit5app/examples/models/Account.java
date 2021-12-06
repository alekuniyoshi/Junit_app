package org.akuniyoshi.junit5app.examples.models;

import org.akuniyoshi.junit5app.examples.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;

public class Account {
    private String person;
    private BigDecimal amount;
    private Bank bank;


    public Account(String person, BigDecimal amount) {
        this.person = person;
        this.amount = amount;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void debit(BigDecimal amount) {
        BigDecimal newBalance = this.amount.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }
        this.amount = newBalance;
    }

    public void credit(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account)) {
            return false;
        }
        Account account = (Account) obj;
        if (this.person == null || this.amount == null) {
            return false;
        }
        return this.person.equals(account.getPerson()) && this.amount.equals(account.getAmount());
    }
}

package org.poo.userDetails.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.poo.userDetails.card.Card;

import java.util.ArrayList;

@Getter
@Setter
public abstract class Account {
    private final String currency;
    private final String IBAN;
    private final ArrayList<Card> cards = new ArrayList<>();
    private double balance;
    private ArrayNode transactions;
    public Account(final String currency, final String IBAN) {
        this.currency = currency;
        this.IBAN = IBAN;
        transactions = new ObjectMapper().createArrayNode();
        balance = 0;
    }
    public void addToBalance(final double amount) {
        balance += amount;
    }
    public void subtractFromBalance(final double amount) {
        balance -= amount;
    }
    public abstract String getAccountType();
}

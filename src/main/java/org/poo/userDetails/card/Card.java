package org.poo.userDetails.card;

import lombok.Data;

@Data
public abstract class Card {
    protected String cardNumber;
    protected String status;
    public Card(final String cardNumber) {
        this.cardNumber = cardNumber;
        this.status = "active";
    }
    public abstract String getCardType();
}

package org.poo.userDetails.card;

import lombok.Data;

@Data
public class Card {
    private String cardNumber;
    public Card(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

}

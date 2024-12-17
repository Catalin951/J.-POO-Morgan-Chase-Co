package org.poo.userDetails.card;

import lombok.Data;

import java.io.Serializable;

@Data
public class OneTimeCard extends Card {
    public OneTimeCard(final String cardNumber) {
        super(cardNumber);
    }
    public String getCardType() {
        return "OneTimeCard";
    }
}

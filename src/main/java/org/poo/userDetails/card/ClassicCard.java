package org.poo.userDetails.card;

import lombok.Data;

@Data
public class ClassicCard extends Card {
    public ClassicCard(final String cardNumber) {
        super(cardNumber);
    }
}

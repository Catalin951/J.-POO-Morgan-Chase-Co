package org.poo.userDetails.card;

import lombok.Data;
import org.poo.mapper.Mappers;
import org.poo.userDetails.account.Account;


@Data
public abstract class Card {
    protected String cardNumber;
    protected String status;
    protected boolean isFrozen;
    public Card(final String cardNumber) {
        this.cardNumber = cardNumber;
        this.status = "active";
        isFrozen = false;
    }
    public abstract void subtractFromBalance(final double amount, final Account ownerAccount,
                                             final Mappers mappers, final int timestamp);
    public abstract String getCardType();
}

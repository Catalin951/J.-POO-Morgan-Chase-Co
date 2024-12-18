package org.poo.userDetails.card;

import lombok.Data;
import org.poo.userDetails.account.Account;

import java.io.Serializable;

@Data
public class OneTimeCard extends Card {
    public OneTimeCard(final String cardNumber) {
        super(cardNumber);
    }
    public void subtractFromBalance(final double amount, final Account account) {
        account.subtractFromBalance(amount);
        int i = 0;
        for(Card card : account.getCards()) {
            if(card.getCardNumber().equals(this.getCardNumber())) {
                account.getCards().remove(i);
                return;
            }
            i++;
        }
    }
    public String getCardType() {
        return "OneTimeCard";
    }
}

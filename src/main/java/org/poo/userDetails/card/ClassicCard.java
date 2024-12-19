package org.poo.userDetails.card;

import lombok.Data;
import org.poo.mapper.Mappers;
import org.poo.userDetails.account.Account;

@Data
public class ClassicCard extends Card {
    public ClassicCard(final String cardNumber) {
        super(cardNumber);
    }
    public void subtractFromBalance(final double amount, final Account account,
                                    final Mappers mappers, final int timestamp) {
        account.subtractFromBalance(amount);
    }
    public String getCardType() {
        return "classic";
    }
}

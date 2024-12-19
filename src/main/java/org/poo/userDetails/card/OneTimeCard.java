package org.poo.userDetails.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jdk.jshell.execution.Util;
import lombok.Data;
import org.poo.commands.create.CreateCard;
import org.poo.commands.create.CreateOneTimeCard;
import org.poo.mapper.Mappers;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.utils.Utils;

import java.io.Serializable;

@Data
public class OneTimeCard extends Card {
    public OneTimeCard(final String cardNumber) {
        super(cardNumber);
    }
    public void subtractFromBalance(final double amount, final Account account,
                                    final Mappers mappers, final int timestamp) {
        account.subtractFromBalance(amount);
        int i = 0;
        for(Card card : account.getCards()) {
            if(card.getCardNumber().equals(this.getCardNumber())) {
                User user = mappers.getUserForAccount(account);
                account.getCards().remove(i);
                account.getCards().addLast(this);
                ObjectNode destroyNode = new ObjectMapper().createObjectNode();
                destroyNode.put("timestamp", timestamp);
                destroyNode.put("description", "The card has been destroyed");
                destroyNode.put("card", card.getCardNumber());
                destroyNode.put("cardHolder", user.getEmail());
                destroyNode.put("account", account.getIBAN());
                account.getTransactions().add(destroyNode);
                user.getTransactions().add(destroyNode);


                this.reset();
                ObjectNode createNode = new ObjectMapper().createObjectNode();
                createNode.put("account", account.getIBAN());
                createNode.put("card", cardNumber);
                createNode.put("cardHolder", user.getEmail());
                createNode.put("description", "New card created");
                createNode.put("timestamp", timestamp);

                account.getTransactions().add(createNode);
                user.getTransactions().add(createNode);
                return;
            }
            i++;
        }
    }
    public String getCardType() {
        return "OneTimeCard";
    }
    private void reset() {
        this.cardNumber = Utils.generateCardNumber();
        this.status = "active";
        this.isFrozen = false;
    }
}

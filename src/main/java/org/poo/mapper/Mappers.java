package org.poo.mapper;

import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.card.Card;

import java.util.HashMap;

public class Mappers {
    private final HashMap<Account, User> accountToUserMap;
    private final HashMap<String, User> emailToUserMap;
    private final HashMap<String, Account> ibanToAccountMap;
    private final HashMap<Card, Account> cardToAccountMap;
    private final HashMap<String, Card> cardNumberToCardMap;

    public Mappers() {
        this.accountToUserMap = new HashMap<>();
        this.emailToUserMap = new HashMap<>();
        this.ibanToAccountMap = new HashMap<>();
        this.cardToAccountMap = new HashMap<>();
        this.cardNumberToCardMap = new HashMap<>();
    }

    // Account -> User mapping
    public void addAccountToUser(Account account, User user) {
        if (account != null && user != null) {
            accountToUserMap.put(account, user);
        }
    }

    public User getUserForAccount(Account account) {
        return accountToUserMap.get(account); // Returns null if not found
    }

    public boolean containsAccount(Account account) {
        return accountToUserMap.containsKey(account);
    }

    // Email -> User mapping
    public void addEmailToUser(String email, User user) {
        if (email != null && user != null) {
            emailToUserMap.put(email, user);
        }
    }

    public User getUserForEmail(String email) {
        return emailToUserMap.get(email); // Returns null if not found
    }

    public boolean containsEmail(String email) {
        return emailToUserMap.containsKey(email);
    }

    // IBAN -> Account mapping
    public void addIbanToAccount(String iban, Account account) {
        if (iban != null && account != null) {
            ibanToAccountMap.put(iban, account);
        }
    }

    public Account getAccountForIban(String iban) {
        return ibanToAccountMap.get(iban); // Returns null if not found
    }

    public boolean containsIban(String iban) {
        return ibanToAccountMap.containsKey(iban);
    }

    // Card -> Account mapping
    public void addCardToAccount(Card card, Account account) {
        if (card != null && account != null) {
            cardToAccountMap.put(card, account);
        }
    }

    public Account getAccountForCard(Card card) {
        return cardToAccountMap.get(card); // Returns null if not found
    }

    public boolean containsCard(Card card) {
        return cardToAccountMap.containsKey(card);
    }

    // CardNumber -> Card mapping
    public void addCardNumberToCard(String cardNumber, Card card) {
        if (cardNumber != null && card != null) {
            cardNumberToCardMap.put(cardNumber, card);
        }
    }

    public Card getCardForCardNumber(String cardNumber) {
        return cardNumberToCardMap.get(cardNumber); // Returns null if not found
    }

    public boolean containsCardNumber(String cardNumber) {
        return cardNumberToCardMap.containsKey(cardNumber);
    }
}

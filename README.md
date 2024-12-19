# J. POO Morgan Chase & Co.
 A program designed to handle given commands like: account creation, transactions, payments, and report generation for them.


## Project classes

### Commands

**Command** is the interface that all the commands implement. The command design pattern is used to execute the given commands gracefully, every command having its own class.

- **AddAccount.java**: Adds a new account to a user.
- **AddFunds.java**: Adds funds to an account.
- **CheckCardStatus.java**: Checks the status of a user's card (the frozen field) and blocks the card permanently if the card's balance is under the minimum balance.
- **SetAlias.java**: Sets an alias for used for account identification. A Hashmap is used for mapping the alias to an IBAN.
- **SetMinBalance.java**: Sets a minimum balance for an account. If the balance is below this and the checkCardStatus command is executed on a card, the card freezes.

#### **Create**
- **CreateCard.java**: Creates a standard card for an account.
- **CreateOneTimeCard.java**: Creates a one-time-use card for an account.

#### **Delete**
- **DeleteAccount.java**: Deletes a user’s account.
- **DeleteCard.java**: Deletes a user’s card.

#### **Interest**
- **AddInterest.java**: Adds interest to a user’s savings account. If the account isn't a savings account, an error is outputted.
- **ChangeInterestRate.java**: Changes the interest rate for a user’s account, the same error is outputted if the account is not a savings one.

#### **Payment**
These classes rely on GraphExchange to properly convert one currency to another. All of them output errors if they fail, and place their respective transactions in the user and in the account:
- **PayOnline.java**: Online payments with commerciants, only 1 card is used.
- **SendMoney.java**: Enables money transfers between accounts.
- **SplitPayment.java**: Allows users to split a payment among multiple accounts. In case of failure, all of the accounts get an error

#### **Print**
- **PrintTransactions.java**: Outputs the transaction history of a user.
- **PrintUsers.java**: Outputs the details of all the users.

#### **Reports**
- **Report.java**: This command is outputting the transactions of an account between 2 specific timestamps. Different transactions are chosen depending on what account is used. Through the entirety of the program the transactions are being placed in the transactions fields of the account objects and this is where it is being put to use.
- **SpendingsReport.java**: A report only for classic accounts that outputs the transactions and how much has been spent on every commerciant. This is achieved with the internal class CommerciantPayments which contains a TreeMap that maps the name of the commerciant to the amount they have at a certain moment in a sorted fashion (by name). whenever the add method is called again, the value increases by the amount it was called with.


### Exchange

- **Exchange.java**: Holds information about the exchanges.

### Graph

- **ExchangeGraph.java**: Has a graph structure that adds different currencies as nodes and contains a convertCurrency method which uses Djikstra's algorithm to find the shortest path between 2 currencies, calculating sequentially the converted amount by multiplying the current amount by the weight of the edge.

### Execution

- **Execute.java**: The main class in this program. It is the invoker of the command design pattern.



### Factories

- **AccountFactory.java**: The factory design pattern is used for creating the two different types of accounts.



### Mapper

- **Mappers.java**: Provides mappings between the classes used, such as users and accounts and ibans. The HashMaps used are for: 
- **acount -> user**
- **email -> user**
- **iban -> account**

### UserDetails

The `userDetails` package contains the the information-holding classes:

#### **User.java**
Defines the user entity, which can have multiple accounts each with multiple cards.

#### **Account**
- **Account.java**: Abstract class that helps in using polimorphism.
- **ClassicAccount.java**: A classic account type.
- **SavingsAccount.java**: An account that has an additional field interestRate which can change the account's ballance upon the execution of the addInterest command.

#### **Card**
- **Card.java**: Abstract base class for cards.
- **ClassicCard.java**: A Classic card.
- **OneTimeCard.java**: A one-time-use card that renews itself every time it used by destroying itself and reputting itself in the arraylist containing the cards inside the account.



### Main

- **Main.java**: The entry point that also converts the input classes to actual use classes.

### Design Patterns used:

- **Factory**: Inside the AccountFactory class.
- **Command**: All the commands in the commands package used by the invoker class Execute
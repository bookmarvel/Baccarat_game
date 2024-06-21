---
title: Baccarat game implementation
---
# Introduction

This document will walk you through the implementation of the Baccarat game feature. The feature was implemented in a way that separates the game logic, the dealer's actions, and the game's user interface into different classes. This separation of concerns makes the code more maintainable and easier to understand.

We will cover:

1. How the game logic is implemented.


2. How the dealer's actions are implemented.


3. How the game's user interface is implemented.


4. How the game is tested.

# Game Logic Implementation

<SwmSnippet path="src/main/java/BaccaratGameLogic.java" line="6">

---

The game logic is implemented in the <SwmToken path="/src/main/java/BaccaratGameLogic.java" pos="9:4:4" line-data="public class BaccaratGameLogic {">`BaccaratGameLogic`</SwmToken> class. This class contains methods that determine the winner of the game, calculate the total score for a hand, and determine if the player or the banker should draw a third card. These methods are essential for the game to function correctly.

```
/*! 
    @brief Implements several methods for working with the game logic for the Baccarat game.
*/
public class BaccaratGameLogic {
```

---

</SwmSnippet>

<SwmSnippet path="/src/main/java/BaccaratGameLogic.java" line="11">

---

&nbsp;

```java
    /*! @brief Determines who won the game of Baccarat.
        @param hand1 The player's hand.
        @param hand2 The banker's hand.
        @returns Either "Player", "Banker", or "Draw", depending on the result of the game.
    */
    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2){
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratGameLogic.java" line="28">

---

&nbsp;

```
    /*! @brief Calculates the total score for a hand according to the Baccarat rules.
        @param hand The hand to calculate the score with.
        @returns The calculated score.
    */
    public int handTotal(ArrayList<Card> hand){
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratGameLogic.java" line="43">

---

&nbsp;

```
    /*! @brief Determines if the banker should draw a third card.
        @param hand The banker's hand.
        @param playerCard The card drawn by the player. If the player did not draw a card, this argument is null.
        @returns True if the banker should draw a third card and false otherwise.
    */
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratGameLogic.java" line="69">

---

&nbsp;

```
    /*! @brief Determines if the player should draw a third card.
        @param hand The player's hand.
        @returns True if the player should draw a third card and false otherwise.
    */
    public boolean evaluatePlayerDraw(ArrayList<Card> hand){
```

---

</SwmSnippet>

# Dealer's Actions Implementation

<SwmSnippet path="src/main/java/BaccaratDealer.java" line="8">

---

The dealer's actions are implemented in the <SwmToken path="/src/main/java/BaccaratDealer.java" pos="11:4:4" line-data="public class BaccaratDealer {">`BaccaratDealer`</SwmToken> class. This class contains methods that generate a new deck of cards, deal the first two cards of the deck, draw the top card off the deck, and shuffle the deck. These actions are necessary for the game to progress.

```
/*!
    @brief A class to act as the dealer in the game Baccarat.
*/
public class BaccaratDealer {
    ArrayList<Card> deck; /**< The deck of playing cards, stored in an ArrayList. */
```

---

</SwmSnippet>

<SwmSnippet path="/src/main/java/BaccaratDealer.java" line="14">

---

&nbsp;

```java
    /*! 
        Generates a new standard 52-card deck of Card objects.
    */
    public void generateDeck() {
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratDealer.java" line="39">

---

&nbsp;

```
    /*! 
        Deals first two cards of the deck and returns them in an ArrayList.
    */
    public ArrayList<Card> dealHand() {
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratDealer.java" line="55">

---

&nbsp;

```
    /*! 
        Draws the top card off the deck and returns it.
    */
    public Card drawOne() {
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratDealer.java" line="66">

---

&nbsp;

```
    /*! 
        Generates a new deck of 52 cards and randomizes the order.
    */
    public void shuffleDeck() {
```

---

</SwmSnippet>

<SwmSnippet path="/src/main/java/BaccaratDealer.java" line="75">

---

&nbsp;

```java
    /*! 
        Returns the current size of the deck.
    */
    public int deckSize() {
```

---

</SwmSnippet>

# Game's User Interface Implementation

<SwmSnippet path="/src/main/java/BaccaratGame.java" line="35">

---

The game's user interface is implemented in the <SwmToken path="/src/main/java/BaccaratGame.java" pos="40:4:4" line-data="public class BaccaratGame extends Application {">`BaccaratGame`</SwmToken> class. This class contains methods that create and configure the UI elements for the start scene, the betting scene, and the play scene. It also contains methods that reset these scenes for the next round and evaluate the amount won or lost. These methods provide the player with a visual interface to interact with the game.

```java
/*! 
	@brief The class to play the game of Baccarat.
	Contains the code required to create the UI elements and call the 
			functions from the BaccaratGameLogic class.
*/
public class BaccaratGame extends Application {
	ArrayList<Card> playerHand; /**< The player's hand of cards, stored in an ArrayList. */
	ArrayList<Card> bankerHand; /**< The banker's hand of cards, stored in an ArrayList. */
	BaccaratDealer theDealer; /**< An instace of the BaccaratDealer class to call the dealer functions. */
	BaccaratGameLogic gameLogic; /**< An instance of the BaccaratGameLogic class to call the logic functions. */
	double currentBet; /**< How much money the player has currently bet on the round. */
	double totalWinnings; /**< How much money the player has won (or lost) over the course of the game so far. */
	String betPlacedOn; /**< Who the player has placed their bet on. Either "Player", "Banker", or "Draw". */
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratGame.java" line="382">

---

&nbsp;

```
	/*! 
		@brief The code to create and configure the UI elements for the betting scene. 
	*/
	public Scene createBettingScene() {
```

---

</SwmSnippet>

<SwmSnippet path="/src/main/java/BaccaratGame.java" line="468">

---

&nbsp;

```java
	/*! 
		@brief The code to create and configure the UI elements for the play scene. 
	*/
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratGame.java" line="577">

---

&nbsp;

```
	/*! 
		@brief Evaluates the amount won or lost. 
		@returns The winnings. A loss is represented as winning a negative amount.
	*/
	public double evaluateWinnings(){
```

---

</SwmSnippet>

<SwmSnippet path="src/main/java/BaccaratGame.java" line="600">

---

&nbsp;

```
	/*! 
		@brief Called whenever a round ends.  
		Evaluates the winnings, displays the result, and sets up button for replaying the game.
	*/
	public void endRound(){
```

---

</SwmSnippet>

# Game Testing

The game is tested using JUnit tests. These tests are located in the <SwmToken path="/src/test/java/BaccaratDealerTest.java" pos="21:4:4" line-data="public class BaccaratDealerTest {">`BaccaratDealerTest`</SwmToken>, <SwmToken path="/src/test/java/BaccaratGameLogicTest.java" pos="13:4:4" line-data="public class BaccaratGameLogicTest {">`BaccaratGameLogicTest`</SwmToken>, and <SwmToken path="/src/test/java/MyTest.java" pos="20:2:2" line-data="class MyTest {">`MyTest`</SwmToken> classes. They test the functionality of the methods in the <SwmToken path="/src/main/java/BaccaratDealer.java" pos="11:4:4" line-data="public class BaccaratDealer {">`BaccaratDealer`</SwmToken>, <SwmToken path="/src/main/java/BaccaratGameLogic.java" pos="9:4:4" line-data="public class BaccaratGameLogic {">`BaccaratGameLogic`</SwmToken>, and <SwmToken path="/src/main/java/BaccaratGame.java" pos="40:4:4" line-data="public class BaccaratGame extends Application {">`BaccaratGame`</SwmToken> classes, respectively. These tests ensure that the game functions as expected and help catch any bugs or issues.

<SwmMeta version="3.0.0" repo-id="Z2l0aHViJTNBJTNBQmFjY2FyYXRfZ2FtZSUzQSUzQWJvb2ttYXJ2ZWw="><sup>Powered by [Swimm](https://app.swimm.io/)</sup></SwmMeta>

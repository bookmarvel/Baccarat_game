/*!
    @file BaccaratGameLogic.java
*/
import java.util.ArrayList;

/*! 
    @brief Implements several methods for working with the game logic for the Baccarat game.
*/
public class BaccaratGameLogic {

    /*! @brief Determines who won the game of Baccarat.
        @param hand1 The player's hand.
        @param hand2 The banker's hand.
        @returns Either "Player", "Banker", or "Draw", depending on the result of the game.
    */
    public String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2){
        int playHandTotal = handTotal(hand1);
        int bankerHandTotal = handTotal(hand2);

        if(playHandTotal > bankerHandTotal)
            return "Player";
        else if(bankerHandTotal > playHandTotal)
            return "Banker";
        else
            return "Draw";
    }

    /*! @brief Calculates the total score for a hand according to the Baccarat rules.
        @param hand The hand to calculate the score with.
        @returns The calculated score.
    */
    public int handTotal(ArrayList<Card> hand){
        int total = 0;
        for (Card card : hand){
            //Any card with a value >= 10 counts as 0 in Baccarat
            if(card.value < 10)
                total += card.value;
        }
        //Only the last digit of the score matters in Baccarat
        return total % 10;
    }

    /*! @brief Determines if the banker should draw a third card.
        @param hand The banker's hand.
        @param playerCard The card drawn by the player. If the player did not draw a card, this argument is null.
        @returns True if the banker should draw a third card and false otherwise.
    */
    public boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){
        if (playerCard == null)
            return handTotal(hand) < 6;
        switch (playerCard.value){
            case 2:
            case 3:
                return handTotal(hand) < 5;
            case 4:
            case 5:
                return handTotal(hand) < 6;
            case 6:
            case 7:
                return handTotal(hand) < 7;
            case 8:
                return handTotal(hand) < 2;
            //Handles cards with a value of 0, 1, or 9
            default:
                return handTotal(hand) < 4;
        }
    }

    /*! @brief Determines if the player should draw a third card.
        @param hand The player's hand.
        @returns True if the player should draw a third card and false otherwise.
    */
    public boolean evaluatePlayerDraw(ArrayList<Card> hand){
        return handTotal(hand) < 6;
    }
}

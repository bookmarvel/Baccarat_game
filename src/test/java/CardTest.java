/*!
    @file CardTest.java
*/

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*!
    @brief Contains the code to test the BaccaratGameLogic class and methods.
*/
public class CardTest {
    @Test
    /*!
        @brief Tests that the Card constructor is working correctly.
    */
    void CardConstructor(){
        Card firstCard = new Card("Hearts", 12);
        Card secondCard = new Card("Spades", 1);

        assertEquals(firstCard.suite, "Hearts");
        assertEquals(firstCard.value, 12);
        assertEquals(secondCard.getSuite(), "Spades");
        assertEquals(secondCard.getValue(), 1);
    }
}

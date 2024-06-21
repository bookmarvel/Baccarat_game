/*!
    @file Card.java
*/


/*!
    @brief A custom class to hold information about the cards.
*/
public class Card {

    String suite; /**< Hearts, Diamonds, Clubs, or Spades. */
    int value; /**< Integers from 1-13. */

    
    public Card(String theSuite, int theValue) {
        this.suite = theSuite;
        this.value = theValue;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

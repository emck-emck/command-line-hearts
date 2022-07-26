//This class doesn't do anything too special, but it creates the type "card", which is a type that is used everywhere in the program.

package hearts;

public class Card {
    public int val;
    public String rank;
    public String suit;
    
    public Card(){
        rank = "Null";
        suit = "Null";
        val = -1;
    }
    
    public String name(){
        String name = rank + " of " + suit;
        return name;
    }
}
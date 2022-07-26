package src.hearts;

public class Card {
    int val;
    public Card(int v){
        this.val = v;
    }

    @Override
    public String toString(){
        return this.getWordValLong();
    }

    public int getSuit(){
        return (int)(this.val/Main.NUM_RANKS);
    }

    // Returns the card value in words (for printing and input check)
    public String getWordValLong(){
        
        int suit = this.getSuit();
        int rank = this.val % Main.NUM_RANKS; // There are 4 suits
        String ssuit = null;
        String srank = null;

        // Gets the String value of the suit
        switch(suit){
            case 0:
                ssuit = "Clubs";
                break;
            case 1:
                ssuit = "Diamonds";
                break;
            case 2:
                ssuit = "Hearts";
                break;
            case 3:
                ssuit = "Spades";
                break;
            default:
                ssuit = "Your";
        }

        // Gets the string value of the rank
        switch(rank){
            case 0:
                srank = "2";
                break;
            case 1:
                srank = "3";
                break;
            case 2:
                srank = "4";
                break;
            case 3:
                srank = "5";
                break;
            case 4:
                srank = "6";
                break;
            case 5:
                srank = "7";
                break;
            case 6:
                srank = "8";
                break;
            case 7:
                srank = "9";
                break;
            case 8:
                srank = "10";
                break;
            case 9:
                srank = "Jack";
                break;
            case 10:
                srank = "Queen";
                break;
            case 11:
                srank = "King";
                break;
            case 12:
                srank = "Ace";
                break;
            default:
                srank = "Mom";
        }

        return srank + " of " + ssuit;
    }

    // Returns the card value abbreviated in symbols (for input checking)
    public String getWordValShort(){
        
        int suit = this.getSuit();
        int rank = this.val % Main.NUM_RANKS; // There are 4 suits
        String ssuit = null;
        String srank = null;

        // Gets the String value of the suit
        switch(suit){
            case 0:
                ssuit = "c";
                break;
            case 1:
                ssuit = "d";
                break;
            case 2:
                ssuit = "h";
                break;
            case 3:
                ssuit = "s";
                break;
        }

        // Gets the string value of the rank
        switch(rank){
            case 0:
                srank = "2";
                break;
            case 1:
                srank = "3";
                break;
            case 2:
                srank = "4";
                break;
            case 3:
                srank = "5";
                break;
            case 4:
                srank = "6";
                break;
            case 5:
                srank = "7";
                break;
            case 6:
                srank = "8";
                break;
            case 7:
                srank = "9";
                break;
            case 8:
                srank = "10";
                break;
            case 9:
                srank = "J";
                break;
            case 10:
                srank = "Q";
                break;
            case 11:
                srank = "K";
                break;
            case 12:
                srank = "A";
                break;
        }

        return srank + ssuit;
    }
}

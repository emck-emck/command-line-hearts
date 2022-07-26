package src.hearts;
import java.io.Console;
import java.util.ArrayList;

public class Player {
    int player_id;
    int points;
    int matchPoints;
    ArrayList<Card> cards;
    boolean isAI;

    public Player(int n){
        this.player_id = n + 1;
        this.points = 0;
        this.matchPoints = 0;
        this.cards = null;
        if(this.player_id != 1){
            this.isAI = true;
        }else{
            this.isAI = false;
        }
    }

    public void sortCards(){
        this.cards.sort((c1, c2) -> c1.val - c2.val);
    }

    private void printHand(){
        System.out.println();
        for(Card cs: this.cards){
            System.out.println(cs.getWordValLong());
        }
        System.out.println();
    }

    // Method called by Match.java when a player plays a card
    public Card playCard(Card lead, boolean isHeartsBroken, int trickCount){
        if(this.isAI){
            return aiPlayCard(lead, isHeartsBroken, trickCount);
        }else{
            return pPlayCard(lead, isHeartsBroken, trickCount);
        }
    }

    // Play card for human (command line input, input sanity, all that jazz)
    private Card pPlayCard(Card lead, boolean isHeartsBroken, int trickCount){
        boolean isValid = false;
        String input;
        Card c = null;

        printHand();

        while(!isValid){
            input = "";
            System.out.println();
            System.out.println("Play card");
            while(input == ""){
				System.console().flush();
                input = System.console().readLine();
            }
            input = input.toLowerCase();
            System.out.println();

            // Check for player quit command
            if(input.equals("quit")) return null;
            
            // Check player cards for card matching input
            for(Card x: this.cards){
                if(input.equals(x.getWordValShort().toLowerCase()) || input.equals(x.getWordValLong().toLowerCase())){
                    c = x;
                }
            }
            if(c == null){
                System.out.println("Invalid input, try rank of suit and name of suit (ex. 2c for 2 of Clubs)");
                continue;
            }

            // Check to make sure the two of clubs isn't in this hand
            if(lead == null && c.val != Main.TWO_OF_CLUBS){
                if(this.cards.get(0).val == Main.TWO_OF_CLUBS){
                    System.out.println("Invalid, first hand must lead 2 of clubs");
                    c = null;
                    continue;
                }
            }

            // Check that card is following led suit (unless they don't have that suit)
            if(lead != null){
                if(!(c.getSuit() == lead.getSuit())){
                    // Search for that suit within the hand
                    ArrayList<Card> search = (ArrayList<Card>) this.cards.clone();
                    search.removeIf(s -> (s.getSuit() != lead.getSuit()));
                    // If we found one, input is invalid
                    if(!search.isEmpty()){
                        System.out.println("Invalid, card must match led suit");
                        c = null;
                        continue;
                    }
                }
            }

            // If card played is hearts, make sure it's not the first turn
            if((c.getSuit() == Main.HEART_SUIT_VAL || c.val == Main.QUEEN_OF_SPADES) && trickCount == 0){
                System.out.println("Invalid, points cannot be played on the first turn");
                c = null;
                continue;
            }

            // If card is the lead and is hearts, check that hearts is broken
            // Check also whether the person has only hearts in their hand (valid if)
            if(lead == null && c.getSuit() == Main.HEART_SUIT_VAL){ // If card is the lead and is hearts
                if(!isHeartsBroken){ // If hearts isn't broken
                    ArrayList<Card> search = (ArrayList<Card>) this.cards.clone(); 
                    search.removeIf(s -> (s.getSuit() == Main.HEART_SUIT_VAL)); // Search hand for card other than hearts
                    if(!search.isEmpty()){ // If found, it's invalid
                        System.out.println("Invalid, hearts isn't broken yet");
                        c = null;
                        continue;
                    }
                }
            }

            break;
        }
        
        return c;
    }

    // Logic for AI to play game (is extremely primitive)
    private Card aiPlayCard(Card lead, boolean isHeartsBroken, int trickCount){

        if(lead == null){ // If it's AI's lead
            // Locate 2 of clubs
            // If it's found, return it immediately
            if(this.cards.get(0).val == Main.TWO_OF_CLUBS)
                return this.cards.get(0);

            if(isHeartsBroken){
                // Play a random card
                int rando = (int)(Math.floor(Math.random() * this.cards.size()));
                return this.cards.get(rando);
            }else{
                // Play a random card (that's not hearts)
                // Filter
                ArrayList<Card> search = (ArrayList<Card>) this.cards.clone();
                search.removeIf(c -> ((int)(c.val/Main.NUM_RANKS) == Main.HEART_SUIT_VAL));
                // Select
                int rando = (int)(Math.floor(Math.random() * search.size()));
                return search.get(rando);
            }

        }else if(trickCount == 0){ // If it's someone else's lead and it's the fist turn (hearts can't be broken yet)
            // Find cards of led suit in hand
            ArrayList<Card> search = (ArrayList<Card>) this.cards.clone();
            search.removeIf(c -> !((int)(c.val/Main.NUM_RANKS) == lead.getSuit()));

            // If cards were found
            if(!search.isEmpty()){
                // Play a random card from the suit
                int rando = (int)(Math.floor(Math.random() * search.size()));
                return search.get(rando);
            }else{ // Else
                // Play a random card
                int rando = (int)(Math.floor(Math.random() * this.cards.size()));
                return this.cards.get(rando);
            }            

        }else{ // If someone else led this trick
            // Find cards of led suit in hand
            ArrayList<Card> search = (ArrayList<Card>) this.cards.clone();
            search.removeIf(c -> !((int)(c.val/Main.NUM_RANKS) == lead.getSuit()));

            // If cards were found
            if(!search.isEmpty()){
                // Play a random card from the suit
                int rando = (int)(Math.floor(Math.random() * search.size()));
                return search.get(rando);
            }else{ // Else
                // Search for queen of spades
                search = (ArrayList<Card>) this.cards.clone();
                search.removeIf(c -> !(c.val == Main.QUEEN_OF_SPADES));
                // If found play it
                if(!search.isEmpty())
                    return search.get(0);

                // Search for hearts
                search = (ArrayList<Card>) this.cards.clone();
                search.removeIf(c -> !((int)(c.val/Main.NUM_RANKS) == Main.HEART_SUIT_VAL));
                // play the highest if found
                if(!search.isEmpty())
                    return search.get(search.size() - 1);
                // Play a random card
                int rando = (int)(Math.floor(Math.random() * this.cards.size()));
                return this.cards.get(rando);
            }            
        }
    }

}

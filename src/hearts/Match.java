package src.hearts;
import java.util.ArrayList;
import java.util.Arrays;

// Contains the logic for a single round of the game
public class Match {
    public int match(Player[] p){
        // Variables
        int lead;
        int turn;
        int trickCount;
        boolean isHeartsBroken;

        // Initialize match (deal cards)
        lead = initMatch(p); // Tracks whose turn it is (2 of clubs starts)
        // Sort cards
        for(Player ps: p){
            ps.sortCards();
        }
        isHeartsBroken = false;
        trickCount = 0;
        
        // Game loop (ends when players are out of cards)
        while(!p[0].cards.isEmpty()){
            Card cardLead = null;
            Card[] trick = new Card[Main.PLAYERS_IN_GAME]; // Remembers which cards everyone played
            turn = lead;
            

            // Logic for the hands played in a trick
            for(int x = 0; x < Main.PLAYERS_IN_GAME; x++){
                Card played = null;
                turn = (lead + x) % Main.PLAYERS_IN_GAME; // Tracks whose turn it is
                played = p[turn].playCard(cardLead, isHeartsBroken, trickCount); // playCard is where the logic for playing happens
                if(played == null){ // Happens if player types "quit"
                    return 0;
                }
                System.out.println("Player " + String.valueOf(p[turn].player_id) + " played " + played);
                if(cardLead == null) cardLead = played;
                trick[turn] = played; // Save the card played
                p[turn].cards.remove(played); // Remove the played card from player's hand
            }

            // Figure out who won the trick
            Card win;
            win = winsTrick(trick, cardLead); // Determine card that won

            // Determine whose card it was
            int x;
            for(x = 0; x < trick.length; x++){
                if(win.val == trick[x].val){
                    break;
                }
            }

            // Determine if hearts was broken
            if(!isHeartsBroken){
                isHeartsBroken = checkHeartsBroken(trick);
            }

            // Tally points in trick
            tallyPoints(trick, p[x]);

            // Make trick winner the leader
            lead = x;

            trickCount++; // Increment the number of tricks played

            // Print trick results
            System.out.println();
            System.out.println("Player " + String.valueOf(p[lead].player_id) + " wins the trick with the " + win);
            System.out.println();
        }

        // Check for if the moon was shot
        adjustShotMoon(p);

        return 1;
    }

    // Creates the hands used to play the match. Returns the index of the player holding the 2 of clubs (they lead)
    private int initMatch(Player[] p){
        // Holder Variables
        ArrayList<Integer> arr = new ArrayList<Integer>();
        int[] shuf = new int[Main.CARDS_IN_DECK];
        ArrayList<ArrayList<Card>> hands = new ArrayList<ArrayList<Card>>();
        int lead = -1;
        int x;

        // Create hands for cards to sit in
        for(x = 0; x < Main.PLAYERS_IN_GAME; x++){
            hands.add(new ArrayList<Card>());
        }

        // Generate cards
        for(x = 0; x < Main.CARDS_IN_DECK; x++){
            arr.add(x);
        }

        // Shuffle cards
        x = 0;
        while(!arr.isEmpty()){
            int rando = (int)Math.floor((Math.random() * arr.size()));
            shuf[x] = arr.remove(rando);
            x++;
        }

        // Deal cards
        x = 0;
        for(x = 0; x < Main.PLAYERS_IN_GAME; x++){
            for(int y = 0; y < (Main.CARDS_IN_DECK/Main.PLAYERS_IN_GAME); y++){
                int z = ((Main.CARDS_IN_DECK/Main.PLAYERS_IN_GAME) * x) + y;
                if(shuf[z] == 0) lead = x;
                hands.get(x).add((new Card(shuf[z])));
            }
        }

        // Assign cards to players
        for(x = 0; x < p.length; x++){
            p[x].cards = hands.get(x);
        }

        return lead;
    }

    // Function determines the card that won the trick
    public Card winsTrick(Card[] c, Card lead){
        ArrayList<Card> temp = new ArrayList<Card>(Arrays.asList(c)); // Create temp array
        temp.removeIf(s -> (s.getSuit() != lead.getSuit())); // Remove all of non-led suit
        temp.sort((s1, s2) -> s2.val - s1.val); // Find highest card in suit
        return temp.get(0); // Return
    }

    // Checks whether hearts was broken in that trick or not
    private boolean checkHeartsBroken(Card[] c){
        ArrayList<Card> temp = new ArrayList<Card>(Arrays.asList(c)); // Create temp array
        temp.removeIf(s -> (s.getSuit() != Main.HEART_SUIT_VAL)); // Remove all that aren't hearts

        // If there were hearts in the trick, hearts were broken.
        if(temp.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private void tallyPoints(Card[] trick, Player p){
        // Assign points for queen of spades in trick
        for (Card c: trick){
            if(c.val == Main.QUEEN_OF_SPADES){
                p.matchPoints = p.matchPoints + Main.QUEEN_OF_SPADES_VALUE;
            }
        }

        // Assign points for hearts in trick
        for (Card c: trick){
            if(c.getSuit() == Main.HEART_SUIT_VAL){
                p.matchPoints = p.matchPoints + Main.HEARTS_VALUE;
            }
        }
    }

    // Check if someone shot the moon and adjust the points accordingly
    // Shooting the moon means acquiring all the points possible in a current match
    private void adjustShotMoon(Player[] p){
        int x = 1;
        boolean isMoonShot = false;
        for(Player ps: p){
            if(ps.matchPoints == Main.TOTAL_POINTS_IN_MATCH){
                isMoonShot = true;
                break;
            }
            x++;
        }

        if(isMoonShot){
            for(Player ps: p){
                if(ps.player_id == x){
                    System.out.println("Player " + String.valueOf(ps.player_id) + " shot the moon!!");
                    ps.matchPoints = 0;
                }else{
                    ps.matchPoints = Main.TOTAL_POINTS_IN_MATCH;
                }
            }
        }
    }
}

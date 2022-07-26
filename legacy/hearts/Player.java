//This contains the AI for the game, and also contains some useful info about both the user and the AI.

package hearts;

public class Player {
    public Deck hand = new Deck(13);
    public final Methods methods = new Methods();
    public int id;
    public int points;
    public int tempPoints;
    private final String[] suit = {"Clubs", "Diamonds", "Hearts", "Spades"};
    
    Player(int num){
        id = num;
        points = 0;
        tempPoints = 0;
    }
    
    public void removeCard(Card c){
        int x = 0;
        while(x < hand.cards.length){
            if(c.val == hand.cards[x].val){
                hand.cards[x].val = 52;
                break;
            }
            x++; 
        }
    }
    
    public Deck tradeCards(){
        Deck d = new Deck(3);
        int[] numInSuit = new int[2];
        int cardsUsed = 0;
        int cardPos;
        int smallSuit;
        int searchVal;
        int x, y;
        
        for(searchVal = 49; searchVal < 52; searchVal++){
            cardPos = findCard(searchVal);
            if(cardPos!= -1){
                d.cards[cardsUsed] = hand.cards[cardPos];
                cardsUsed++;
            }
        }
        
        for(x = 0; x < 2; x++){
            numInSuit[x] = countInSuit(x);
        }
        smallSuit = findSmallestInt(numInSuit);
        if(numInSuit[smallSuit] <= (3 - cardsUsed)){
            for(y = 0; y < hand.cards.length; y++){
                if(hand.cards[y].suit.equals(suit[smallSuit])){
                    d.cards[cardsUsed] = hand.cards[y];
                    cardsUsed++;
                }
            }
        }

        searchVal = 38;
        while(cardsUsed < 3 && searchVal >= 0){
            cardPos = findCard(searchVal);
            if(cardPos!= -1){
                if(!hand.cards[cardPos].suit.equals(suit[smallSuit])){
                    d.cards[cardsUsed] = hand.cards[cardPos];
                    cardsUsed++;
                }
            }
            searchVal--;
        }
        
        return d;
    }
    
    
    public Card playCard(Card lead, Deck cardsPlayed){
        Card c;
        int s;
        
        hand.cards = hand.sortCards(hand.cards);
        if(Main.turn != 1){
            if(lead.val == -1){
                if(countInSuit(3) > 0 && lowestInSuit(3) < 10){
                    c = playLowestInSuit(3);
                }else if(Main.heartsBroken && countInSuit(2) > 0 
                        && lowestInSuit(2) < 7){
                    c = playLowestInSuit(2);
                }else if(countInSuit(0) > 0){
                    c = playLowestInSuit(0);
                }else if(countInSuit(1) > 0){
                    c = playLowestInSuit(1);
                }else if(onlyHearts()){
                    c = playLowestInSuit(2);
                    Main.heartsBroken = true;
                }else{
                    c = playLowestInSuit(3);
                }
            }else{
                s = findSuitVal(lead);
                if(countInSuit(s) == 0){
                    if(findCard(49) != -1){
                        c = hand.cards[(findCard(49))];
                    }else if(findCard(50) != -1){
                        c = hand.cards[(findCard(50))];
                    }else if(findCard(51) != -1){
                        c = hand.cards[(findCard(51))];
                    }else if(countInSuit(2) > 0){
                        c = playHighestInSuit(2);
                        Main.heartsBroken = true;
                    }else{
                        c = voidSuit();
                    }
                }else{
                    if(duck(lead, cardsPlayed) > -1){
                          c = hand.cards[(duck(lead, cardsPlayed))];
                    }else{
                          c = playLowestInSuit(s);
                    }   
                }
            }
        }else{
            if(lead.val == -1){
                c = hand.cards[0];
            }else{
                if(countInSuit(0) > 0){
                    c = playHighestInSuit(0);
                }else if(findCard(51) != -1){
                    c = hand.cards[(findCard(51))];
                }else if(findCard(50) != -1){
                    c = hand.cards[(findCard(50))];
                }else{
                    c = playHighestInSuit(1);
                }
            }
        }
        
        return c;
    }
    
    private Card playLowestInSuit(int s){
        int x;
        int pos;

        for(x = 0; x < 13; x++){
            pos = findCard(x + (13*s));
            if(pos != -1){
                return hand.cards[pos];
            }
        }
        
        return new Card();
    }
    
    private Card playHighestInSuit(int s){
        int x;
        int pos;
        
        for(x = (hand.cards.length - 1); x >= 0; x--){
            pos = findCard(x + (13*s));
            if(pos != -1){
                return hand.cards[pos];
            }
        }
        
        return new Card();
    }
    
    private Card voidSuit(){
        int s;
        int[] numInSuit = new int[4];
        
        for(s = 0; s < suit.length; s++){
            numInSuit[s] = countInSuit(s);
        }
        s = -1;
        while(s == -1){
            s = findSmallestInt(numInSuit);
            if(numInSuit[s] == 0){
                numInSuit[s] = 99;
                s = -1;
            }
        }
        
        return playHighestInSuit(s);
    }
    
    private int findCard(int val){
        int x;
        
        x = 0;
        while(x < hand.cards.length){
            if(hand.cards[x].val == val){
                return x;
            }
            x++;
        }
        
        return -1;
    }
    
    private int duck(Card lead, Deck cardsPlayed){
        int x;
        int highest;
        
        highest = methods.determineLeader(cardsPlayed, lead);
        highest = cardsPlayed.cards[highest].val;
        
        x = hand.cards.length - 1;
        
        while(x >= 0){
            if(hand.cards[x].suit.equals(lead.suit) &&
                    hand.cards[x].val < highest){
                break;
            }
            x--;
        }
        return x;
    }
    
    private int lowestInSuit(int s){
        int x;
        
        for(x = 0; x < hand.cards.length; x++){
            if(hand.cards[x].suit.equals(suit[s])){
                return hand.cards[x].val - (13*s);
            }
        }
        
        return -1;
    }
    
    private int countInSuit(int s){
        int x;
        int count;
        
        count = 0;
        for(x = 0; x < hand.cards.length; x++){
            if(hand.cards[x].suit.equals(suit[s])){
                count++;
            }
        }
        
        return count;
    }
    
    private int findSmallestInt(int[] i){
        int x;
        int smallest = 999999;
        int pos = -1;
        for(x = 0; x < i.length; x++){
            if(i[x] < smallest){
                smallest = i[x];
                pos = x;
            }
        }
        return pos;
    }
    
    private boolean onlyHearts(){
        boolean onlyHearts = true;
        int x = 0;
        
        while(x < hand.cards.length){
            if(hand.cards[x].suit.equals("Hearts")){
                onlyHearts = false;
                break;
            }
            x++;
        }
        
        return onlyHearts;
    }
    
    private int findSuitVal(Card c){
        int x;
        
        for(x = 0; x < suit.length; x++){
            if(c.suit.equals(suit[x])){
                return x;
            }
        }
        
        return -1;
    }
}
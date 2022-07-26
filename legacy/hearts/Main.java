//This is the main class, everything gets controlled here

package hearts;
import java.util.Scanner;
public class Main {
    static Methods methods = new Methods();
    public static int turn;
    public static boolean heartsBroken;
    
    public static void main(String[] args){
        int mover;
        int shift;
        int x;
        boolean isGameOver;
        
        Player[] p = new Player[4];
        Deck d = new Deck(52);
        Deck trick;
        Card lead = new Card();
        Scanner s = new Scanner(System.in);
        
        for(x = 0; x < p.length; x++){
            p[x] = new Player(x);
        }
        
        System.out.println("Welcome to CLI Hearts!");
        System.out.println();

        isGameOver = false;
        shift = 1;
        while(!isGameOver){
            p = init(p, d);
//            printAll(p);
//            mover = tradeCards(p, s, shift);
            mover = methods.find2c(p);
            printAll(p);
            for(x = 0; x < 13; x++){
                trick = playHand(p, s, lead, mover);
                mover = finishHand(p, trick, mover);
            }
            isGameOver = finishRound(p);
            shift = methods.rotate(shift);
        }
        finishGame(p);
    }
    
    private static Player[] init(Player[] p, Deck d){
        int x;
        turn = 1;
        heartsBroken = false;
        
        for(x = 0; x < p.length; x++){
            p[x].tempPoints = 0;
        }
        
        d.initCards();
        d.cards = d.shuffle(d.cards);
        p = d.dealCards(p, d.cards);
        //p = d.stackDeck(p, d.cards);
        for(x = 0; x < p.length; x++){
            p[x].hand.cards = p[x].hand.sortCards(p[x].hand.cards);
        }
        System.out.println("Time for a new hand!");
        System.out.println();
        
        return p;
    }

    private static int tradeCards(Player[] p, Scanner s, int shift){
        int x, y;
        int returnCode;
        int mover;
        String input;
        Card temp = new Card();
        Card[] c = new Card[3];
        Deck[] cardsToTrade = new Deck[4];
        
        for(x = 0; x < cardsToTrade.length; x++){
            cardsToTrade[x] = new Deck(3);
        }
        
        methods.getShiftMsg(shift);
        if(shift > 0){
            for(x = 0; x < p.length; x++){
                if(x != 0){
                    cardsToTrade[x] = p[x].tradeCards();
                }else{
                    printHand(p[x]);
                    for(y = 0; y < c.length; y++){
                        c[y] = new Card();
                    }
                    while((c[0].val == -1 || c[1].val == -1 || c[2].val == -1)){
                        returnCode = -1;
                        while(returnCode != 0){
                            input = s.nextLine();
                            System.out.println();
                            returnCode = methods.shiftVal(input, p[x]);
                            if(returnCode != 0){
                                methods.getCodeMsg(returnCode);
                            }else{
                                temp = methods.getCard(input);
                            }
                        }
                        methods.fixTradeArray(temp, c);
                        printTradeHand(p[x], c);
                    }
                    for(y = 0; y < cardsToTrade[x].cards.length; y++){
                        cardsToTrade[x].cards[y] = c[y];
                    }
                }
            }
            p = methods.shiftCards(p, cardsToTrade, shift);
        }
        
        mover = methods.find2c(p);
        System.out.println("Player " + (mover + 1) + " plays first.");
        System.out.println();
        return mover;
    }
    
    private static Deck playHand(Player[] p, Scanner s, Card lead, int mover){
        Deck trick = new Deck(4);
        int x;
        lead = new Card();
        
        for(x = 0; x < p.length; x++){
            trick.cards[mover] = playCard(p[mover], s, lead, trick);
            if(lead.suit.equals("Null")){
                lead = trick.cards[mover];
            }
            mover = methods.rotate(mover);
        }
        
        System.out.println();
        
        return trick;
    }
    
    private static Card playCard(Player p, Scanner s, Card lead, Deck cardsPlayed){
        int x;
        int returnCode = -1;
        int pos = -1;
        String input;
        Card c = new Card();
        
        if(p.id != 0){
            c = p.playCard(lead, cardsPlayed);
        }else{
            System.out.println();
            p.hand.cards = p.hand.sortCards(p.hand.cards);
            printHand(p);        

            while(returnCode != 0){
                input = s.nextLine();
                System.out.println();
                returnCode = methods.inputVal(input, p, lead);
                if(returnCode != 0){
                    methods.getCodeMsg(returnCode);
                }else{
                    c = methods.getCard(input);
                }
            }
        }
        System.out.println("Player " + (p.id + 1) + 
                " plays the " + c.name());
        
        x = 0;
        while(x < p.hand.cards.length){
            if(c.val == p.hand.cards[x].val){
                pos = x;
                break;
            }
            x++;
        }
        try{
            return p.hand.cards[pos];
        }catch(ArrayIndexOutOfBoundsException err){
            System.out.println("Card not Found");
            System.out.println();
            return new Card();
        }
    }
    
    private static void finishGame(Player[] p){
        int[] finalPoints = new int[4];
        int winner;
        int x;
        
        for(x = 0; x < finalPoints.length; x++){
            finalPoints[x] = p[x].points;
        }
        winner = methods.findSmallestInt(finalPoints);
        
        System.out.println("That's the game, folks!");
        System.out.println();
        System.out.println("And the winner is...");
        System.out.println();
        System.out.println("Player " + (p[winner].id + 1) + "!!!");
        System.out.println();
        System.out.println("Congrats! Play again sometime!");
    }
    
    private static boolean finishRound(Player[] p){
        boolean isGameOver;
        p = methods.finalizePoints(p);
        printScore(p);
        isGameOver = checkGameOver(p);
        return isGameOver;
    }
    
    private static int finishHand(Player[] p, Deck trick, int mover){
        int x;
        mover = methods.determineLeader(trick, trick.cards[mover]);
        p[mover].tempPoints += methods.assignPoints(trick);
        System.out.println("Player " + (mover + 1) + " takes the trick with the "
                + trick.cards[mover].name());
        System.out.println();
        for(x = 0; x < p.length; x++){
            p[x].hand = removeCard(p[x], trick.cards[x]);
        }
        turn++;
        
        printAll(p);
        
        return mover;
    }
    
    private static Deck removeCard(Player p, Card c){
        int newLen = p.hand.cards.length - 1;
        int x;
        Deck newDeck = new Deck(newLen);
        
        p.removeCard(c);
        p.hand.sortCards(p.hand.cards);
        for(x = 0; x < newLen; x++){
            newDeck.cards[x] = p.hand.cards[x];
        }
        return newDeck;
    }
    
    private static boolean checkGameOver(Player[] p){
        int x;
        boolean isGameOver;
        
        x = 0;
        isGameOver = false;
        while(x < p.length){
            if(p[x].points >= 100){
                isGameOver = true;
                break;
            }
            x++;
        }
        
        return isGameOver;
    }
    
    private static void printHand(Player p){
        int x;

        System.out.println("Player " + (p.id + 1));
        System.out.println();
        for (x = 0; x < p.hand.cards.length; x++) {
            System.out.println(p.hand.cards[x].name());
        }
        System.out.println();
    }
    
    private static void printAll(Player[] p){
        for (Player p1 : p) {
            System.out.println("Player " + (p1.id + 1));
            System.out.println();
            for (Card card : p1.hand.cards) {
                System.out.println(card.name());
            }
            System.out.println();
        }
    }
    
    private static void printTradeHand(Player p, Card[] c){
        int x;
        int y;
        
        System.out.println("Player " + (p.id + 1));
        System.out.println();
        for (x = 0; x < p.hand.cards.length; x++) {
            y = 0;
            while(y < c.length){
                if(p.hand.cards[x].name().equals(c[y].name())){
                    System.out.print("    ");
                    break;
                }
                y++;
            }
            System.out.println(p.hand.cards[x].name());
        }
        System.out.println();
    }
    
    private static void printScore(Player[] p){
        int x;
        
        System.out.println("Here are the current standings");
        System.out.println();
        for(x = 0; x < p.length; x++){
            System.out.println("Player " + (p[x].id + 1) + ": " 
                    + p[x].points + " points");
        }
        System.out.println();
    }
    
}
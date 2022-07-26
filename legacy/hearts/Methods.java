//So this is the class in question, it has everything that I don't know what to do with. It's like a storage spot for methods, but it's really messy
//and I'd like to clean it up.

package hearts;

public class Methods {
        
    public Card getCard(String input){
        Card c = checkifValid(input);
        return c;
    }
    
    public int inputVal(String input, Player p, Card lead){
        int returnCode;
        boolean followsSuit;
        Card c;
        input = input.toLowerCase();
        input = input.replaceAll(" ", "");
        if(input.length() < 2){
            return -1;
        }else{
            c = checkifValid(input);
            followsSuit = followsSuit(c, p, lead);
            returnCode = getReturnCode(c, lead, p, followsSuit);
            return returnCode;
        }
    }
    
    public int shiftVal(String input, Player p){
        int returnCode;
        Card c;
        input = input.toLowerCase();
        input = input.replaceAll(" ", "");
        if(input.length() < 2){
            return -1;
        }else{
            c = checkifValid(input);
            returnCode = getShiftCode(c, p);
        }
        return returnCode;
    }
    
    private Card checkifValid(String input){
        int is10 = 0;
        int adder;
        int multiplier;
        char rank = input.charAt(0);
        char suit;
        String check;
        Card c = new Card();
        
        if(rank == 'j'){
            c.rank = "Jack";
            adder = 9;
        }else if(rank == 'q'){
            c.rank = "Queen";
            adder = 10;
        }else if(rank == 'k'){
            c.rank = "King";
            adder = 11;
        }else if(rank == 'a'){
            c.rank = "Ace";
            adder = 12;
        }else if(rank >= '2' && rank <= '9'){
            c.rank = "" + rank;
            adder = rank - 50;
        }else{
            check = "" + rank + input.charAt(1);
            if(check.equals("10")){
                c.rank = "10";
                is10 = 1;
                adder = 8;
            }else{
                c.rank = "!";
                adder = 52;
            }
        }
        suit = input.charAt(1 + is10);
        switch(suit){
            case 'c':
                c.suit = "Clubs";
                multiplier = 0;
                break;
            case 'd':
                c.suit = "Diamonds";
                multiplier = 1;
                break;
            case 'h':
                c.suit = "Hearts";
                multiplier = 2;
                break;
            case 's':
                c.suit = "Spades";
                multiplier = 3;
                break;
            default:
                c.suit = "!";
                multiplier = 4;
        }
        c.val = (13*multiplier) + adder;
        return c;
    }
    
    private boolean followsSuit(Card c, Player p, Card lead){
        boolean followsSuit;
        if(lead.suit.equals("Null")){
            followsSuit = true;
        }else if (lead.suit.equals(c.suit)){
            followsSuit = true;
        }else{
            followsSuit = isVoid(p, lead, c);
        }
        return followsSuit;
    }
    
    private boolean isVoid(Player p, Card lead, Card c){
        boolean isVoid = true;
        int x = 0;
        
        while(x < p.hand.cards.length){
            if(p.hand.cards[x].suit.equals(lead.suit)){
                isVoid = false;
                break;
            }
            x++;
        }
        if(Main.turn > 1){
            if(c.suit.equals("Hearts") && isVoid == true){
                Main.heartsBroken = true;
            }
        }
        
        
        return isVoid;
    }
    
    private boolean onlyHearts(Player p){
        boolean onlyHearts = true;
        int x = 0;
        
        while(x < p.hand.cards.length){
            if(!p.hand.cards[x].suit.equals("Hearts")){
                onlyHearts = false;
                break;
            }
            x++;
        }
        
        return onlyHearts;
    }
        
    private int getReturnCode(Card c, Card lead, Player p, boolean followsSuit){
        int x;
        int returnCode = 0;
        
        if(c.val > 51){
            returnCode = -1;
        }else{
            x = 0;
            while(x < p.hand.cards.length){
                if(c.name().equals(p.hand.cards[x].name())){
                    returnCode = 0;
                    if(Main.turn == 1 && lead.suit.equals("Null")){
                        if(c.val != 0){
                            returnCode = 3;
                        }
                    }else if(Main.turn == 1 && c.name().equals("Queen of Spades")){
                        returnCode = 6;
                    }else if(!followsSuit){
                        returnCode = 2;
                    }else if(!Main.heartsBroken && 
                        c.suit.equals("Hearts")){
                        if(onlyHearts(p)){
                            Main.heartsBroken = true;
                            returnCode = 0;
                        }else if(Main.turn == 1){
                             returnCode = 5;
                        }else{
                            returnCode = 4;
                        }
                    }
                    break;
                }else{
                    returnCode = 1;
                    x++;
                }
            }
        
        }
      return returnCode;
    }
    
    private int getShiftCode(Card c, Player p){
        int returnCode = -1;
        int x;
        if(c.val > 51){
            return -1;
        }else{
            x = 0;
            while(x < p.hand.cards.length){
                if(p.hand.cards[x].name().equals(c.name())){
                    returnCode = 0;
                    break;
                }else{
                    returnCode = 1;
                }
                x++;
            }
        }
        return returnCode;
    }
    
    public void getCodeMsg(int returnCode){
        switch(returnCode){
            case -1:
                System.out.println("That is not a valid input.");
                System.out.println("Please type the value of the rank followed "
                        + "by the first letter of the suit.");
                System.out.println();
                break;
            case 1:
                System.out.println("You do not have this card.");
                System.out.println();
                break;
            case 2:
                System.out.println("You need to follow suit. You cannot play "
                        + "this card");
                System.out.println();
                break;
            case 3:
                System.out.println("You must play the 2 of clubs to start"
                        + " the game.");
                System.out.println();
                break;
            case 4:
                System.out.println("Hearts hasn't been broken yet.");
                System.out.println();
                break;
            case 5:
                System.out.println("Hearts can't be broken on the first turn.");
                System.out.println();
                break;
            case 6:
                System.out.println("You can't play the queen of spades on the "
                        + "first turn.");
        }
    }
    
    public void getShiftMsg(int shift){
        switch(shift){
            case 0:
                System.out.println("You will not be trading cards this round");
                System.out.println();
                break;
            case 1:
                System.out.println("Select 3 cards that you will be trading to"
                        + " the player on your left");
                System.out.println();
                break;
            case 2:
                System.out.println("Select 3 cards that you will be trading to"
                        + " the player across from you");
                System.out.println();
                break;
            case 3:
                System.out.println("Select 3 cards that you will be trading to"
                        + " the player on your right");
                System.out.println();
                break;
            default:
                System.out.println("Mistakes were made");
        }
    }
    
    public int rotate(int prev){
        if(prev < 3){
            prev++;
        }else{
            prev = 0;
        }
        return prev;
    }
    
    public int determineLeader(Deck d, Card lead){
        int returnVal;
        int x;
        Deck temp = new Deck(d.cards.length);

        for(x = 0; x < temp.cards.length; x++){
            temp.cards[x].val = d.cards[x].val;
            temp.cards[x].suit = d.cards[x].suit;
        }
        
        for(x = 0; x < temp.cards.length; x++){
            if(lead.suit.equals(temp.cards[x].suit)){
                temp.cards[x].val += 52;
            }
        }
        
        returnVal = findBiggestInt(temp);

        return returnVal;
    }
    
    private int findBiggestInt(Deck d){
        int x;
        int biggest = -1;
        int pos = -1;
        for(x = 0; x < d.cards.length; x++){
            if(d.cards[x].val > biggest){
                biggest = d.cards[x].val;
                pos = x;
            }
        }
        return pos;
    }
    
    public int findSmallestInt(int[] i){
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
    
    public int countPoints(Deck trick){
        int x;
        int points = 0;
        for(x = 0; x < trick.cards.length; x++){
            if(trick.cards[x].suit.equals("Hearts")){
                points++;
            }else if(trick.cards[x].val == 49){
                points+= 13;
            }
        }
        return points;
    }
    
    public void fixTradeArray(Card temp, Card[] c){
        int x;
        boolean erases = false;
        x = 0;
        while(x < c.length){
            if(c[x].name().equals(temp.name())){
                c[x] = new Card();
                erases = true;
                break;
            }
            x++;
        }
        if(erases == false){
            x = 0;
            while(x < c.length){
                if(c[x].val == -1){
                    c[x] = temp;
                    break;
                }
                x++;
            }
        }
    }
    
    public Player[] shiftCards(Player[] p, Deck[] d, int shift){
        int x, y, z;
        int numFound;
        
        for(x = 0; x < p.length; x++){
            y = 0;
            numFound = 0;
            while(y < p[x].hand.cards.length && numFound < 3){
                for(z = 0; z < d[shift].cards.length; z++){
                    if(p[x].hand.cards[y].name().equals(d[x].cards[z].name())){
                        p[x].hand.cards[y] = d[shift].cards[z];
                        numFound++;
                    }
                }
                y++;
            }
            shift = rotate(shift);
        }
        return p;
    }
    
    public int assignPoints(Deck d){
        int points = 0;
        int x;
        
        for(x = 0; x < d.cards.length; x++){
            if(d.cards[x].suit.equals("Hearts")){
                points++;
            }else if(d.cards[x].name().equals("Queen of Spades")){
                points += 13;
            }
        }
        
        return points;
    }
    
    public int find2c(Player[] p){
        int x, y;
        int where2c = 0;
        
        x = 0;
        while(x < p.length){
            y = 0;
            while(y < p[x].hand.cards.length){
                if(p[x].hand.cards[y].name().equals("2 of Clubs")){
                    where2c = x;
                    break;
                }
                y++;
            }
            x++;
        }
        return where2c;
    }
    
    public Player[] finalizePoints(Player[] p){
        boolean shotMoon = false;
        int x;
        int y;
        x = 0;
        while(x < p.length){
            if(p[x].tempPoints == 26){
                shotMoon = true;
                break;
            }else{
                x++;
            }
        }
        
        if(shotMoon){
            for(y = 0; y < p.length; y++){
                if(y != x){
                    p[y].tempPoints = 26;
                }else{
                    p[y].tempPoints = 0;
                }
            }
        }
        
        for(x = 0; x < p.length; x++){
            p[x].points += p[x].tempPoints;
        }
        return p;
    }
}
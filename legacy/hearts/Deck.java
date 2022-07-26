//This class essentially takes an array of cards and transforms it however it wants. Sometimes it sorts it, sometimes it shuffles it,
//basically it's a bunch of key methods that do stuff with cards

package hearts;
import java.util.Scanner;
import java.io.*;
public class Deck {
    
    public Card[] cards;
        
    public Deck(int len){
        cards = new Card[len];
        for(int x = 0; x < cards.length; x++){
            cards[x] = new Card();
        }
    }
    
    public void initCards(){
        int x;
        int y;
        int z = 0;
        String[] suit = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String[] rank = {"2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "Jack", "Queen", "King", "Ace"};
        
        for(x = 0; x < suit.length; x++){
            for(y = 0; y < rank.length; y++){
                cards[z].suit = suit[x];
                cards[z].rank = rank[y];
                cards[z].val = (13*x) + y;
                z++;
            }
        }
    }
    
    public Player[] stackDeck(Player[] p, Card[] cards){
        try{
            int x, y, z;
            String input;
            Scanner IO = new Scanner(new File("deck.txt"));
            for(x = 0; x < p.length; x++){
                for(y = 0; y < p[x].hand.cards.length; y++){
                    input = IO.nextLine();
                    z = 0;
                    while (z < cards.length) {
                        if(cards[z].name().equals(input)){
                            p[x].hand.cards[y] = cards[z];
                            break;
                        }
                        z++;
                    }
                }
            }
        }catch (Exception ex) {
            System.out.println(ex);
        }
        return p;
    }
    
    public Player[] dealCards(Player[] p, Card[] cards){
        int x, y, z;
        z = 0;
        for(x = 0; x < p.length; x++){
            p[x].hand = new Deck(13);
        }
        for(x = 0; x < p[0].hand.cards.length; x++){
            y = 0;
            while(y < p.length && z < cards.length){
                p[y].hand.cards[x] = cards[z];
                y++;
                z++;
            }
        }
        return p;
    }
    
    public Card[] shuffle(Card[] oldCards){
        int x;
        int y;
        int max = oldCards.length;
        Card[] usedCards = new Card[max];
        int rand;
        Card[] newCards = new Card[max];
        boolean isUnique;
        
        for(x = 0; x < max; x++){
            isUnique = false;
            while(isUnique == false){
                rand = (int)(max*Math.random());
                newCards[x] = oldCards[rand];
                isUnique = true;
                for(y = 0; y <= x; y++){
                    if(newCards[x].equals(usedCards[y])){
                        isUnique = false;
                    }
                }
            }
            usedCards[x] = newCards[x];
        }
        return newCards;
    }
    
    public Card[] sortCards(Card[] c){
        Card placeholder;
        int x, y, z;
        int len = c.length;
        for(x = 0; x < len; x++){
            y = x - 1;
            z = x;
            while(y >= 0){
                if(c[z].val < c[y].val){
                    placeholder = c[z];
                    c[z] = c[y];
                    c[y] = placeholder;
                    z--;
                    y--;
                }else{
                    break;
                }
            }
        }
        return c;
    }
    
}
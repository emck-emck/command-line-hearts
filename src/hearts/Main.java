package src.hearts;
import java.util.ArrayList;

class Main{
    public static int CARDS_IN_DECK = 52;
    public static int CARDS_IN_HAND = 13;
    public static int NUM_RANKS = 13;
    public static int NUM_SUITS = 4;
    public static int HEART_SUIT_VAL = 2;
    public static int TWO_OF_CLUBS = 0;
    public static int QUEEN_OF_SPADES = 49;
    public static int QUEEN_OF_SPADES_VALUE = 13;
    public static int HEARTS_VALUE = 1;
    public static int PLAYERS_IN_GAME = 4;
    public static int TOTAL_POINTS_IN_MATCH = 26;
	public static int END_SCORE = 100;

    public static void main(String[] args){
        boolean isGameOver = false;
        Player[] p = new Player[PLAYERS_IN_GAME];
        for(int x = 0; x < PLAYERS_IN_GAME; x++){
            p[x] = new Player(x);
        }

        // Intro text; instructions
        System.out.println("Welcome to CLI Hearts!");
        System.out.println();
        System.out.println("Usage Instructions:");
        System.out.println("Type the rank and suit of the card you want to play.");
        System.out.println("Type quit at any time to exit game.");
        System.out.println();

        while(!isGameOver){
            Match m = new Match();
            int result = m.match(p); // Game logic handled here
            
            if(result == 1){
                System.out.println("Round over");
                calculatePoints(p);
                printResults(p);
                isGameOver = isGameEnd(p);
            }else{ // If not 1 (if 0), user quit game
                System.out.println("User quit");
                calculatePoints(p);
                printResults(p);
                break;
            }
        }        

		printFinalResults(p);

        
    }

	private static void printFinalResults(Player[] p){
		ArrayList<Player> winners;

		System.out.println();
		System.out.println("The game is over.");
		printResults(p);

		winners = calculateWinner(p);

		if(winners.size() == 1){
			System.out.println();
			System.out.println("The winner is player " + String.valueOf(winners.get(0).player_id));
			System.out.println("Thank you for playing!");
			System.out.println();
		}else{
			String winnerIDs = "";
			for(Player ps: winners){
				winnerIDs = winnerIDs + String.valueOf(ps.player_id) + ", ";
			}
			if(winnerIDs.length() > 2){
				winnerIDs = winnerIDs.substring(0, winnerIDs.length()-2);
			}
			System.out.println();
			System.out.println("The winners are players " + winnerIDs);
			System.out.println("Thank you for playing!");
		}
	}

	private static ArrayList<Player> calculateWinner(Player[] p){
		int min = END_SCORE;
		ArrayList<Player> winners = new ArrayList<Player>();
		// Find minimum score
		for (Player ps: p){
			if(ps.points < min){
				min = ps.points;
			}
		}

		// Determine winner(s) (if people have the same score)
		for (Player ps: p){
			if(ps.points == min){
				winners.add(ps);
			}
		}
		return winners;
	}

    // Print function for displaying point results
    private static void printResults(Player[] p){
        System.out.println();
        System.out.println("Results:");
        for(Player ps: p){
            System.out.println("Player " + (ps.player_id) + ": " + ps.points + " points");
        }
        System.out.println();
    }

    // Tallys up match points with total player points (and resets match points)
    private static void calculatePoints(Player[] p){
        for(Player ps: p){
            ps.points = ps.points + ps.matchPoints;
            ps.matchPoints = 0;
        }
    }

    // Checks if any player has gotten over 100 points
    private static boolean isGameEnd(Player[] p){
        for(Player ps: p){
            if(ps.points >= END_SCORE) return true;
        }
        return false;
    }
}
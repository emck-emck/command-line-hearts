#command-line-hearts
This is a command-line version of the card game Hearts written in Java. It's a simple project; it took me about a week.
I wrote this project in June 2022.

I thought about using Maven to implement the code structure but decided that for four basic Java files it was overkill.
There are Powershell and bash scripts used to compile and execute the code.

There are four classes: Main, Game, Player, and Card.

Main is the basic controller class and is the entry into the program itself.
Main holds four Players in memory and creates new Games as needed.
Each game initializes and deals Cards to Players.
Each Player holds Cards.

The Cards are identified using the numbers from 0 to 51 (inclusive).
A card's suit is found by taking the Math.floor() of the value divided by 13.
A card's rank is the value modded by 13.
There's an ugly "case-statement" method for converting a card's value to a readable String; it was my best idea at the time.

There is one human Player and three computer players.
The computer players are not sophisticate AIs, they follow the card game rules but they do not play with strategy.
The computer players rely on random number generators to control the cards they play.
Array filtering is an essential component for determining the state of a Player's hand. 
There is a TypeCasting warning where I cloned the array in order to sort its contents; I decided to ignore the warning because of the way I used the code. I know the type of the array I cloned and I know I'm assigning its clone to an array of the same type.

Player input is read through the Console class.
That means the game does not work in environments that do not have consoles to read from.

The game ends when a player reaches 100 points. The winner is the person with the least points.
The user can type "quit" at any time to exit the game.
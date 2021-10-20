# Battleship game
## Console dialogs scenarios
### Initial input stage
There are two ways to enter board and ships parameters: 
1. With console input while the program is running.
2. With command line arguments.

If command line args is empty or failed to be parsed, 
or program failed to construct battlefield with stated parameters, then 
it switches to console input mode.

#### Format of initial input
{height} {width} {numOfCarriers,numOfBattleship,numOfCruisers,nuьOfDestroyers,numOfSubmarines}

5 <= height <= 30 

5 <= width <= 30 

Ship nums are no particularly limited, but if the program 
fails to place specified number of ships on the field, you 
will be asked to decrease the numbers.

#### Format of further input
After initialising the board you will be asked
if you would like to enable torpedo and recovery modes.

For torpedo mode you should enter:
yes numOfTorpedoes - if you want to enable it 

0 < numOfTorpedoes <= totalShipNumber

or

no - if you don't want torpedo mode to be enabled.

For recovery mode you should print:

yes - if you want to enable recovery mode.

or 

no - otherwise.

### Battle stage
Before entering every in-game command you will see field layout and the following tip:  
```
You have x torpedoes left.
If you enabled torpedo mode, you can enter:  
T x y  
to launch torpedo at x y cell  
Rest of the commands:  
printDebug - print board with enemy ship - for  
debug and evaluation purposes only.  
x y - fire the x y cell  
exit - to exit game.  
Note that all x and y should be within board indexes.
```
x and y should always be withing the board size.  
You can launch torpedoes only if you have any left.

After this message you print chosen command and that is how you play the game!

### Endgame stage
When you destroyed all the ships you will be provided with total number of shots and game over message.


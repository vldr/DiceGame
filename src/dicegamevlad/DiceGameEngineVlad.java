package dicegamevlad;

// Import everything needed.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class DiceGameEngineVlad {
    // Initalize the points variable.
    private int points;
    
    // Initalize the starting points variable.
    private int startingPoints;
    
    // Initalize the rounds variable.
    private int rounds; 
    
    // Initalize the rounds won variable.
    private int roundsWon;
    
    // Initalize amount of dice variable.
    private int amountOfDice;
    
    // Initalize the dice limit.
    private int diceLimit; 
    
    // Initalize sum of all bets variable.
    private int sumOfAllBets;    
    
    // Initalize frightened attempts variable.
    private int frightenedAttempts;
    
    // Initalize luck variable.
    private int luck;
    
    // Initalize minimum bet variable.
    private int minBet;
    
    // Initalize history array where all our history transactions will be kept.
    // It is set to zero since we'll be dynamically increasing it as we add more items.
    private String[] history = new String[0];  

    // Constructor, loads all the starting values for each variable. Default.
    DiceGameEngineVlad()
    {
        // Starting points should be 1000.
        startingPoints = 1000;
        
        // Amount of dice should 2.
        amountOfDice = 2;
        
        // Dice limit should be 10, easily fits on screen.
        diceLimit = 10;
        
        // Rounds starts at zero. Always.
        rounds = 0;
        
        // Rounds won starts at zero. Always
        roundsWon = 0;
        
        // Frightened attempts should start at zero. Always
        frightenedAttempts = 0;
        
        // Sum of all bets starts at zero. Always
        sumOfAllBets = 0;
        
        // Normal luck so it's zero. -1 is unlucky, +1 is lucky.
        luck = 0;
        
        // Minimum bet should be 100. Sometimes...
        minBet = 100;
        
        // Set the points to the starting points.
        points = startingPoints;
    }
    
    // Starts the game engine.
    public void start() {
        // Intro message dialog informing user on how the game plays out.
        JOptionPane.showMessageDialog(null, "Welcome to the dice game!"
            + "\n___________________________________________________"
            + "\nYou start with " + startingPoints + " points and you must bet a minimum of " + minBet + " points inorder to play..."
            + "\nYou can choose how many dice you want to roll. For every dice you must get an odd value or else you lose."
            + "\nIf you do get an odd value for all dice then you get the amount you bet times the amount of dice rolled."
            + "\n___________________________________________________", "", JOptionPane.PLAIN_MESSAGE);
        
        // Initalize the reply variable.
        int reply = 0;
        
        // Initalize file variable.
        File file = new File("savegame.sav");
        
        // Add the reply from a question asking about save game?
        if (file.length() >= 200)
            reply = JOptionPane.showConfirmDialog(null, 
                "Would you like to continue from where you left off before?", 
                "Would you like to continue from where you left off?", 
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Check if they want to load a savegame.
        if (reply == JOptionPane.YES_OPTION )
            // Load the savegame.
            load();
        else {
            // Delete the savegame.
            file.delete();
        }
            
        // Go to the ask method.
        ask();
        
        // Return here.
        return;
    }
    
    // Asks the user if they would like to play, also shows some stats if you've played more than one round.
    private void ask() {
        System.out.println(Arrays.toString(history));
        
        // Save user items.
        save();

        // Initalize a temporary variable.
        String info = "";
        
        // For loop every history item.
        for (int i = 0; i != history.length; i++) {
            
            // Display only the recent 10 items in your history.
            if (i % 10 == 0)
                info = "";
            
            // Append the item to the string.
            info += history[i] + "<br>";
        }

        // Create a reply variable.
        int reply;
        
        // If you've played more than one round display some statistics.
        if (rounds > 0) {
            // Check the reply of the showConfirmDialog, and display stats.
            reply = JOptionPane.showConfirmDialog(null, "<html>History (last 10):" 
                    + "<br>_________________________<br><br>"
                    + info 
                    + "_________________________<br>"
                    + "<br>Balance: " + points + " points"                  
                    + "<br>Rounds Played: " + rounds                    
                    + "<br>Rounds Won: " + roundsWon                       
                    + "<br>Rounds Lost: " + (rounds - roundsWon)                      
                    + "<br>Average Bet Amount: " + (sumOfAllBets / rounds)                   
                    + "<br>Profit/Losses: " + (points - startingPoints)                    
                    + "<br>Frightened Attempts: " + frightenedAttempts      
                    + "<br>_________________________<br><br>"        
                    + "Would you like to play again?<br><br>", "Do you wish to play?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        }
        else
            // Check the reply if they want to play.
            reply = JOptionPane.showConfirmDialog(null, "Would you like to play?", "Do you wish to play?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        // If they press Yes button.
        if (reply == JOptionPane.YES_OPTION) {
            // Go to the play method.
            play();
            
            // Return here.
            return;
        }
    }
    
    // Attempts to parse an integer, returns null if it fails.
    private Integer tryParseInt(String input) {
        // Try statement to catch errors.
        try {
            
            // Return integer if successful.
            return Integer.parseInt(input);
            
        } catch (NumberFormatException ex) {
            
            // Return null if failed.
            return null;
        }
    }
    
    // Asks how much you want to bet, and how many dice you want to roll.
    private void play() {
        // Save user items.
        save();
            
        // Show input dialog to get how much they want to bet.
        String input = JOptionPane.showInputDialog(
        "<html><span style='color:gray'>Balance: " + points + " points </span><br>"
        + "_________________________<br><br>"
        + "How many points do you want to bet?<br><br></html>", "100");
        
        // Check if the user pressed Cancel button.
        if (input == null) {
            // Increase frightened attempts.
            frightenedAttempts++;

            // Save user items.
            save();
            
            // Go back to ask method.
            ask();
            
            // Return here.
            return;
        } else if (tryParseInt(input) == null) {
            // If the try parse input fails perform cheats.
            
            // Adds 250,000 points to persons account cheat.
            if (input.equals("MAKEMEARICHMAN")) {
                // Add 250,000 to the points variable.
                points += 250000;

                // Inform them that the cheat was activated.
                JOptionPane.showMessageDialog(null, "Money cheat activated...", "How did you find this?", JOptionPane.WARNING_MESSAGE); 
                
                // Go back to the play method.
                play();
                
                // Return here.
                return;
            }
            
            // Very lucky cheat.
            if (input.equals("BEGINNERSLUCK")) {
                // Set luck variable to postive one.
                luck = 1;
                
                // Inform them that the cheat was activated.
                JOptionPane.showMessageDialog(null, "Lucky cheat activated...", "How did you find this?", JOptionPane.WARNING_MESSAGE); 

                // Go back to the play method.
                play();
                
                // Return here.
                return;
            }
            
            // Bad luck cheat.
            if (input.equals("SNAKEEYES")) {
                // Set luck variable to negative one.
                luck = -1;
                
                // Inform them that the cheat was activated.
                JOptionPane.showMessageDialog(null, "Unlucky cheat activated...", "How did you find this?", JOptionPane.WARNING_MESSAGE); 
               
                // Go back to the play method.
                play();
                
                // Return here.
                return;
            }
            
            // Unlimited dice cheat.
            if (input.equals("NOMANSSKY")) {
                // Increase dice limit variable to 9999.
                diceLimit = 9999;
                
                // Inform them that the cheat was activated.
                JOptionPane.showMessageDialog(null, "Unlimited dice cheat activated...", "How did you find this?", JOptionPane.WARNING_MESSAGE); 
                
                // Go back to the play method.
                play();
                
                // Return here.
                return;
            }
            
            // Invalid characters...
            JOptionPane.showMessageDialog(null, "Invalid characters...", "Error!", JOptionPane.ERROR_MESSAGE); 
            
            // Go back to the play method.
            play();
                
            // Return here.
            return;
        }
        
        // Parse the input to an bet amount int.
        int betAmountInt = Integer.parseInt(input);  
        
        // If the bet amount is more than the points you own or the bet amount is less than mimimum betting amount.
        if (betAmountInt > points || betAmountInt < minBet) {
            JOptionPane.showMessageDialog(null, "Invalid amount of funds to bet (minimum " + minBet + ")...", "Error!", JOptionPane.ERROR_MESSAGE); 
            
            // Go back to play method.
            play();
            
            // Return here.
            return;
        }

        // Ask the user how many dice they want to roll, and input it into inputAmountOfDice variable.
        String inputAmountOfDice = JOptionPane.showInputDialog("How many dice do you want to roll?", "2");
        
        // Check if they press Cancel and return to play method.
        if (inputAmountOfDice == null) {
            // Go to the play method.
            play();
            
            // Return here.
            return;
        } else if (tryParseInt(inputAmountOfDice) == null) {
            // Check if invalid characters.
            JOptionPane.showMessageDialog(null, "Invalid characters...", "Error!", JOptionPane.ERROR_MESSAGE); 
            
            // Go to the play method.
            play();
            
            // Return here.
            return;
        }

        // Initalize the parsed int variable of the amount of dice.
        int inputAmountOfDiceInt = Integer.parseInt(inputAmountOfDice);
        
        // Check if the amount of dice is more than zero but also make sure it's within the dice limit
        // or equal to.
        if (!(inputAmountOfDiceInt > 0 && inputAmountOfDiceInt <= diceLimit)) {
            JOptionPane.showMessageDialog(null, "You can roll up to " + diceLimit + " dice only...", "Error!", JOptionPane.ERROR_MESSAGE); 
            play();
            return;
        }   

        // Set the global variable to the amount of dice.
        amountOfDice = inputAmountOfDiceInt;
        
        // Go to the bet method with bet amount variable.
        bet(betAmountInt);
    }
    
    // Actually does the betting.
    private void bet(int betAmount) {
        // Initalize the wins variable locally to zero.
        int wins = 0;
        
        // Initalize info variable.
        String info = "";
        
        // For loop through all the dice.
        for (int i = 0; i < amountOfDice; i++) {
            // For every dice make a random number between 6 and 1.
            int diceRoll = 1 + (int)(Math.random() * ((6 - 1) + 1));
            
            // If luck is equal to 1 then make the dice roll variable always odd.
            if ( luck == 1 )
                diceRoll = 1;
            // If luck is equal to -1 then ake the dice roll variable always even.
            else if (luck == -1)
                diceRoll = 2;

            // If the dice roll is odd then add a win.
            if ( diceRoll % 2 != 0 )
                wins++;
                
            // Add each dice roll information to the info variable which will be used eventually.
            info += "<br>Dice Roll #" + (i+1) + ": " + diceRoll;
        }

        // Add the bet amount to the sum of all bets.
        sumOfAllBets += betAmount;
        
        // Add one more round, to the history.
        rounds++;
        
        // If the wins don't equal the amount of dice rolled then you've lost.
        if ( wins != amountOfDice ) {
            
            // Inform the user the amount of dice he got that are even and odd.
            // Also inform the user that they've actually lost.
            JOptionPane.showMessageDialog(null, "<html>"
                    + "<span style=\"color:red;\">You lost the bet!</span>"
                    + "<br>_________________________"
                    + "<br>" + info
                    + "<br><br>Even: " + (amountOfDice - wins)                    
                    + "<br>Odd: " + wins 
                    + "<br>_________________________<br><br>"
                    + "</html>", "You lost!", JOptionPane.PLAIN_MESSAGE);
            
            // Increase the array dynamically by one.
            history = Arrays.copyOf(history, history.length + 1);
            
            // Add this transaction to the history so later on the user can see what he's recently has done.
            history[history.length - 1] = "<span style='color:red'>"
                    + points + " - " + betAmount + " = " + (points - betAmount)
                    + "</span>";
            
            // Remove the amount betted from your points balance.
            points -= betAmount;
            
            // Go to the ask method.
            ask();
            
            // Return here.
            return;
        }
        // If you did get the same amount of wins as the amount of dice rolled then you've won.
        else {
            // Increase rounds won variable.
            roundsWon++;
            
            // Inform the user that he has won. 
            JOptionPane.showMessageDialog(null, "<html>"
                    + "<span style=\"color:green;\">You won the bet!</span>"
                    + "<br>_________________________"
                    + "<br>" + info
                    + "<br><br>Even: " + (amountOfDice - wins)                    
                    + "<br>Odd: " + wins 
                    + "<br>_________________________<br><br>"
                    + "</html>", "You won!", JOptionPane.PLAIN_MESSAGE);

            // Increase the size of the array.
            history = Arrays.copyOf(history, history.length + 1);
            
            // Add the transaction to the history array for later viewing.
            history[history.length - 1] = "<span style='color:green'>"
                    + points + " + " + betAmount + " x " + amountOfDice + " = " + (points + betAmount * (amountOfDice))
                    + "</span>";
            
            // Add more points depending on the amount of dice rolled.
            points += betAmount * (amountOfDice);
            
            // Go back to the ask method.
            ask();
            
            // Return here.
            return;
        }     
    }
    
    // Saves the progress of the user.
    private void save() {
        try
        {
            // Initalize a fileoutputstream
            FileOutputStream saveFile = new FileOutputStream("savegame.sav");
            
            // Initalize a objectoutputstream using the fileoutputstream saveFile.
            ObjectOutputStream save = new ObjectOutputStream(saveFile);

            // Write all the needed variables to the file.
            save.writeObject(history);
            save.writeObject(rounds);
            save.writeObject(roundsWon);
            save.writeObject(sumOfAllBets);
            save.writeObject(frightenedAttempts);
            save.writeObject(points);
            
            // Close the file.
            save.close();
        }
        catch(Exception ex) {}
    }
    
    // Load the progress of the user.
    private void load() {
        try
        {
            // Initalize a FileInputStream
            FileInputStream saveFile = new FileInputStream("savegame.sav");
            
            // Initalize a ObjectInputStream using the FileInputStream saveFile.
            ObjectInputStream save = new ObjectInputStream(saveFile);
            
            // Read all the needed variables.
            history = (String[]) save.readObject();
            rounds = (Integer) save.readObject();
            roundsWon = (Integer) save.readObject();
            sumOfAllBets = (Integer) save.readObject();
            frightenedAttempts = (Integer) save.readObject();
            points = (Integer) save.readObject();

            // Close the file.
            save.close();
        }
        catch(Exception ex) {}
    }
}
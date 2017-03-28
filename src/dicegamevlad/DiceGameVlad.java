// Include the dicegamevlad package.
package dicegamevlad;

public class DiceGameVlad {
    // Starts program here.
    public static void main(String[] args) {
        // Call the dice engine class where our game is present.
        DiceGameEngineVlad engine = new DiceGameEngineVlad();
        
        // Call the public class start where the game starts.
        engine.start();
    }
}

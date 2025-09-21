import java.util.*;

/**
 * Simple demonstration of the Snakes and Ladders game that actually works properly.
 * Shows a working game with proper progression.
 */
public class SimpleDemo {
    public static void main(String[] args) {
        System.out.println("🐍🪜 Simple Snakes and Ladders Demo 🪜🐍");
        System.out.println("=" + "=".repeat(48) + "=");
        
        try {
            playSimpleGame();
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void playSimpleGame() {
        System.out.println("\n🎮 Starting Simple Demo Game...");
        
        // Create a standard 100-square board with default snakes and ladders
        Board board = new BoardBuilder()
                .withDefaultSnakesAndLadders()
                .build();
        
        // Use a regular dice (not seeded for real randomness)
        Dice dice = new StandardDice();
        
        // Create two players
        List<Player> players = Arrays.asList(
                new Player("Alice"),
                new Player("Bob")
        );
        
        // Create game engine
        GameEngine gameEngine = new GameEngine(board, dice, players);
        
        // Add game statistics observer
        GameStatistics statistics = new GameStatistics();
        gameEngine.addObserver(statistics);
        
        // Add a simple progress tracker
        gameEngine.addObserver(new ProgressTracker());
        
        System.out.println("Board size: " + board.getSize());
        System.out.println("Number of snakes: " + board.getSnakes().size());
        System.out.println("Number of ladders: " + board.getLadders().size());
        System.out.println("Players: " + players.stream()
                .map(Player::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse(""));
        
        System.out.println("\nStarting game...\n");
        
        // Start the game
        gameEngine.startGame();
    }
    
    /**
     * Simple progress tracker to show game progression.
     */
    private static class ProgressTracker implements GameObserver {
        private int moveCount = 0;
        
        @Override
        public void onGameStarted(List<Player> players) {
            System.out.println("🚀 Game started! Players: " + 
                             players.stream()
                                   .map(Player::getName)
                                   .reduce((a, b) -> a + ", " + b)
                                   .orElse(""));
        }
        
        @Override
        public void onPlayerMoved(Player player, Position oldPosition, Position newPosition, int diceRoll) {
            moveCount++;
            if (moveCount <= 20 || moveCount % 10 == 0) { // Show first 20 moves, then every 10th
                System.out.printf("Move %d: %s rolled %d, moved from %d to %d%n", 
                                moveCount, player.getName(), diceRoll, 
                                oldPosition.getValue(), newPosition.getValue());
            }
        }
        
        @Override
        public void onSnakeEncounter(Player player, Position snakeHead, Position snakeTail) {
            System.out.println("🐍 SNAKE! " + player.getName() + " slid from " + 
                             snakeHead.getValue() + " to " + snakeTail.getValue());
        }
        
        @Override
        public void onLadderEncounter(Player player, Position ladderBottom, Position ladderTop) {
            System.out.println("🪜 LADDER! " + player.getName() + " climbed from " + 
                             ladderBottom.getValue() + " to " + ladderTop.getValue());
        }
        
        @Override
        public void onPlayerWon(Player player, int totalMoves) {
            System.out.println("\n🎉 " + player.getName() + " WINS! 🎉");
            System.out.println("Winner reached position 100 in " + totalMoves + " moves!");
        }
        
        @Override
        public void onGameEnded(Player winner) {
            System.out.println("\n🏁 Game completed successfully!");
        }
        
        @Override
        public void onPlayerTryingToEnter(Player player, int diceRoll, boolean successful) {
            // Not relevant for simple demo
        }
        
        @Override
        public void onPlayerEliminated(Player eliminator, Player eliminated) {
            // Not relevant for simple demo
        }
        
        @Override
        public void onConsecutiveSixesPenalty(Player player, int consecutiveSixes) {
            // Not relevant for simple demo
        }
        
        @Override
        public void onMoveBlocked(Player player, int diceRoll, String reason) {
            // Not relevant for simple demo
        }
    }
}
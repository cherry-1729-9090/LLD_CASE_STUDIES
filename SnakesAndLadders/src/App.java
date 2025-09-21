import java.util.*;

/**
 * Main application class for Snakes and Ladders game.
 * Demonstrates the use of all design patterns and SOLID principles.
 * 
 * Design Patterns Used:
 * 1. Builder Pattern - BoardBuilder for creating complex Board objects
 * 2. Strategy Pattern - Dice interface with StandardDice implementation
 * 3. Observer Pattern - GameObserver interface with GameStatistics implementation
 * 
 * SOLID Principles Applied:
 * 1. Single Responsibility - Each class has one reason to change
 * 2. Open/Closed - Classes are open for extension, closed for modification
 * 3. Liskov Substitution - Derived classes can substitute base classes
 * 4. Interface Segregation - Interfaces are focused and specific
 * 5. Dependency Inversion - Depend on abstractions, not concretions
 */
public class App {
    public static void main(String[] args) {
        System.out.println("🐍🪜 Welcome to Snakes and Ladders! 🪜🐍");
        System.out.println("=" + "=".repeat(48) + "=");
        
        try {
            // Demo 1: Quick game with default setup
            playQuickGame();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Starting another game...");
            Thread.sleep(2000); // Brief pause
            
            // Demo 2: Custom game setup
            playCustomGame();
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Demonstrates a quick game with default board setup.
     */
    private static void playQuickGame() {
        System.out.println("\n🎮 Starting Quick Game with Default Setup...");
        
        // Create board using Builder Pattern with default snakes and ladders
        Board board = new BoardBuilder()
                .withDefaultSnakesAndLadders()
                .build();
        
        // Create dice using Strategy Pattern
        Dice dice = new StandardDice();
        
        // Create players
        List<Player> players = Arrays.asList(
                new Player("Alice"),
                new Player("Bob")
        );
        
        // Create game engine with dependency injection
        GameEngine gameEngine = new GameEngine(board, dice, players);
        
        // Add observer for game statistics (Observer Pattern)
        GameStatistics statistics = new GameStatistics();
        gameEngine.addObserver(statistics);
        
        // Start the game
        gameEngine.startGame();
    }
    
    /**
     * Demonstrates a custom game setup with manual board configuration.
     */
    private static void playCustomGame() {
        System.out.println("\n🎮 Starting Custom Game...");
        
        // Create custom board using Builder Pattern
        Board customBoard = new BoardBuilder()
                .setSize(30) // Even smaller board for quicker game
                .addSnake(new Position(29), new Position(15))
                .addSnake(new Position(25), new Position(7))
                .addSnake(new Position(20), new Position(12))
                .addLadder(new Position(3), new Position(14))
                .addLadder(new Position(8), new Position(18))
                .addLadder(new Position(13), new Position(24))
                .addLadder(new Position(16), new Position(26))
                .build();
        
        // Create dice with fixed seed for reproducible results (useful for testing)
        Dice deterministicDice = new StandardDice(12345L);
        
        // Create more players
        List<Player> players = Arrays.asList(
                new Player("Charlie"),
                new Player("Diana"),
                new Player("Eve")
        );
        
        // Create game engine
        GameEngine gameEngine = new GameEngine(customBoard, deterministicDice, players);
        
        // Add multiple observers
        GameStatistics statistics = new GameStatistics();
        ConsoleGameLogger logger = new ConsoleGameLogger();
        
        gameEngine.addObserver(statistics);
        gameEngine.addObserver(logger);
        
        // Print board information
        System.out.println("Custom Board Info: " + customBoard);
        System.out.println("Snakes on board: " + customBoard.getSnakes().size());
        System.out.println("Ladders on board: " + customBoard.getLadders().size());
        
        // Start the game
        gameEngine.startGame();
    }
    
    /**
     * Additional observer implementation for demonstration.
     * Shows how Observer pattern allows multiple observers.
     */
    private static class ConsoleGameLogger implements GameObserver {
        @Override
        public void onGameStarted(List<Player> players) {
            System.out.println("📝 Logger: Game started with players: " + 
                             players.stream()
                                   .map(Player::getName)
                                   .reduce((a, b) -> a + ", " + b)
                                   .orElse(""));
        }
        
        @Override
        public void onPlayerMoved(Player player, Position oldPosition, Position newPosition, int diceRoll) {
            // Only log significant moves to avoid clutter
            if (newPosition.getValue() - oldPosition.getValue() != diceRoll) {
                System.out.println("📝 Logger: " + player.getName() + " had an interesting move!");
            }
        }
        
        @Override
        public void onSnakeEncounter(Player player, Position snakeHead, Position snakeTail) {
            System.out.println("📝 Logger: Snake encounter logged for " + player.getName());
        }
        
        @Override
        public void onLadderEncounter(Player player, Position ladderBottom, Position ladderTop) {
            System.out.println("📝 Logger: Ladder encounter logged for " + player.getName());
        }
        
        @Override
        public void onPlayerWon(Player player, int totalMoves) {
            System.out.println("📝 Logger: Victory logged for " + player.getName());
        }
        
        @Override
        public void onGameEnded(Player winner) {
            System.out.println("📝 Logger: Game session ended. Winner: " + winner.getName());
        }
        
        @Override
        public void onPlayerTryingToEnter(Player player, int diceRoll, boolean successful) {
            // Optional logging for entry attempts
        }
        
        @Override
        public void onPlayerEliminated(Player eliminator, Player eliminated) {
            System.out.println("📝 Logger: Player elimination logged");
        }
        
        @Override
        public void onConsecutiveSixesPenalty(Player player, int consecutiveSixes) {
            System.out.println("📝 Logger: Penalty applied for consecutive sixes");
        }
        
        @Override
        public void onMoveBlocked(Player player, int diceRoll, String reason) {
            System.out.println("📝 Logger: Move blocked - " + reason);
        }
    }
}
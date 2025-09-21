import java.util.*;

/**
 * Main application demonstrating Snakes and Ladders game with SOLID principles and design patterns.
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
     * Quick game demonstration with default setup.
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
        
        GameStatistics statistics = new GameStatistics();
        gameEngine.addObserver(statistics);
        
        gameEngine.startGame();
    }
    
    /**
     * Custom game demonstration with manual board configuration.
     */
    private static void playCustomGame() {
        System.out.println("\n🎮 Starting Custom Game...");
        
        Board customBoard = new BoardBuilder()
                .setSize(30)
                .addSnake(new Position(29), new Position(15))
                .addSnake(new Position(25), new Position(7))
                .addSnake(new Position(20), new Position(12))
                .addLadder(new Position(3), new Position(14))
                .addLadder(new Position(8), new Position(18))
                .addLadder(new Position(13), new Position(24))
                .addLadder(new Position(16), new Position(26))
                .build();
        
        Dice deterministicDice = new StandardDice(12345L);
        
        List<Player> players = Arrays.asList(
                new Player("Charlie"),
                new Player("Diana"),
                new Player("Eve")
        );
        
        GameEngine gameEngine = new GameEngine(customBoard, deterministicDice, players);
        
        GameStatistics statistics = new GameStatistics();
        ConsoleGameLogger logger = new ConsoleGameLogger();
        
        gameEngine.addObserver(statistics);
        gameEngine.addObserver(logger);
        
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
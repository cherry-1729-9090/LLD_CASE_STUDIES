import java.util.*;

/**
 * Interactive demonstration of the enhanced Snakes and Ladders game.
 * Shows different rule configurations and their effects on gameplay.
 * More human-readable and user-friendly approach.
 */
public class InteractiveDemo {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("🎮 Welcome to Enhanced Snakes and Ladders! 🎮");
        System.out.println("=" + "=".repeat(50) + "=");
        
        try {
            showMainMenu();
        } catch (Exception e) {
            System.err.println("Oops! Something went wrong: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Shows the main menu and handles user choices.
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\nChoose your game mode:");
            System.out.println("1. 🎯 Classic Rules (Traditional gameplay)");
            System.out.println("2. 🚀 Modern Rules (All features enabled)");
            System.out.println("3. ⚙️  Custom Rules (Configure your own)");
            System.out.println("4. 📚 Rule Comparison Demo");
            System.out.println("5. 🚪 Exit");
            
            System.out.print("\nEnter your choice (1-5): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        playWithClassicRules();
                        break;
                    case 2:
                        playWithModernRules();
                        break;
                    case 3:
                        playWithCustomRules();
                        break;
                    case 4:
                        showRuleComparison();
                        break;
                    case 5:
                        System.out.println("Thanks for playing! 👋");
                        return;
                    default:
                        System.out.println("Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Plays a game with classic/traditional rules.
     */
    private static void playWithClassicRules() {
        System.out.println("\n🎯 Starting Classic Game...");
        
        GameRules rules = GameRules.createClassicRules();
        System.out.println(rules);
        
        List<Player> players = createPlayers();
        Board board = createStandardBoard();
        Dice dice = new StandardDice();
        
        playGame(board, dice, players, rules);
    }
    
    /**
     * Plays a game with modern rules (all features enabled).
     */
    private static void playWithModernRules() {
        System.out.println("\n🚀 Starting Modern Game...");
        
        GameRules rules = GameRules.createModernRules();
        System.out.println(rules);
        
        List<Player> players = createPlayers();
        Board board = createStandardBoard();
        Dice dice = new StandardDice();
        
        playGame(board, dice, players, rules);
    }
    
    /**
     * Lets user configure custom rules.
     */
    private static void playWithCustomRules() {
        System.out.println("\n⚙️ Custom Rules Configuration...");
        
        GameRules rules = buildCustomRules();
        System.out.println("\nYour custom rules:");
        System.out.println(rules);
        
        List<Player> players = createPlayers();
        Board board = createStandardBoard();
        Dice dice = new StandardDice();
        
        playGame(board, dice, players, rules);
    }
    
    /**
     * Shows a comparison of different rule sets.
     */
    private static void showRuleComparison() {
        System.out.println("\n📚 Rule Comparison Demo");
        System.out.println("=" + "=".repeat(30) + "=");
        
        System.out.println("\n🎯 CLASSIC RULES:");
        System.out.println(GameRules.createClassicRules());
        
        System.out.println("\n🚀 MODERN RULES:");
        System.out.println(GameRules.createModernRules());
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Builds custom rules based on user preferences.
     */
    private static GameRules buildCustomRules() {
        GameRules.GameRulesBuilder builder = GameRules.custom();
        
        // Entry requirement
        if (askYesNo("Should players need to roll 6 to enter the board?")) {
            builder.withSixToEnter();
        } else {
            builder.withoutSixToEnter();
        }
        
        // Player collisions
        if (askYesNo("Should players be able to eliminate each other?")) {
            builder.withPlayerCollisions();
        } else {
            builder.withoutPlayerCollisions();
        }
        
        // Consecutive sixes penalty
        if (askYesNo("Should players be penalized for rolling too many sixes?")) {
            builder.withThreeSixesPenalty();
            
            System.out.print("How many consecutive sixes before penalty? (2-10, default 3): ");
            try {
                int maxSixes = Integer.parseInt(scanner.nextLine().trim());
                if (maxSixes >= 2 && maxSixes <= 10) {
                    builder.withMaxConsecutiveSixes(maxSixes);
                }
            } catch (NumberFormatException e) {
                System.out.println("Using default value of 3.");
            }
        } else {
            builder.withoutThreeSixesPenalty();
        }
        
        // Winning strategy
        if (askYesNo("Should players win by crossing finish line (vs exact landing)?")) {
            builder.withCrossFinishLineWin();
        } else {
            builder.withExactLandingWin();
        }
        
        return builder.build();
    }
    
    /**
     * Creates player list based on user input.
     */
    private static List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        
        System.out.print("\nHow many players? (2-6): ");
        int numPlayers = 2;
        try {
            numPlayers = Integer.parseInt(scanner.nextLine().trim());
            numPlayers = Math.max(2, Math.min(6, numPlayers));
        } catch (NumberFormatException e) {
            System.out.println("Using default of 2 players.");
        }
        
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + " (or press Enter for default): ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player" + i;
            }
            players.add(new Player(name));
        }
        
        return players;
    }
    
    /**
     * Creates a standard 100-square board.
     */
    private static Board createStandardBoard() {
        return new BoardBuilder()
                .withDefaultSnakesAndLadders()
                .build();
    }
    
    /**
     * Actually runs the game with given parameters.
     */
    private static void playGame(Board board, Dice dice, List<Player> players, GameRules rules) {
        System.out.println("\n🎮 Starting game...");
        System.out.println("Players: " + players.stream()
                .map(Player::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse(""));
        
        GameEngine engine = new GameEngine(board, dice, players, rules);
        
        // Add observers
        engine.addObserver(new EnhancedGameStatistics());
        engine.addObserver(new HumanFriendlyLogger());
        
        // Start the game
        engine.startGame();
        
        System.out.println("\nPress Enter to return to main menu...");
        scanner.nextLine();
    }
    
    /**
     * Helper method to ask yes/no questions.
     */
    private static boolean askYesNo(String question) {
        while (true) {
            System.out.print(question + " (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            } else if (answer.equals("n") || answer.equals("no")) {
                return false;
            } else {
                System.out.println("Please answer 'y' or 'n'.");
            }
        }
    }
    
    /**
     * Enhanced statistics that track more details.
     */
    private static class EnhancedGameStatistics extends GameStatistics {
        private int eliminations = 0;
        private int penalties = 0;
        private int blockedMoves = 0;
        private int entryAttempts = 0;
        
        @Override
        public void onPlayerEliminated(Player eliminator, Player eliminated) {
            super.onPlayerEliminated(eliminator, eliminated);
            eliminations++;
        }
        
        @Override
        public void onConsecutiveSixesPenalty(Player player, int consecutiveSixes) {
            super.onConsecutiveSixesPenalty(player, consecutiveSixes);
            penalties++;
        }
        
        @Override
        public void onMoveBlocked(Player player, int diceRoll, String reason) {
            super.onMoveBlocked(player, diceRoll, reason);
            blockedMoves++;
        }
        
        @Override
        public void onPlayerTryingToEnter(Player player, int diceRoll, boolean successful) {
            super.onPlayerTryingToEnter(player, diceRoll, successful);
            entryAttempts++;
        }
        
        @Override
        public void onGameEnded(Player winner) {
            super.onGameEnded(winner);
            System.out.println("Enhanced Stats:");
            System.out.println("  Player eliminations: " + eliminations);
            System.out.println("  Penalties applied: " + penalties);
            System.out.println("  Blocked moves: " + blockedMoves);
            System.out.println("  Entry attempts: " + entryAttempts);
        }
    }
    
    /**
     * Human-friendly game logger with clear messages.
     */
    private static class HumanFriendlyLogger implements GameObserver {
        private int turnNumber = 0;
        
        @Override
        public void onGameStarted(List<Player> players) {
            System.out.println("\n🎲 Game begins! Good luck everyone!");
        }
        
        @Override
        public void onPlayerMoved(Player player, Position oldPos, Position newPos, int roll) {
            turnNumber++;
            if (turnNumber <= 15 || turnNumber % 20 == 0) {
                System.out.printf("Turn %d: %s rolled %d (%d→%d)%n", 
                                turnNumber, player.getName(), roll, 
                                oldPos.getValue(), newPos.getValue());
            }
        }
        
        @Override
        public void onSnakeEncounter(Player player, Position head, Position tail) {
            // Already handled by parent
        }
        
        @Override
        public void onLadderEncounter(Player player, Position bottom, Position top) {
            // Already handled by parent
        }
        
        @Override
        public void onPlayerWon(Player player, int moves) {
            System.out.println("\n🎉 " + player.getName() + " is the champion! 🎉");
        }
        
        @Override
        public void onGameEnded(Player winner) {
            System.out.println("🏁 Game over! What a match!");
        }
        
        @Override
        public void onPlayerTryingToEnter(Player player, int roll, boolean successful) {
            // Already handled by parent
        }
        
        @Override
        public void onPlayerEliminated(Player eliminator, Player eliminated) {
            // Already handled by parent
        }
        
        @Override
        public void onConsecutiveSixesPenalty(Player player, int consecutiveSixes) {
            // Already handled by parent
        }
        
        @Override
        public void onMoveBlocked(Player player, int roll, String reason) {
            // Already handled by parent
        }
    }
}
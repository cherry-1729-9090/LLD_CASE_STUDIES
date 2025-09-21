import java.util.*;

/**
 * Quick demonstration of the enhanced features.
 * Shows different rule configurations in action.
 */
public class QuickDemo {
    public static void main(String[] args) {
        System.out.println("🎮 Enhanced Snakes and Ladders Demo 🎮");
        System.out.println("=" + "=".repeat(45) + "=");
        
        // Demo 1: Classic rules
        System.out.println("\n1️⃣ CLASSIC RULES DEMO");
        demoClassicRules();
        
        // Demo 2: Modern rules with special features
        System.out.println("\n2️⃣ MODERN RULES DEMO");
        demoModernRules();
        
        // Demo 3: Custom rules
        System.out.println("\n3️⃣ CUSTOM RULES DEMO");
        demoCustomRules();
    }
    
    private static void demoClassicRules() {
        GameRules rules = GameRules.createClassicRules();
        System.out.println(rules);
        
        runQuickGame(rules, "Classic");
    }
    
    private static void demoModernRules() {
        GameRules rules = GameRules.createModernRules();
        System.out.println(rules);
        
        runQuickGame(rules, "Modern");
    }
    
    private static void demoCustomRules() {
        // Create super competitive rules
        GameRules rules = GameRules.custom()
                .withSixToEnter()
                .withPlayerCollisions()
                .withThreeSixesPenalty()
                .withCrossFinishLineWin()
                .withMaxConsecutiveSixes(2)  // Very strict!
                .build();
        
        System.out.println("🔥 ULTRA COMPETITIVE RULES:");
        System.out.println(rules);
        
        runQuickGame(rules, "Ultra Competitive");
    }
    
    private static void runQuickGame(GameRules rules, String gameType) {
        try {
            List<Player> players = Arrays.asList(
                new Player("Alice"),
                new Player("Bob"),
                new Player("Charlie")
            );
            
            Board board = new BoardBuilder()
                    .setSize(30)  // Smaller board for quicker demo
                    .addSnake(new Position(28), new Position(10))
                    .addSnake(new Position(25), new Position(5))
                    .addLadder(new Position(3), new Position(15))
                    .addLadder(new Position(8), new Position(20))
                    .addLadder(new Position(12), new Position(26))
                    .build();
            
            Dice dice = new StandardDice();
            GameEngine engine = new GameEngine(board, dice, players, rules);
            
            // Add compact observer for demo
            engine.addObserver(new CompactObserver(gameType));
            
            System.out.println("🎲 Starting " + gameType + " game...");
            engine.startGame();
            
        } catch (Exception e) {
            System.out.println("Demo encountered issue: " + e.getMessage());
        }
        
        System.out.println("\n" + "-".repeat(50));
    }
    
    /**
     * Compact observer for demo purposes.
     */
    private static class CompactObserver implements GameObserver {
        private String gameType;
        private int moveCount = 0;
        
        public CompactObserver(String gameType) {
            this.gameType = gameType;
        }
        
        @Override
        public void onGameStarted(List<Player> players) {
            System.out.println("🚀 " + gameType + " game started with " + players.size() + " players");
        }
        
        @Override
        public void onPlayerMoved(Player player, Position oldPos, Position newPos, int roll) {
            moveCount++;
            if (moveCount <= 10) {
                System.out.println("  " + player.getName() + " rolled " + roll + 
                                 " (" + oldPos.getValue() + "→" + newPos.getValue() + ")");
            } else if (moveCount == 11) {
                System.out.println("  ... (continuing quietly) ...");
            }
        }
        
        @Override
        public void onSnakeEncounter(Player player, Position head, Position tail) {
            System.out.println("  🐍 " + player.getName() + " hit snake! " + 
                             head.getValue() + "→" + tail.getValue());
        }
        
        @Override
        public void onLadderEncounter(Player player, Position bottom, Position top) {
            System.out.println("  🪜 " + player.getName() + " found ladder! " + 
                             bottom.getValue() + "→" + top.getValue());
        }
        
        @Override
        public void onPlayerTryingToEnter(Player player, int roll, boolean successful) {
            if (successful) {
                System.out.println("  🚪 " + player.getName() + " entered with " + roll + "!");
            } else {
                System.out.println("  🚫 " + player.getName() + " rolled " + roll + " (needs 6 to enter)");
            }
        }
        
        @Override
        public void onPlayerEliminated(Player eliminator, Player eliminated) {
            System.out.println("  💥 " + eliminator.getName() + " eliminated " + eliminated.getName() + "!");
        }
        
        @Override
        public void onConsecutiveSixesPenalty(Player player, int consecutiveSixes) {
            System.out.println("  ⚠️ " + player.getName() + " penalized for " + consecutiveSixes + " sixes!");
        }
        
        @Override
        public void onMoveBlocked(Player player, int roll, String reason) {
            System.out.println("  🚧 " + player.getName() + "'s move blocked (" + reason + ")");
        }
        
        @Override
        public void onPlayerWon(Player player, int moves) {
            System.out.println("🏆 " + player.getName() + " wins " + gameType + " game in " + moves + " moves!");
        }
        
        @Override
        public void onGameEnded(Player winner) {
            System.out.println("✅ " + gameType + " game completed! Total moves: " + moveCount);
        }
    }
}
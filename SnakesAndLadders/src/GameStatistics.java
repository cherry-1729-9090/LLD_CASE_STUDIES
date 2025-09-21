import java.util.*;

/**
 * Game statistics observer that tracks various game metrics.
 * Follows Observer Pattern and Single Responsibility Principle.
 */
public class GameStatistics implements GameObserver {
    private int totalMoves;
    private int snakeEncounters;
    private int ladderEncounters;
    private Map<String, Integer> playerMoves;
    private long gameStartTime;
    private long gameEndTime;
    
    public GameStatistics() {
        this.playerMoves = new HashMap<>();
        reset();
    }
    
    @Override
    public void onGameStarted(List<Player> players) {
        reset();
        this.gameStartTime = System.currentTimeMillis();
        for (Player player : players) {
            playerMoves.put(player.getName(), 0);
        }
        System.out.println("🎮 Game started with " + players.size() + " players!");
    }
    
    @Override
    public void onPlayerMoved(Player player, Position oldPosition, Position newPosition, int diceRoll) {
        totalMoves++;
        playerMoves.put(player.getName(), playerMoves.get(player.getName()) + 1);
        
        System.out.println("🎲 " + player.getName() + " rolled " + diceRoll + 
                          " and moved from " + oldPosition.getValue() + 
                          " to " + newPosition.getValue());
    }
    
    @Override
    public void onSnakeEncounter(Player player, Position snakeHead, Position snakeTail) {
        snakeEncounters++;
        System.out.println("🐍 " + player.getName() + " encountered a snake! " +
                          "Slid from " + snakeHead.getValue() + " to " + snakeTail.getValue());
    }
    
    @Override
    public void onLadderEncounter(Player player, Position ladderBottom, Position ladderTop) {
        ladderEncounters++;
        System.out.println("🪜 " + player.getName() + " found a ladder! " +
                          "Climbed from " + ladderBottom.getValue() + " to " + ladderTop.getValue());
    }
    
    @Override
    public void onPlayerWon(Player player, int totalMoves) {
        System.out.println("🏆 " + player.getName() + " won the game in " + totalMoves + " moves!");
    }
    
    @Override
    public void onGameEnded(Player winner) {
        this.gameEndTime = System.currentTimeMillis();
        printGameStatistics(winner);
    }
    
    private void printGameStatistics(Player winner) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🏆 GAME STATISTICS 🏆");
        System.out.println("=".repeat(50));
        System.out.println("Winner: " + winner.getName());
        System.out.println("Total moves: " + totalMoves);
        System.out.println("Snake encounters: " + snakeEncounters);
        System.out.println("Ladder encounters: " + ladderEncounters);
        System.out.println("Game duration: " + (gameEndTime - gameStartTime) + " ms");
        
        System.out.println("\nPlayer Moves:");
        playerMoves.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " moves"));
        
        System.out.println("=".repeat(50));
    }
    
    private void reset() {
        this.totalMoves = 0;
        this.snakeEncounters = 0;
        this.ladderEncounters = 0;
        this.playerMoves.clear();
        this.gameStartTime = 0;
        this.gameEndTime = 0;
    }
    
    @Override
    public void onPlayerTryingToEnter(Player player, int diceRoll, boolean successful) {
        if (successful) {
            System.out.println("🚪 " + player.getName() + " rolled " + diceRoll + " and entered the board!");
        } else {
            System.out.println("🚫 " + player.getName() + " rolled " + diceRoll + " but needs 6 to enter");
        }
    }
    
    @Override
    public void onPlayerEliminated(Player eliminator, Player eliminated) {
        System.out.println("💥 " + eliminator.getName() + " eliminated " + eliminated.getName() + 
                          "! " + eliminated.getName() + " goes back to start");
    }
    
    @Override
    public void onConsecutiveSixesPenalty(Player player, int consecutiveSixes) {
        System.out.println("⚠️ " + player.getName() + " rolled " + consecutiveSixes + 
                          " sixes in a row! Penalty: back to start");
    }
    
    @Override
    public void onMoveBlocked(Player player, int diceRoll, String reason) {
        System.out.println("🚧 " + player.getName() + "'s move blocked (rolled " + diceRoll + "): " + reason);
    }
    
    // Getters for accessing statistics
    public int getTotalMoves() { return totalMoves; }
    public int getSnakeEncounters() { return snakeEncounters; }
    public int getLadderEncounters() { return ladderEncounters; }
    public Map<String, Integer> getPlayerMoves() { return new HashMap<>(playerMoves); }
    public long getGameDuration() { return gameEndTime - gameStartTime; }
}
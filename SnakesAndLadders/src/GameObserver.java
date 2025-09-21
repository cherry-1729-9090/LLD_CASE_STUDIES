/**
 * Observer interface for game events following Observer design pattern.
 */
public interface GameObserver {
    void onPlayerMoved(Player player, Position oldPosition, Position newPosition, int diceRoll);
    
    void onSnakeEncounter(Player player, Position snakeHead, Position snakeTail);
    
    void onLadderEncounter(Player player, Position ladderBottom, Position ladderTop);
    
    void onPlayerWon(Player player, int totalMoves);
    
    void onGameStarted(java.util.List<Player> players);
    
    /**
     * Called when the game ends.
     */
    void onGameEnded(Player winner);
    
    /**
     * Called when a player tries to enter the board.
     */
    void onPlayerTryingToEnter(Player player, int diceRoll, boolean successful);
    
    /**
     * Called when a player eliminates another player.
     */
    void onPlayerEliminated(Player eliminator, Player eliminated);
    
    /**
     * Called when a player is penalized for consecutive sixes.
     */
    void onConsecutiveSixesPenalty(Player player, int consecutiveSixes);
    
    /**
     * Called when a player's move is blocked (exact landing rule).
     */
    void onMoveBlocked(Player player, int diceRoll, String reason);
}
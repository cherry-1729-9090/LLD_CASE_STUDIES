/**
 * Observer interface for game events.
 * Enhanced to support new rule-based events.
 * Follows Observer Design Pattern for loose coupling between game engine and observers.
 */
public interface GameObserver {
    /**
     * Called when a player moves.
     */
    void onPlayerMoved(Player player, Position oldPosition, Position newPosition, int diceRoll);
    
    /**
     * Called when a player encounters a snake.
     */
    void onSnakeEncounter(Player player, Position snakeHead, Position snakeTail);
    
    /**
     * Called when a player encounters a ladder.
     */
    void onLadderEncounter(Player player, Position ladderBottom, Position ladderTop);
    
    /**
     * Called when a player wins the game.
     */
    void onPlayerWon(Player player, int totalMoves);
    
    /**
     * Called when the game starts.
     */
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
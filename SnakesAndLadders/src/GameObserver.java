/**
 * Observer interface for game events.
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
}
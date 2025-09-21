import java.util.*;

/**
 * Main game engine that manages the Snakes and Ladders game flow.
 * Follows Single Responsibility Principle - manages game logic only.
 * Follows Open/Closed Principle - can be extended with new game rules.
 * Follows Dependency Inversion Principle - depends on abstractions (Dice interface).
 * Implements Observer Pattern for game events.
 */
public class GameEngine {
    private final Board board;
    private final Dice dice;
    private final List<Player> players;
    private final List<GameObserver> observers;
    private int currentPlayerIndex;
    private boolean gameEnded;
    private Player winner;
    private Map<String, Integer> playerMoveCount;
    
    public GameEngine(Board board, Dice dice, List<Player> players) {
        if (board == null || dice == null || players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Board, dice, and players cannot be null or empty");
        }
        
        if (players.size() < 2) {
            throw new IllegalArgumentException("Game requires at least 2 players");
        }
        
        this.board = board;
        this.dice = dice;
        this.players = new ArrayList<>(players);
        this.observers = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameEnded = false;
        this.playerMoveCount = new HashMap<>();
        
        // Initialize move count for each player
        for (Player player : players) {
            playerMoveCount.put(player.getName(), 0);
        }
    }
    
    /**
     * Adds an observer to the game.
     * Follows Observer Pattern.
     */
    public void addObserver(GameObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }
    
    /**
     * Removes an observer from the game.
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Starts the game.
     */
    public void startGame() {
        if (gameEnded) {
            throw new IllegalStateException("Game has already ended");
        }
        
        notifyGameStarted();
        
        int maxTurns = 1000; // Prevent infinite games
        int turnCount = 0;
        
        while (!gameEnded && turnCount < maxTurns) {
            playTurn();
            turnCount++;
        }
        
        if (turnCount >= maxTurns) {
            System.out.println("⏰ Game ended due to maximum turn limit reached!");
            // Find player closest to winning position
            winner = players.stream()
                    .max((p1, p2) -> Integer.compare(p1.getCurrentPosition().getValue(), 
                                                   p2.getCurrentPosition().getValue()))
                    .orElse(players.get(0));
            gameEnded = true;
        }
        
        notifyGameEnded();
    }
    
    /**
     * Plays a single turn for the current player.
     */
    private void playTurn() {
        Player currentPlayer = getCurrentPlayer();
        Position oldPosition = currentPlayer.getCurrentPosition();
        
        // Roll dice
        int diceRoll = dice.roll();
        
        // Calculate new position
        int newPositionValue = oldPosition.getValue() + diceRoll;
        
        // Check if player would exceed board size
        if (newPositionValue > board.getSize()) {
            // Player stays at current position (house rule)
            notifyPlayerMoved(currentPlayer, oldPosition, oldPosition, diceRoll);
            moveToNextPlayer();
            return;
        }
        
        Position newPosition = new Position(newPositionValue);
        currentPlayer.setCurrentPosition(newPosition);
        incrementPlayerMoveCount(currentPlayer);
        
        // Notify about the move
        notifyPlayerMoved(currentPlayer, oldPosition, newPosition, diceRoll);
        
        // Check for snakes and ladders
        Position finalPosition = board.getFinalPosition(newPosition);
        
        if (!finalPosition.equals(newPosition)) {
            // Player encountered snake or ladder
            currentPlayer.setCurrentPosition(finalPosition);
            
            // Determine if it was a snake or ladder
            if (finalPosition.getValue() < newPosition.getValue()) {
                // It was a snake
                notifySnakeEncounter(currentPlayer, newPosition, finalPosition);
            } else {
                // It was a ladder
                notifyLadderEncounter(currentPlayer, newPosition, finalPosition);
            }
        }
        
        // Check for win condition
        if (currentPlayer.hasWon()) {
            gameEnded = true;
            winner = currentPlayer;
            notifyPlayerWon(currentPlayer);
            return;
        }
        
        moveToNextPlayer();
    }
    
    /**
     * Gets the current player.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Moves to the next player.
     */
    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    
    /**
     * Increments the move count for a player.
     */
    private void incrementPlayerMoveCount(Player player) {
        playerMoveCount.put(player.getName(), playerMoveCount.get(player.getName()) + 1);
    }
    
    /**
     * Gets the total moves made by a player.
     */
    public int getPlayerMoveCount(Player player) {
        return playerMoveCount.getOrDefault(player.getName(), 0);
    }
    
    // Observer notification methods
    private void notifyGameStarted() {
        for (GameObserver observer : observers) {
            observer.onGameStarted(new ArrayList<>(players));
        }
    }
    
    private void notifyPlayerMoved(Player player, Position oldPosition, Position newPosition, int diceRoll) {
        for (GameObserver observer : observers) {
            observer.onPlayerMoved(player, oldPosition, newPosition, diceRoll);
        }
    }
    
    private void notifySnakeEncounter(Player player, Position snakeHead, Position snakeTail) {
        for (GameObserver observer : observers) {
            observer.onSnakeEncounter(player, snakeHead, snakeTail);
        }
    }
    
    private void notifyLadderEncounter(Player player, Position ladderBottom, Position ladderTop) {
        for (GameObserver observer : observers) {
            observer.onLadderEncounter(player, ladderBottom, ladderTop);
        }
    }
    
    private void notifyPlayerWon(Player player) {
        for (GameObserver observer : observers) {
            observer.onPlayerWon(player, getPlayerMoveCount(player));
        }
    }
    
    private void notifyGameEnded() {
        for (GameObserver observer : observers) {
            observer.onGameEnded(winner);
        }
    }
    
    // Getters for game state
    public boolean isGameEnded() { return gameEnded; }
    public Player getWinner() { return winner; }
    public List<Player> getPlayers() { return new ArrayList<>(players); }
    public Board getBoard() { return board; }
}
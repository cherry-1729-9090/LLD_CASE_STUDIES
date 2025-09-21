import java.util.*;

/**
 * Game engine with configurable rules support and human-readable code structure.
 */
public class GameEngine {
    private final Board board;
    private final Dice dice;
    private final List<Player> players;
    private final List<GameObserver> observers;
    private final GameRules rules;
    
    private int currentPlayerIndex;
    private boolean gameEnded;
    private Player winner;
    
    public GameEngine(Board board, Dice dice, List<Player> players, GameRules rules) {
        validateGameSetup(board, dice, players, rules);
        
        this.board = board;
        this.dice = dice;
        this.players = new ArrayList<>(players);
        this.rules = rules;
        this.observers = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameEnded = false;
    }
    
    public GameEngine(Board board, Dice dice, List<Player> players) {
        this(board, dice, players, GameRules.createClassicRules());
    }
    
    /**
     * Adds observer to watch game events.
     */
    public void addObserver(GameObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }
    
    /**
     * Starts the game with turn-based play until completion.
     */
    public void startGame() {
        if (gameEnded) {
            throw new IllegalStateException("This game has already finished!");
        }
        
        announceGameStart();
        
        int turnCounter = 0;
        int maxTurns = 2000;
        
        while (!gameEnded && turnCounter < maxTurns) {
            playPlayerTurn(getCurrentPlayer());
            moveToNextPlayer();
            turnCounter++;
        }
        
        if (turnCounter >= maxTurns) {
            handleGameTimeout();
        }
        
        announceGameEnd();
    }
    
    /**
     * Handles a single player's turn with dice roll and move logic.
     */
    private void playPlayerTurn(Player player) {
        int diceRoll = dice.roll();
        
        if (diceRoll == 6) {
            player.rollSix();
        } else {
            player.rollNonSix();
        }
        
        if (shouldPenalizeForConsecutiveSixes(player)) {
            applyConsecutiveSixesPenalty(player);
            return;
        }
        
        if (!player.hasEnteredBoard() && rules.needSixToEnter()) {
            handleBoardEntry(player, diceRoll);
            return;
        }
        
        executePlayerMove(player, diceRoll);
    }
    
    /**
     * Handles a player trying to enter the board.
     */
    private void handleBoardEntry(Player player, int diceRoll) {
        if (diceRoll == 6) {
            player.enterBoard();
            notifyPlayerTryingToEnter(player, diceRoll, true);
            // Don't move yet, just entered
        } else {
            notifyPlayerTryingToEnter(player, diceRoll, false);
        }
    }
    
    /**
     * Executes the actual movement of a player.
     */
    private void executePlayerMove(Player player, int diceRoll) {
        Position oldPosition = player.getCurrentPosition();
        Position targetPosition = calculateTargetPosition(oldPosition, diceRoll);
        
        // Check if move is valid based on winning strategy
        if (!isValidMove(targetPosition)) {
            notifyMoveBlocked(player, diceRoll, "Would exceed board limit");
            return;
        }
        
        // Move player to new position
        player.setCurrentPosition(targetPosition);
        notifyPlayerMoved(player, oldPosition, targetPosition, diceRoll);
        
        // Handle collisions with other players
        if (rules.allowPlayerCollisions()) {
            handlePlayerCollisions(player, targetPosition);
        }
        
        // Check for snakes and ladders
        Position finalPosition = board.getFinalPosition(targetPosition);
        if (!finalPosition.equals(targetPosition)) {
            movePlayerToFinalPosition(player, targetPosition, finalPosition);
        }
        
        // Check if player won
        if (player.hasWon(rules.getWinningStrategy())) {
            declareWinner(player);
        }
    }
    
    /**
     * Calculates where player should land based on dice roll.
     */
    private Position calculateTargetPosition(Position currentPosition, int diceRoll) {
        int newPositionValue = currentPosition.getValue() + diceRoll;
        
        // Handle different winning strategies
        if (rules.getWinningStrategy() == WinningStrategy.CROSS_FINISH_LINE) {
            // Allow going beyond board size
            return new Position(Math.min(newPositionValue, board.getSize()));
        } else {
            // Exact landing required
            if (newPositionValue > board.getSize()) {
                return currentPosition; // Stay put if would exceed
            }
            return new Position(newPositionValue);
        }
    }
    
    /**
     * Checks if a move to target position is valid.
     */
    private boolean isValidMove(Position targetPosition) {
        return targetPosition.getValue() <= board.getSize();
    }
    
    /**
     * Handles what happens when players land on the same square.
     */
    private void handlePlayerCollisions(Player currentPlayer, Position position) {
        for (Player otherPlayer : players) {
            if (otherPlayer != currentPlayer && 
                otherPlayer.getCurrentPosition().equals(position) && 
                otherPlayer.hasEnteredBoard()) {
                
                // Send the other player back to start
                otherPlayer.sendToStart();
                notifyPlayerEliminated(currentPlayer, otherPlayer);
            }
        }
    }
    
    /**
     * Moves player after encountering snake or ladder.
     */
    private void movePlayerToFinalPosition(Player player, Position encounterPosition, Position finalPosition) {
        player.setCurrentPosition(finalPosition);
        
        if (finalPosition.getValue() < encounterPosition.getValue()) {
            notifySnakeEncounter(player, encounterPosition, finalPosition);
        } else {
            notifyLadderEncounter(player, encounterPosition, finalPosition);
        }
    }
    
    /**
     * Checks if player should be penalized for consecutive sixes.
     */
    private boolean shouldPenalizeForConsecutiveSixes(Player player) {
        return rules.hasThreeSixesPenalty() && 
               player.getConsecutiveSixes() >= rules.getMaxConsecutiveSixes();
    }
    
    /**
     * Applies penalty for rolling too many sixes in a row.
     */
    private void applyConsecutiveSixesPenalty(Player player) {
        int consecutiveSixes = player.getConsecutiveSixes();
        player.sendToStart();
        notifyConsecutiveSixesPenalty(player, consecutiveSixes);
    }
    
    /**
     * Declares the winner and ends the game.
     */
    private void declareWinner(Player player) {
        gameEnded = true;
        winner = player;
        notifyPlayerWon(player);
    }
    
    /**
     * Gets the player whose turn it is right now.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Moves to the next player's turn.
     */
    private void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    
    /**
     * Handles game timeout scenario.
     */
    private void handleGameTimeout() {
        System.out.println("⏰ Game reached maximum turns! Finding winner...");
        winner = findLeadingPlayer();
        gameEnded = true;
    }
    
    /**
     * Finds the player who's closest to winning.
     */
    private Player findLeadingPlayer() {
        return players.stream()
                .filter(Player::hasEnteredBoard)
                .max((p1, p2) -> Integer.compare(p1.getCurrentPosition().getValue(), 
                                               p2.getCurrentPosition().getValue()))
                .orElse(players.get(0));
    }
    
    /**
     * Validates the game setup before starting.
     */
    private void validateGameSetup(Board board, Dice dice, List<Player> players, GameRules rules) {
        if (board == null || dice == null || players == null || rules == null) {
            throw new IllegalArgumentException("Game components cannot be null");
        }
        
        if (players.isEmpty()) {
            throw new IllegalArgumentException("Need at least one player to play");
        }
        
        if (players.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 players for a proper game");
        }
    }
    
    // Observer notification methods - more human-readable names
    private void announceGameStart() {
        for (GameObserver observer : observers) {
            observer.onGameStarted(new ArrayList<>(players));
        }
    }
    
    private void announceGameEnd() {
        for (GameObserver observer : observers) {
            observer.onGameEnded(winner);
        }
    }
    
    private void notifyPlayerMoved(Player player, Position oldPos, Position newPos, int roll) {
        for (GameObserver observer : observers) {
            observer.onPlayerMoved(player, oldPos, newPos, roll);
        }
    }
    
    private void notifySnakeEncounter(Player player, Position head, Position tail) {
        for (GameObserver observer : observers) {
            observer.onSnakeEncounter(player, head, tail);
        }
    }
    
    private void notifyLadderEncounter(Player player, Position bottom, Position top) {
        for (GameObserver observer : observers) {
            observer.onLadderEncounter(player, bottom, top);
        }
    }
    
    private void notifyPlayerWon(Player player) {
        for (GameObserver observer : observers) {
            observer.onPlayerWon(player, player.getTotalMoves());
        }
    }
    
    private void notifyPlayerTryingToEnter(Player player, int roll, boolean successful) {
        for (GameObserver observer : observers) {
            observer.onPlayerTryingToEnter(player, roll, successful);
        }
    }
    
    private void notifyPlayerEliminated(Player eliminator, Player eliminated) {
        for (GameObserver observer : observers) {
            observer.onPlayerEliminated(eliminator, eliminated);
        }
    }
    
    private void notifyConsecutiveSixesPenalty(Player player, int consecutiveSixes) {
        for (GameObserver observer : observers) {
            observer.onConsecutiveSixesPenalty(player, consecutiveSixes);
        }
    }
    
    private void notifyMoveBlocked(Player player, int roll, String reason) {
        for (GameObserver observer : observers) {
            observer.onMoveBlocked(player, roll, reason);
        }
    }
    
    // Public getters for game state
    public boolean isGameEnded() { return gameEnded; }
    public Player getWinner() { return winner; }
    public List<Player> getPlayers() { return new ArrayList<>(players); }
    public Board getBoard() { return board; }
    public GameRules getRules() { return rules; }
}
/**
 * Configurable game rules using Builder pattern for easy setup and extensibility.
 */
public class GameRules {
    private final boolean needSixToEnter;
    private final boolean allowPlayerCollisions;
    private final boolean threeSixesPenalty;
    private final WinningStrategy winningStrategy;
    private final int maxConsecutiveSixes;
    
    private GameRules(GameRulesBuilder builder) {
        this.needSixToEnter = builder.needSixToEnter;
        this.allowPlayerCollisions = builder.allowPlayerCollisions;
        this.threeSixesPenalty = builder.threeSixesPenalty;
        this.winningStrategy = builder.winningStrategy;
        this.maxConsecutiveSixes = builder.maxConsecutiveSixes;
    }
    
    public boolean needSixToEnter() {
        return needSixToEnter;
    }
    
    public boolean allowPlayerCollisions() {
        return allowPlayerCollisions;
    }
    
    public boolean hasThreeSixesPenalty() {
        return threeSixesPenalty;
    }
    
    public WinningStrategy getWinningStrategy() {
        return winningStrategy;
    }
    
    public int getMaxConsecutiveSixes() {
        return maxConsecutiveSixes;
    }
    
    /**
     * Creates standard rules for traditional gameplay.
     */
    public static GameRules createClassicRules() {
        return new GameRulesBuilder()
                .withExactLandingWin()
                .withoutSixToEnter()
                .withoutPlayerCollisions()
                .withoutThreeSixesPenalty()
                .build();
    }
    
    /**
     * Creates modern rules with interactive features.
     */
    public static GameRules createModernRules() {
        return new GameRulesBuilder()
                .withCrossFinishLineWin()
                .withSixToEnter()
                .withPlayerCollisions()
                .withThreeSixesPenalty()
                .build();
    }
    
    /**
     * Creates a builder for custom rule configuration.
     */
    public static GameRulesBuilder custom() {
        return new GameRulesBuilder();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game Rules:\n");
        sb.append("- Entry requirement: ").append(needSixToEnter ? "Roll 6 to enter" : "Enter immediately").append("\n");
        sb.append("- Player collisions: ").append(allowPlayerCollisions ? "Enabled (can eliminate)" : "Disabled").append("\n");
        sb.append("- Three 6s penalty: ").append(threeSixesPenalty ? "Enabled (reset to start)" : "Disabled").append("\n");
        sb.append("- Winning strategy: ").append(winningStrategy.getDescription()).append("\n");
        return sb.toString();
    }
    
    /**
     * Builder class for creating GameRules with fluent API.
     */
    public static class GameRulesBuilder {
        private boolean needSixToEnter = false;
        private boolean allowPlayerCollisions = false;
        private boolean threeSixesPenalty = false;
        private WinningStrategy winningStrategy = WinningStrategy.EXACT_LANDING;
        private int maxConsecutiveSixes = 3;
        
        public GameRulesBuilder withSixToEnter() {
            this.needSixToEnter = true;
            return this;
        }
        
        public GameRulesBuilder withoutSixToEnter() {
            this.needSixToEnter = false;
            return this;
        }
        
        public GameRulesBuilder withPlayerCollisions() {
            this.allowPlayerCollisions = true;
            return this;
        }
        
        public GameRulesBuilder withoutPlayerCollisions() {
            this.allowPlayerCollisions = false;
            return this;
        }
        
        public GameRulesBuilder withThreeSixesPenalty() {
            this.threeSixesPenalty = true;
            return this;
        }
        
        public GameRulesBuilder withoutThreeSixesPenalty() {
            this.threeSixesPenalty = false;
            return this;
        }
        
        public GameRulesBuilder withExactLandingWin() {
            this.winningStrategy = WinningStrategy.EXACT_LANDING;
            return this;
        }
        
        public GameRulesBuilder withCrossFinishLineWin() {
            this.winningStrategy = WinningStrategy.CROSS_FINISH_LINE;
            return this;
        }
        
        public GameRulesBuilder withMaxConsecutiveSixes(int max) {
            if (max < 2 || max > 10) {
                throw new IllegalArgumentException("Max consecutive sixes must be between 2 and 10");
            }
            this.maxConsecutiveSixes = max;
            return this;
        }
        
        public GameRules build() {
            return new GameRules(this);
        }
    }
}
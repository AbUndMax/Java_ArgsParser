package ArgsParser;

public class Command {
    private final String fullCommandName;
    private final String shortCommandName;
    private final String description;
    private ArgsParser argsParser;
    private boolean status = false;

    /**
     * Constructs a Command object with specified full flag, short flag, description, and args parser.
     *
     * @param fullCommandName the detailed or long form flag for this command
     * @param shortCommandName the abbreviated or short form flag for this command
     * @param description a brief description of what this command does
     */
    public Command(String fullCommandName, String shortCommandName, String description) {
        this.fullCommandName = fullCommandName;
        this.shortCommandName = shortCommandName;
        this.description = description;
    }

    protected void setArgsParser(ArgsParser argsParser) {
        this.argsParser = argsParser;
    }

    /**
     * Returns the full flag associated with this command.
     *
     * @return the full flag of the command.
     */
    protected String getFullCommandName() {
        return fullCommandName;
    }

    /**
     * Returns the short flag associated with this command.
     *
     * @return the short flag of the command.
     */
    protected String getShortCommandName() {
        return shortCommandName;
    }

    /**
     * Returns the description of the command.
     *
     * @return the description of the command.
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Sets the command status to true, indicating that the command has been activated.
     */
    protected void setCommand() {
        this.status = true;
    }

    /**
     * Checks if the command has been provided after the arguments were parsed.
     *
     * @return true if the command is provided, false otherwise.
     * @throws IllegalArgumentException if the {@link ArgsParser#parse(String[] args)} method was not called before checking the command.
     * or if this Command was not added to any {@link ArgsParser}!
     */
    public boolean isProvided() throws IllegalStateException {
        if (argsParser == null) throw new IllegalStateException("Command: " + this + " is not assigned to any parser instance!");
        if (!argsParser.parseArgsWasCalled()) throw new IllegalStateException("parse() was not called before trying to check the command!");
        return status;
    }

    /**
     * Creates a String identifying this Command instance with full and short name version
     * @return a String identifying this Command instance with full and short name version
     */
    @Override
    public String toString() {
        return "[" + fullCommandName + " / " + shortCommandName + "]";
    }

}

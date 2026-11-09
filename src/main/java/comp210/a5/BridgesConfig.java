package comp210.a5;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Reads your BRIDGES credentials out of the .env file sitting next to pom.xml.
 *
 * Nothing here needs changing. Read it anyway. Loading a secret from outside
 * the source code is a thing you will end up writing again, and this is about
 * as small as the pattern gets.
 *
 * Why a file instead of a String literal in ShakespeareWords? Source code
 * gets committed, pushed, screenshotted, and pasted into Slack, and an API key
 * is a password. The .env file is listed in .gitignore, so git leaves it alone
 * and the key stays on your machine.
 */
public final class BridgesConfig {

    private static final String USERNAME_KEY = "BRIDGES_USERNAME";
    private static final String APIKEY_KEY = "BRIDGES_APIKEY";
    private static final String ASSIGNMENT_KEY = "BRIDGES_ASSIGNMENT";

    private final String username;
    private final String apiKey;
    private final int assignmentNumber;

    private BridgesConfig(String username, String apiKey, int assignmentNumber) {
        this.username = username;
        this.apiKey = apiKey;
        this.assignmentNumber = assignmentNumber;
    }

    /**
     * Loads .env and hands back the three values BRIDGES needs.
     *
     * @throws IllegalStateException with a readable message if the file is
     *         missing or a value was left blank. Failing here, with a message
     *         that names the problem, saves you from debugging an HTTP error
     *         thirty seconds later that says nothing useful.
     */
    public static BridgesConfig load() {
        // Launched through Maven, pom.xml sets "dotenv.dir" to the folder holding
        // pom.xml. IntelliJ's green arrow skips that, so we fall back to the
        // working directory, which IntelliJ sets to that same folder.
        String dir = System.getProperty("dotenv.dir", "./");

        Dotenv dotenv;
        try {
            dotenv = Dotenv.configure().directory(dir).load();
        } catch (DotenvException e) {
            throw new IllegalStateException(
                "Could not find a .env file. Copy .env.example to .env and fill in "
                + "your BRIDGES username and API key. See the Setup section of the README.", e);
        }

        String user = require(dotenv, USERNAME_KEY);
        String key = require(dotenv, APIKEY_KEY);

        String rawAssignment = dotenv.get(ASSIGNMENT_KEY, "0");
        int assignment;
        try {
            assignment = Integer.parseInt(rawAssignment.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                ASSIGNMENT_KEY + " must be a whole number, but .env says \"" + rawAssignment + "\".", e);
        }

        return new BridgesConfig(user, key, assignment);
    }

    private static String require(Dotenv dotenv, String name) {
        String value = dotenv.get(name);
        if (value == null || value.isBlank() || value.startsWith("your-")) {
            throw new IllegalStateException(
                name + " is missing or still set to the placeholder in your .env file. "
                + "Grab the real value from your BRIDGES profile page.");
        }
        return value.trim();
    }

    public String getUsername() {
        return username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getAssignmentNumber() {
        return assignmentNumber;
    }

    /**
     * Safe to print. Shows the first four characters of the key, which is
     * enough to tell whether the right one loaded and not enough to be worth
     * stealing out of your terminal history.
     */
    public String describe() {
        String masked = apiKey.length() <= 4
            ? "****"
            : apiKey.substring(0, 4) + "*".repeat(apiKey.length() - 4);
        return "username=" + username + ", apiKey=" + masked + ", assignment=" + assignmentNumber;
    }
}

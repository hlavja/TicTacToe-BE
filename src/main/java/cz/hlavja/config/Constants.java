package cz.hlavja.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String STANDARD_GAME = "STANDARD_GAME";

    public static final String RUNNING_GAME = "RUNNING";
    public static final String ENDED_GAME = "ENDED";
    public static final String WON_GAME = "WON";
    public static final String DRAFT_GAME = "DRAFT";

    public static final String ADD_FRIEND = "ADD_FRIEND";
    public static final String GAME_CHALLENGE = "GAME_CHALLENGE";
    public static final String GAME_ACCEPTED = "GAME_ACCEPTED";
    public static final String GAME_REJECTED = "GAME_REJECTED";

    private Constants() {
    }
}

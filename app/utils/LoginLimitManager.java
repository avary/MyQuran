package utils;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles recording admin console login attempts and handling temporary lockouts where necessary.
 *
 * @author Daniel Henninger
 */
public class LoginLimitManager {

    // Wrap this guy up so we can mock out the LoginLimitManager class.
    private static class LoginLimitManagerContainer {
        private static LoginLimitManager instance = new LoginLimitManager();
    }

    /**
     * Returns a singleton instance of LoginLimitManager.
     *
     * @return a LoginLimitManager instance.
     */
    public static LoginLimitManager getInstance() {
        return LoginLimitManagerContainer.instance;
    }
    // Max number of attempts per ip address that can be performed in given time frame
    private long maxAttemptsPerIP;
    // Max number of attempts per username that can be performed in a given time frame
    private long maxAttemptsPerUsername;
    // Record of attempts per IP address
    private Map<String, Long> attemptsPerIP;
    // Record of attempts per username
    private Map<String, Long> attemptsPerUsername;

    /**
     * Constructs a new login limit manager.
     */
    private LoginLimitManager() {
        // Set up initial maps
        attemptsPerIP = new ConcurrentHashMap<String, Long>();
        attemptsPerUsername = new ConcurrentHashMap<String, Long>();

        // Max number of attempts per ip address that can be performed in given time frame (10 attempts default)
        maxAttemptsPerIP = 5;
        // Max number of attempts per username that can be performed in a given time frame (10 attempts default)
        maxAttemptsPerUsername = 5;
    }

    public void clearBadAttempt(){
        this.attemptsPerIP.clear();
        this.attemptsPerUsername.clear();
    }

    /**
     * Returns true of the entered username or connecting IP address has hit it's attempt limit.
     *
     * @param username Username being checked.
     * @param address IP address that is connecting.
     * @return True if the login attempt limit has been hit.
     */
    public boolean hasHitConnectionLimit(String username, String address) {
        if (attemptsPerIP.get(address) != null && attemptsPerIP.get(address) > maxAttemptsPerIP) {
            return true;
        }
        if (attemptsPerUsername.get(username) != null && attemptsPerUsername.get(username) > maxAttemptsPerUsername) {
            return true;
        }
        // No problem then, no limit hit.
        return false;
    }

    /**
     * Records a failed connection attempt.
     *
     * @param username Username being attempted.
     * @param address IP address that is attempting.
     */
    public void recordFailedAttempt(String username, String address) {

        Long cnt = (long) 0;
        if (attemptsPerIP.get(address) != null) {
            cnt = attemptsPerIP.get(address);
        }
        cnt++;
        attemptsPerIP.put(address, cnt);

        cnt = (long) 0;
        if (attemptsPerUsername.get(username) != null) {
            cnt = attemptsPerUsername.get(username);
        }
        cnt++;
        attemptsPerUsername.put(username, cnt);
    }

    /**
     * Clears failed login attempts if a success occurs.
     *
     * @param username Username being attempted.
     * @param address IP address that is attempting.
     */
    public void recordSuccessfulAttempt(String username, String address) {
        attemptsPerIP.remove(address);
        attemptsPerUsername.remove(username);
    }

}


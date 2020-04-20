package com.gms.appconfiguration.security;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class LoginPayloadSample {

    /**
     * Username or email from the user.
     */
    private String usernameOrEmail;
    /**
     * User's password.
     */
    private String password;

    /**
     * Creates a {@link LoginPayloadSample} from the given arguments.
     *
     * @param usernameOrEmail User's username or email
     * @param password        User's password.
     */
    public LoginPayloadSample(final String usernameOrEmail, final String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    /**
     * Creates a {@link LoginPayloadSample}.
     */
    public LoginPayloadSample() {
    }

    /**
     * Returns the user's email or username.
     *
     * @return String
     */
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    /**
     * Returns the user's password.
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password User's password to be set.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Sets the user's email or username.
     *
     * @param usernameOrEmail User's username or email to be set.
     */
    public void setUsernameOrEmail(final String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

}

package org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.abstracts.exception;

/**
 * The class Authentication exception is thrown when something went wrong during authentication
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Instantiates a new Authentication exception.
     *
     * @param message the message
     */
    public AuthenticationException(String message) {
        super(message);
    }

}

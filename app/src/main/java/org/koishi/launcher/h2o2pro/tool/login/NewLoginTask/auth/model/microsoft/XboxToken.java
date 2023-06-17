package org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.microsoft;

/**
 * The class xbox token has the same functions as {@link XboxLiveToken}
 */
public class XboxToken extends XboxLiveToken {

    /**
     * Instantiates a new Xbox token.
     */
    public XboxToken() {
    }

    /**
     * Instantiates a new Xbox token.
     *
     * @param token the token
     * @param uhs   the uhs
     */
    public XboxToken(String token, String uhs) {
        super(token, uhs);
    }

}

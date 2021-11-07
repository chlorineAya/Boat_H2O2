package org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang;

/**
 * The class Minecraft token stores minecraft accessToken and username
 */
public class MinecraftToken {

    private String accessToken;
    private String username;

    /**
     * Instantiates a new Minecraft token.
     */
    public MinecraftToken() {
    }

    /**
     * Instantiates a new Minecraft token.
     *
     * @param accessToken the access token
     * @param username    the username
     */
    public MinecraftToken(String accessToken, String username) {
        this.accessToken = accessToken;
        this.username = username;
    }

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "MinecraftToken{" +
                "accessToken='" + accessToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

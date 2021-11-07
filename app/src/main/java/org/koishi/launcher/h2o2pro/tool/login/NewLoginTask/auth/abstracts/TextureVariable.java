package org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.abstracts;

import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang.profile.MinecraftProfile;

/**
 * The type Texture variable stores data from {@link MinecraftProfile}
 */
public abstract class TextureVariable {

    private String id;
    private String state;
    private String url;
    private String alias;

    /**
     * Instantiates a new Texture variable.
     */
    public TextureVariable() {
    }

    /**
     * Instantiates a new Texture variable.
     *
     * @param id    the id
     * @param state the state
     * @param url   the url
     * @param alias the alias
     */
    public TextureVariable(String id, String state, String url, String alias) {
        this.id = id;
        this.state = state;
        this.url = url;
        this.alias = alias;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return "TextureVariable{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", url='" + url + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}

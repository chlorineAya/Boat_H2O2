package org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang.profile;

import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.abstracts.TextureVariable;

/**
 * The class Minecraft skin stores cape data from {@link MinecraftProfile}
 */
public class MinecraftSkin extends TextureVariable {

    private String variant;

    /**
     * Instantiates a new Minecraft skin.
     */
    public MinecraftSkin() {
    }

    /**
     * Instantiates a new Minecraft skin.
     *
     * @param variant the variant
     */
    public MinecraftSkin(String variant) {
        this.variant = variant;
    }

    /**
     * Instantiates a new Minecraft skin.
     *
     * @param id      the id
     * @param state   the state
     * @param url     the url
     * @param alias   the alias
     * @param variant the variant
     */
    public MinecraftSkin(String id, String state, String url, String alias, String variant) {
        super(id, state, url, alias);
        this.variant = variant;
    }

    /**
     * Gets variant.
     *
     * @return the variant
     */
    public String getVariant() {
        return variant;
    }


    @Override
    public String toString() {
        return "MinecraftSkin{" +
                "id='" + getId() + '\'' +
                ", state='" + getState() + '\'' +
                ", url='" + getUrl() + '\'' +
                ", alias='" + getAlias() + '\'' +
                "variant='" + variant + '\'' +
                '}';
    }
}

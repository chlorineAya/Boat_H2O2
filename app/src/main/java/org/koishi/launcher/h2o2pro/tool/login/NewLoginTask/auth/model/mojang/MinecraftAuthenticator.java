package org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang;

import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.abstracts.Authenticator;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.abstracts.exception.AuthenticationException;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.microsoft.MicrosoftAuthenticator;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.microsoft.XboxToken;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang.profile.MinecraftCape;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang.profile.MinecraftProfile;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang.profile.MinecraftSkin;
import org.koishi.launcher.h2o2pro.tool.json.model.JsonArray;
import org.koishi.launcher.h2o2pro.tool.json.model.JsonObject;
import org.koishi.launcher.h2o2pro.tool.json.model.JsonString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The class Minecraft authenticator is used to log in with normal minecraft details or with microsoft
 */
public class MinecraftAuthenticator extends Authenticator<MinecraftToken> {

    /**
     * The Microsoft authenticator is used for {@link #loginWithXbox(String, String)}
     */
    protected final MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();

    @Override
    public MinecraftToken login(String email, String password) {
        throw new RuntimeException("Not implemented yet.");
    }

    /**
     * Login with microsoft email and microsoft password
     *
     * @param email    the email
     * @param password the password
     * @return the minecraft token
     */
    public MinecraftToken loginWithXbox(String email, String password) {
        XboxToken xboxToken = microsoftAuthenticator.login(email, password);

        try {
            URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            JsonObject request = new JsonObject();
            request.add("identityToken", new JsonString("XBL3.0 x=" + xboxToken.getUhs() + ";" + xboxToken.getToken()));

            String requestBody = request.toString();

            httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Host", "api.minecraftservices.com");
            httpURLConnection.connect();

            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
            }

            JsonObject jsonObject = microsoftAuthenticator.parseResponseData(httpURLConnection);
            return new MinecraftToken(jsonObject.get("access_token", JsonString.class).getValue(), jsonObject.get("username", JsonString.class).getValue());
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    /**
     * Check ownership from {@link MinecraftToken} and generate {@link MinecraftProfile}
     *
     * @param minecraftToken the minecraft token
     * @return the minecraft profile
     */
    public MinecraftProfile checkOwnership(MinecraftToken minecraftToken) {
        try {
            URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.setRequestProperty("Authorization", "Bearer " + minecraftToken.getAccessToken());
            httpURLConnection.setRequestProperty("Host", "api.minecraftservices.com");
            httpURLConnection.connect();

            JsonObject jsonObject = parseResponseData(httpURLConnection);

            UUID uuid = generateUUID(jsonObject.get("id", JsonString.class).getValue());
            String name = jsonObject.get("name", JsonString.class).getValue();
            List<MinecraftSkin> minecraftSkins = json.fromJson(jsonObject.get("skins", JsonArray.class), List.class, MinecraftSkin.class);
            List<MinecraftCape> minecraftCapes = json.fromJson(jsonObject.get("skins", JsonArray.class), List.class, MinecraftCape.class);

            return new MinecraftProfile(uuid, name, minecraftSkins, minecraftCapes);
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    /**
     * Parse response data to {@link JsonObject}
     *
     * @param httpURLConnection the http url connection
     * @return the json object
     * @throws IOException the io exception
     */
    public JsonObject parseResponseData(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader bufferedReader;

        if (httpURLConnection.getResponseCode() != 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        }
        String lines = bufferedReader.lines().collect(Collectors.joining());

        JsonObject jsonObject = json.fromJsonString(lines, JsonObject.class);
        if (jsonObject.has("error")) {
            throw new AuthenticationException(String.format("Could not find profile!. Error: '%s'", jsonObject.get("errorMessage", JsonString.class).getValue()));
        }
        return jsonObject;
    }


    /**
     * Generate uuid from trimmedUUID
     *
     * @param trimmedUUID the trimmed uuid
     * @return the uuid
     * @throws IllegalArgumentException the illegal argument exception
     */
    public UUID generateUUID(String trimmedUUID) throws IllegalArgumentException {
        if (trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
            return UUID.fromString(builder.toString());
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }

}

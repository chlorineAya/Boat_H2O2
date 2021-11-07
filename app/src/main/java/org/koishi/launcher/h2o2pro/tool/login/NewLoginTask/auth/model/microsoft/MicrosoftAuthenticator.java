package org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.microsoft;

import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.model.mojang.MinecraftToken;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.abstracts.Authenticator;
import org.koishi.launcher.h2o2pro.tool.login.NewLoginTask.auth.abstracts.exception.AuthenticationException;
import org.koishi.launcher.h2o2pro.tool.json.model.JsonArray;
import org.koishi.launcher.h2o2pro.tool.json.model.JsonObject;
import org.koishi.launcher.h2o2pro.tool.json.model.JsonString;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The class Microsoft authenticator is used to log in into microsoft account and generate mojang JWT token
 */
public class MicrosoftAuthenticator extends Authenticator<XboxToken> {

    protected final String clientId = "00000000402b5328";
    protected final String scopeUrl = "service::user.auth.xboxlive.com::MBI_SSL";

    protected String loginUrl;
    protected String loginCookie;
    protected String loginPPFT;

    @Override
    public XboxToken login(String email, String password) {
        MicrosoftToken microsoftToken = generateTokenPair(generateLoginCode(email, password));
        XboxLiveToken xboxLiveToken = generateXboxTokenPair(microsoftToken);
        return generateXboxTokenPair(xboxLiveToken);
    }

    /**
     * Generate start login code from email and password
     *
     * @param email    microsoft email
     * @param password microsoft password
     * @return login code
     */
    private String generateLoginCode(String email, String password) {
        try {
            URL url = new URL("https://login.live.com/oauth20_authorize.srf?redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=" + scopeUrl + "&display=touch&response_type=code&locale=en&client_id=" + clientId);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getResponseCode() == 200 ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();

            loginCookie = httpURLConnection.getHeaderField("set-cookie");

            String responseData = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining());
            Matcher bodyMatcher = Pattern.compile("sFTTag:[ ]?'.*value=\"(.*)\"/>'").matcher(responseData);
            if (bodyMatcher.find()) {
                loginPPFT = bodyMatcher.group(1);
            } else {
                throw new AuthenticationException("Authentication error. Could not find 'LOGIN-PFTT' tag from response!");
            }

            bodyMatcher = Pattern.compile("urlPost:[ ]?'(.+?(?='))").matcher(responseData);
            if (bodyMatcher.find()) {
                loginUrl = bodyMatcher.group(1);
            } else {
                throw new AuthenticationException("Authentication error. Could not find 'LOGIN-URL' tag from response!");
            }

            if (loginCookie == null || loginPPFT == null || loginUrl == null)
                throw new AuthenticationException("Authentication error. Error in authentication process!");

        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }

        return sendCodeData(email, password);
    }

    /**
     * Send code data from email and password
     *
     * @param email    microsoft email
     * @param password microsoft password
     * @return login code
     */
    private String sendCodeData(String email, String password) {
        String authToken;

        Map<String, String> requestData = new HashMap<>();

        requestData.put("login", email);
        requestData.put("loginfmt", email);
        requestData.put("passwd", password);
        requestData.put("PPFT", loginPPFT);

        String postData = encodeURL(requestData);

        try {
            byte[] data = postData.getBytes(StandardCharsets.UTF_8);

            HttpURLConnection connection = (HttpURLConnection) new URL(loginUrl).openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setRequestProperty("Cookie", loginCookie);

            connection.setDoInput(true);
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(data);
            }

            if (connection.getResponseCode() != 200 || connection.getURL().toString().equals(loginUrl)) {
                throw new AuthenticationException("Authentication error. Username or password is not valid.");
            }

            Pattern pattern = Pattern.compile("[?|&]code=([\\w.-]+)");

            Matcher tokenMatcher = pattern.matcher(URLDecoder.decode(connection.getURL().toString(), StandardCharsets.UTF_8.name()));
            if (tokenMatcher.find()) {
                authToken = tokenMatcher.group(1);
            } else {
                throw new AuthenticationException("Authentication error. Could not handle data from response.");
            }
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }

        this.loginUrl = null;
        this.loginCookie = null;
        this.loginPPFT = null;

        return authToken;
    }


    /**
     * Send xbox auth request
     */
    private void sendXboxRequest(HttpURLConnection httpURLConnection, JsonObject request, JsonObject properties) throws IOException {
        request.add("Properties", properties);

        String requestBody = request.toString();

        httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.connect();
        try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
            outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
        }
    }

    /**
     * Generate {@link MinecraftToken}
     *
     * @param authToken from {@link #generateLoginCode(String, String)}
     * @return {@link MinecraftToken}
     */
    private MicrosoftToken generateTokenPair(String authToken) {
        try {
            Map<String, String> arguments = new HashMap<>();
            arguments.put("client_id", clientId);
            arguments.put("code", authToken);
            arguments.put("grant_type", "authorization_code");
            arguments.put("redirect_uri", "https://login.live.com/oauth20_desktop.srf");
            arguments.put("scope", scopeUrl);

            StringJoiner argumentBuilder = new StringJoiner("&");
            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                argumentBuilder.add(encodeURL(entry.getKey()) + "=" + encodeURL(entry.getValue()));
            }

            byte[] data = argumentBuilder.toString().getBytes(StandardCharsets.UTF_8);

            URL url = new URL("https://login.live.com/oauth20_token.srf");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setFixedLengthStreamingMode(data.length);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.connect();

            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(data);
            }

            JsonObject jsonObject = parseResponseData(httpURLConnection);

            return new MicrosoftToken(jsonObject.get("access_token", JsonString.class).getValue(), jsonObject.get("refresh_token", JsonString.class).getValue());

        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    /**
     * Generate {@link XboxLiveToken} from {@link MicrosoftToken}
     *
     * @param microsoftToken the microsoft token
     * @return the xbox live token
     */
    public XboxLiveToken generateXboxTokenPair(MicrosoftToken microsoftToken) {
        try {
            URL url = new URL("https://user.auth.xboxlive.com/user/authenticate");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setDoOutput(true);

            JsonObject request = new JsonObject();
            request.add("RelyingParty", new JsonString("http://auth.xboxlive.com"));
            request.add("TokenType", new JsonString("JWT"));

            JsonObject properties = new JsonObject();
            properties.add("AuthMethod", new JsonString("RPS"));
            properties.add("SiteName", new JsonString("user.auth.xboxlive.com"));
            properties.add("RpsTicket", new JsonString(microsoftToken.getToken()));

            sendXboxRequest(httpURLConnection, request, properties);

            JsonObject jsonObject = parseResponseData(httpURLConnection);

            String uhs = ((JsonObject) (jsonObject.get("DisplayClaims", JsonObject.class)).get("xui", JsonArray.class).get(0)).get("uhs", JsonString.class).getValue();
            return new XboxLiveToken(jsonObject.get("Token", JsonString.class).getValue(), uhs);
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }


    /**
     * Generate {@link XboxToken} from {@link XboxLiveToken}
     *
     * @param xboxLiveToken the xbox live token
     * @return the xbox token
     */
    public XboxToken generateXboxTokenPair(XboxLiveToken xboxLiveToken) {
        try {
            URL url = new URL("https://xsts.auth.xboxlive.com/xsts/authorize");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            JsonObject request = new JsonObject();
            request.add("RelyingParty", new JsonString("rp://api.minecraftservices.com/"));
            request.add("TokenType", new JsonString("JWT"));

            JsonObject properties = new JsonObject();
            properties.add("SandboxId", new JsonString("RETAIL"));
            JsonArray userTokens = new JsonArray();
            userTokens.add(new JsonString(xboxLiveToken.getToken()));
            properties.add("UserTokens", userTokens);

            sendXboxRequest(httpURLConnection, request, properties);

            if (httpURLConnection.getResponseCode() == 401) {
                throw new AuthenticationException("No xbox account was found!");
            }

            JsonObject jsonObject = parseResponseData(httpURLConnection);

            String uhs = ((JsonObject) (jsonObject.get("DisplayClaims", JsonObject.class)).get("xui", JsonArray.class).get(0)).get("uhs", JsonString.class).getValue();
            return new XboxToken(jsonObject.get("Token", JsonString.class).getValue(), uhs);
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
            throw new AuthenticationException(jsonObject.get("error") + ": " + jsonObject.get("error_description"));
        }
        return jsonObject;
    }


    /**
     * Encode url string.
     *
     * @param url the url
     * @return the string
     */
    private String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }


    /**
     * Encode url string.
     *
     * @param map the map
     * @return the string
     */
    private String encodeURL(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    encodeURL(entry.getKey().toString()),
                    encodeURL(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }


}

package net.gameinbox.voidserver.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MojangAuthenticator {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static MojangPlayer registerHasJoined(String username, String serverId) throws IOException {
        HttpUrl httpUrl = HttpUrl.parse("https://sessionserver.mojang.com/session/minecraft/hasJoined").newBuilder()
                .addQueryParameter("username", username)
                .addQueryParameter("serverId", "")
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Response response = HTTP_CLIENT.newCall(request).execute();

        Gson gson = new Gson();

        System.out.println(request.url().url().toString());
        System.out.println(response.code());
        System.out.println(response.body().string());

        //JsonObject object = gson.fromJson(content.toString(), JsonObject.class);
        //System.out.println(object.get("id"));

        return null;
    }

}

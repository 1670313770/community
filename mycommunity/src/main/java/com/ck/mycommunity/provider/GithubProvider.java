package com.ck.mycommunity.provider;

import com.ck.mycommunity.pojo.AccessTokenPojo;
import com.ck.mycommunity.pojo.GithubUser;
import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author CK
 * @create 2020-01-23-16:25
 */
@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenPojo accessTokenPojo){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, new Gson().toJson(accessTokenPojo));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            string=string.split("&")[0].split("=")[1];
//            System.out.println(string);
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser githubUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GithubUser githubUser = new Gson().fromJson(string, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

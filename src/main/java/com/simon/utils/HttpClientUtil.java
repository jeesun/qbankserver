package com.simon.utils;

import com.google.gson.Gson;
import com.simon.domain.token.AccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 2017/3/3.
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String post(String clientId, String clientSecret, String uri, Map<String,String> map, String encoding) throws IOException {

        String credentials = clientId + ":" + clientSecret;
        final String basic = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));
        log.info(basic);
        String body="";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        List<NameValuePair> nvps = new ArrayList<>();
        if(null!=map){
            for(Map.Entry<String, String> entry : map.entrySet()){
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
        httpPost.setHeader("Authorization", basic);
        httpPost.setHeader("Accept", "application/json;charset=UTF-8");

        CloseableHttpResponse response = client.execute(httpPost);

        HttpEntity entity = response.getEntity();

        if(null!=entity){
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        response.close();
        log.info("body=" + body);
        return body;

    }

    public static AccessToken postAndGetToken(String clientId, String clientSecret, String uri, Map<String,String> map, String encoding) throws IOException{
        Gson gson = new Gson();
        return gson.fromJson(post(clientId, clientSecret, uri, map, encoding), AccessToken.class);
    }
}

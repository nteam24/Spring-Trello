package com.sparta.springusersetting.domain.notification.slack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class SlackChatUtil {

    @Autowired
    SystemProperties systemProperties;

    public void sendSlackErr(String errStr) throws IOException {
        String token = systemProperties.getSlackToken();
        String channelId = systemProperties.getSlackChannelId();
        boolean isErrorFlag = systemProperties.isErrorFlag();

        String text = "자바 시스템 오류 발생!\n오류내용: " + errStr;

        String urlStr = "https://slack.com/api/chat.postMessage";
        urlStr += "?channel="+channelId;
        urlStr += "&text="+ URLEncoder.encode(text, "UTF-8");

        if(isErrorFlag==false) {

            HttpURLConnection conn = null;
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer "+token);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer response = new StringBuffer();

            if (conn.getResponseCode()==200) {
                response.append(br.readLine());
                if (response!=null) {
                    if (String.valueOf(response).contains("\"ok\":true")) {
                        systemProperties.setErrorFlag(true);
                        System.out.println("슬랙 메시지 발송 성공");
                    }else {
                        System.out.println("슬랙 메시지 발송 실패");
                    }
                }
            } else {
                System.out.println("슬랙 API 오류 발생 code: " + conn.getResponseCode() + " message: " + conn.getResponseMessage());
            }

            br.close();
            conn.disconnect();
        }
    }
}


package com.sparta.springusersetting.domain.notificationsendtest;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SlackSendMessageTest {


    @Test
    void sendTest() {

        try {
            sendSlack();
        } catch (Exception e) {
            System.out.println("sendTest Error occurred err: " + e.toString());
        }
    }

    public void sendSlack() throws IOException {
        String urlStr = "https://slack.com/api/chat.postMessage";
        // xoxb- 로 시작하는 API 토큰값 설정
        String token = "xoxb-7880078409587-7877498475125-iGOu4iBv1hcRWcwilsG8fAv2";
        // 채널 ID값 설정
        String channelId = "D07SK4F2RPA";
        // 전송하고자 하는 메시지값 설정
        String text = "자바 테스터로 메시지 전송 테스트";

        urlStr += "?channel="+channelId;
        urlStr += "&text="+ URLEncoder.encode(text, "UTF-8");

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


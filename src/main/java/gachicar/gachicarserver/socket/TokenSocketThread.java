package gachicar.gachicarserver.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TokenSocketThread implements Runnable {

    private Socket tokenSocket;                     // 토큰 서버와의 소켓 연결
    private PrintWriter tokenWriter;
    private BufferedReader tokenReader;

    @Override
    public void run() {
        connectToTokenServer();
    }

    /**
     * TokenServer에 연결
     */
    private void connectToTokenServer() {
        try {
            // 토큰 추출 서버 IP 주소
            String tokenIpAddress = "localhost";
            // 토큰 추출 서버의 포트 번호
            int tokenPort = 9999;

            tokenSocket = new Socket(tokenIpAddress, tokenPort);
            tokenWriter = new PrintWriter(tokenSocket.getOutputStream(), true);
            tokenReader = new BufferedReader(new InputStreamReader(tokenSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 메시지를 보내고 응답을 받는 메서드
    public String sendAndReceiveFromTokenServer(String message) {
        if (tokenWriter != null) {
            tokenWriter.println(message);
            System.out.println("Sent to Token Server: " + message);
            try {
                return tokenReader.readLine(); // 토큰 서버로부터 응답을 받음
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Failed to send message to Token Server. Token socket is not available.");
            return null;
        }
    }

    /**
     * Token Server Socket 닫기
     */
    public void closeTokenSocket() {
        if (tokenSocket != null && !tokenSocket.isClosed()) {
            try {
                tokenSocket.close();
                System.out.println("Closed Token Server socket");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
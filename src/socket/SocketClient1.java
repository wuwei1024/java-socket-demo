package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient1 {

    public static void main(String[] args) {
        try {
            //构建客户端socket对象
            Socket s = new Socket("localhost", 8888);
            //构建输入流，发送消息到服务端
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("客户端说: client1");
            pw.flush();
            //构建输入流，接收服务端消息
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                String msg = null;
                if ((msg = br.readLine()) != null) {
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
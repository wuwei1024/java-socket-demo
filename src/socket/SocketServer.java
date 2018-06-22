package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {
    //所有的在线客户端socket
    private static final List<Socket> list = Collections.synchronizedList(new LinkedList<>());
    private static final Logger logger = Logger.getLogger(SocketServer.class.getName());
    /**
     * 获取所有的在线客户端socket
     *
     * @return
     */
    public static List<Socket> getSocketList() {
        return list;
    }

    /**
     * 开启服务端socket监听
     */
    public static void startServerSocket() {
        try {
            ServerSocket ss = new ServerSocket(8888);
            //循环监听
            while (true) {
                //监听客户端上线，阻塞等待
                Socket s = ss.accept();
                list.add(s);
                String ip = s.getInetAddress().getHostAddress();
                System.err.println(ip + " 用户上线了 , 当前在线用户为: " + list.size() + "人 !");
                //启动新的线程，监听客户端的在线状态
                new Thread(() -> listenClientStatus(s)).start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * 监听客户端连接状态，如果客户端断开，移除该连接
     *
     * @param s
     */
    public static void listenClientStatus(Socket s) {
        String ip = s.getInetAddress().getHostAddress();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                String msg = null;
                //接收客户端消息
                if ((msg = reader.readLine()) != null) {
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            //用户下线断开连接
            list.remove(s);
            System.err.println(ip + "已下线 , 当前在线人数为: " + list.size() + " 人 !");
        }
    }
}
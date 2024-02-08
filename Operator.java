package me;

import Chatlist1.RoomList;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class Operator {
   
   private Socket socket; // 클라이언트 소켓을 저장할 변수
   
   Database db = null;
   MainFrame mf = null;
   JoinFrame jf = null;
   RoomList chatlist = null;
   
   public static void main(String[] args) {
      Operator opt = new Operator();
      opt.db = new Database();
      opt.mf = new MainFrame(opt);
      opt.jf = new JoinFrame(opt);
   }
   
   // 로그인 성공 후 ChatlistApp 객체 생성하는 메서드
    public void createChatlistApp() {
        chatlist = new RoomList();
    }
    
    public boolean connectToServer(String ipAddress, int port) {
        try {
            // 서버에 접속하는 코드
            socket = new Socket(ipAddress, port);
            return true; // 접속 성공
        } catch (IOException e) {
            e.printStackTrace();
            return false; // 접속 실패
        }
    }

    public void showMainFrame() {
        SwingUtilities.invokeLater(() -> {
            new MainFrame(Operator.this);
        });
    }
}
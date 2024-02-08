package me;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

import Chatlist1.CreateRoom;
import Chatlist1.KimsPayConsoleApp;


public class MultiChatClient implements ActionListener, Runnable {
   private static final int LOGIN = 100; 
   private static final int LOGOUT = 200;
   private static final int EXIT = 300;
   private static final int  NOMAL = 400;;
   private static final int WISPER = 500;;
   private static final int VAN = 600;
   private static final int CPLIST= 700;
   private static final int ERR_DUP = 800;
   
   
   private String ip;
   private String id;
   private String contents;
   private Socket socket;
   private BufferedReader inMsg = null;
   private PrintWriter outMsg = null;

   private JPanel loginPanel;
   private JButton loginButton;
   private JLabel label1;
   private JTextField idInput;

   private JPanel logoutPanel;
   private JLabel label2;
   private JButton logoutButton;

   private JPanel msgPanel;
   private JTextField msgInput;
   private JButton exitButton;

   private JFrame jframe;
   private JTextArea msgOut;
   
   private JPanel chatpListPanel;
   private JLabel label3;
   private JTextArea listOut;

   private Container tab;
   private CardLayout clayout;
   private Thread thread;

   boolean status;
   
   // 클래스 멤버 변수로 선언
   private JMenuItem memberInfoMenuItem;
   private JMenuItem deleteAccountMenuItem;
   private JMenuItem inviteFriendMenuItem; // "친구 초대하기" 메뉴 아이템 추가
   private JMenuItem sendAccount; // "계좌번호 보내기" 메뉴 아이템 추가

       public MultiChatClient(String ip, String roomName) {
           this.ip = ip;
           
           
           loginPanel = new JPanel();
           loginPanel.setLayout(new BorderLayout());
           idInput = new JTextField(15);
           loginButton = new JButton("닉네임");
           loginButton.addActionListener(this);
           label1 = new JLabel("대화명");

           loginPanel.add(label1, BorderLayout.WEST);
           loginPanel.add(idInput, BorderLayout.CENTER);
           loginPanel.add(loginButton, BorderLayout.EAST);

     
           logoutPanel = new JPanel();

           logoutPanel.setLayout(new BorderLayout());
           label2 = new JLabel();
           logoutButton = new JButton("로그아웃");

           logoutButton.addActionListener(this);
    
           logoutPanel.add(label2, BorderLayout.CENTER);
           logoutPanel.add(logoutButton, BorderLayout.EAST);

    
           msgPanel = new JPanel();
    
           msgPanel.setLayout(new BorderLayout());
           msgInput = new JTextField(30);
    
           msgInput.addActionListener(this);
           msgInput.setEditable(false); //로그인 하기 전에는 채팅입력 불가
           exitButton = new JButton("종료");
           exitButton.addActionListener(this);
     
           msgPanel.add(msgInput, BorderLayout.CENTER);
           msgPanel.add(exitButton, BorderLayout.EAST);

           tab = new JPanel();
           clayout = new CardLayout();
           tab.setLayout(clayout);
           tab.add(loginPanel, "Nicknamelogin");
           tab.add(logoutPanel, "logout");


           jframe = new JFrame("Kim's Talk");
           msgOut = new JTextArea("", 10, 30);
           
           msgOut.setEditable(false);
           
           JScrollPane jsp = new JScrollPane(msgOut,
                 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
           
           
           chatpListPanel = new JPanel(); //채팅 참자가 리스트가 붙을 패널
           chatpListPanel.setLayout(new BorderLayout());
           
           label3 = new JLabel("채팅 참가자"); // 라벨
           listOut =new JTextArea("",10,10); //채팅참가자를 나타낼 영역
           listOut.setEditable(false); //편집불가
           JScrollPane jsp2 = new JScrollPane(listOut,
                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
           chatpListPanel.add(label3,BorderLayout.NORTH); //패널에 라벨과 스크롤을 갖다 붙임
           chatpListPanel.add(jsp2,BorderLayout.CENTER);
           
           jframe.add(tab, BorderLayout.NORTH);
           jframe.add(jsp, BorderLayout.WEST);
           jframe.add(chatpListPanel,BorderLayout.EAST);
           jframe.add(msgPanel, BorderLayout.SOUTH);
          
           clayout.show(tab, "Nicknamelogin");
           
           jframe.pack();
           
           jframe.setResizable(false);
           
           jframe.setVisible(true);
           
        // 메뉴 바 생성
           JMenuBar menuBar = new JMenuBar();
           // 메뉴 생성
           JMenu menu = new JMenu("메뉴");

           JMenuItem drawPictureMenuItem = null;
		// "회원정보" 메뉴 아이템에 대한 액션 리스너
           ActionListener menuActionListener = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
            	   final JMenuItem drawPictureMenuItem = new JMenuItem("그림판");
            	   
                   // "회원정보" 창 열기
                   if (e.getSource() == memberInfoMenuItem) {
                       openMemberInfoDialog();
                   } else if (e.getSource() == deleteAccountMenuItem) {
                       // "계정 삭제" 창 열기 (여기에 확인 창 띄우는 로직 추가)
                       int result = JOptionPane.showConfirmDialog(
                               jframe,
                               "계정을 삭제하시겠습니까?",
                               "계정 삭제 확인",
                               JOptionPane.YES_NO_OPTION);

                    // 사용자의 선택 확인
                       if (result == JOptionPane.YES_OPTION) {
                           // 사용자가 '예'를 선택했을 때 계정 삭제 로직 수행
                           deleteAccount(id); // id는 현재 로그인한 사용자의 ID
                       } else {
                           // 사용자가 '아니오'를 선택하거나 대화상자를 닫았을 때는 아무 작업도 하지 않음
                       }
                   } else if (e.getSource() == drawPictureMenuItem) {
                	   startSketchpad();
                   }
               }
               
            // 그림판 실행 메서드
			private void startSketchpad() {
				try {
			        // 실행할 명령어 및 경로
			        String command = "java -cp . SketchpadServer";
			        
			        // 프로세스 실행
			        Process process = Runtime.getRuntime().exec(command);

			        // 여기에는 SketchpadClient.java를 실행하는 코드를 추가해야 합니다.
			    } catch (IOException ex) {
			        ex.printStackTrace();
			    }
				
			}

           };
           // "회원정보" 메뉴 아이템 생성
           memberInfoMenuItem = new JMenuItem("회원정보 수정");
           memberInfoMenuItem.addActionListener(menuActionListener);
           menu.add(memberInfoMenuItem);

           // "계정 삭제" 메뉴 아이템 생성
           deleteAccountMenuItem = new JMenuItem("계정 삭제");
           deleteAccountMenuItem.addActionListener(menuActionListener);
           menu.add(deleteAccountMenuItem);

           // "그림판" 메뉴 아이템 생성
           drawPictureMenuItem = new JMenuItem("그림판");
           drawPictureMenuItem.addActionListener(menuActionListener);
           menu.add(drawPictureMenuItem);

           menuBar.add(menu);
           jframe.setJMenuBar(menuBar);
           
        // "계좌번호 보내기" 메뉴 아이템에 대한 액션 리스너
           ActionListener sendAccountActionListener = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {

                   // "계좌번호 보내기" 버튼을 눌렀을 때 KimsPayConsoleApp.java 실행
                   SwingUtilities.invokeLater(new Runnable() {
                       @Override
                       public void run() {
                           // KimsPayConsoleApp.java 실행
                    	   KimsPayConsoleApp KimsPayConsoleApp = new KimsPayConsoleApp();
                       }
                   });
               }
           };
           // "계좌번호 보내기" 메뉴 아이템 생성
           JMenuItem sendAccountMenuItem = new JMenuItem("계좌번호 보내기");

           // "계좌번호 보내기" 메뉴 아이템에 액션 리스너 등록
           sendAccountMenuItem.addActionListener(sendAccountActionListener);

           // 메뉴에 "계좌번호 보내기" 아이템 추가
           menu.add(sendAccountMenuItem);
           
        
           
        // "친구 초대하기" 메뉴 아이템에 대한 액션 리스너
           ActionListener inviteFriendActionListener = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {

                   // "친구 초대하기" 버튼을 눌렀을 때 CreateRoom.java 실행
                   SwingUtilities.invokeLater(new Runnable() {
                       @Override
                       public void run() {
                           // CreateRoom.java 실행
                           CreateRoom createRoom = new CreateRoom();
                       }
                   });
               }
           };

           inviteFriendMenuItem = new JMenuItem("친구 초대하기");
           inviteFriendMenuItem.addActionListener(inviteFriendActionListener);
           menu.add(inviteFriendMenuItem);
       }

       
       // 계정 삭제 메서드
       private void deleteAccount(String id) {
           // Database 클래스를 사용하여 MySQL에서 계정 삭제 수행
           Database database = new Database();
           boolean isDeleted = database.deleteAccount(id);

           if (isDeleted) {
               // 계정 삭제 성공
               JOptionPane.showMessageDialog(jframe, "계정이 성공적으로 삭제되었습니다.", "계정 삭제 완료", JOptionPane.INFORMATION_MESSAGE);
               // 여기에서 추가로 로그아웃 등의 작업을 수행할 수 있습니다.
           } else {
               // 계정 삭제 실패
               JOptionPane.showMessageDialog(jframe, "계정 삭제에 실패했습니다. 다시 시도해주세요.", "계정 삭제 실패", JOptionPane.ERROR_MESSAGE);
           }
       }
       
       public void connectServer() {
           try {
              
               socket = new Socket(ip, 8888); //예외발생 가능성
               System.out.println("[Client]Server 연결 성공!!");

       
               inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream())); //예외발생 가능성
               outMsg = new PrintWriter(socket.getOutputStream(), true);

       
               outMsg.println(LOGIN+"/"+id); //LOGIN 명령어로 해당 ID 출력

    
               thread = new Thread(this);
               thread.start();
           } catch(IOException e) { //해당포트에 서버가 실행하고 있지 않은 경우
               // e.printStackTrace();
               System.out.println("서버연결불가");
               if(!socket.isClosed()) {
                  stopClient();
               }
               return;
           }
       }
       public void stopClient() {
          System.out.println("연결끊음");

           msgOut.setText(""); //채팅창 비우기
           listOut.setText(" "); //참가자 창 비우기
           msgInput.setEditable(false); //채팅입력불가
           clayout.show(tab, "login"); 
           status = false;
           
           if(socket!=null && !socket.isClosed()) {
              try {
               socket.close(); //예외 발생 가능성
            } catch (IOException e) {}
           } 
          
       }
       
       public void actionPerformed(ActionEvent arg0) {
           Object obj = arg0.getSource();

    
           if(obj == exitButton) {
              outMsg.println(EXIT+"/"+id );
              stopClient();
              System.exit(0);
               
           } 
           else if(obj == loginButton) {
               id = idInput.getText().trim();
               label2.setText("대화명 : " + id);
               clayout.show(tab, "logout");
               msgInput.setEditable(true); //채팅입력 창 활성화(채팅입력 가능)
               connectServer();
           } 
           
           else if(obj == logoutButton) {
      
               outMsg.println(LOGOUT+"/"+id );   
           
              stopClient();
              
           } 
           else if(obj == msgInput) {
              Thread thread = new Thread() { 
                 //출력 쓰레드 새로생성(도배방지 기능 구현 관계상 출력 쓰레드만 sleep 시키기 위해)
                 //입력 스레드는 계속 일을 해야 채팅제한시간에도 내용이 채팅창에 추가되므로
                 @Override
                 public void run() {
                    contents = msgInput.getText();
                    //입력창의 내용 contents에 대입
                     
                     if(contents.indexOf("to")==0) { 
                        // 처음 시작이 to (예전 코드는 중간에 to가 들어갈경우 구분 불가)
                        int begin = contents.indexOf(" ") + 1;   
                        //  to 1111 안녕하세요 일 경우 처음 빈칸 다음자리부터
                        int end = contents.indexOf(" ", begin);
                        //끝자리 포함x(+1 안함)  // 다음 빈칸까지(마지막 자리는 포함 안됨)
                        String toid = contents.substring(begin, end);
                        //contents에서 해당 부분을 찾아 id에 대입
                        
                        String wisper = contents.substring(end+1); 
                        //두번째 빈칸 다음자리부터 끝까지를 뽑아서 wisper에 저장(내용)
                        outMsg.println(WISPER+"/"+id + "/"+ toid+ "/" + wisper); 
                        // 각 내용을 /로 구분해서 출력
                     }
                     else if(contents.indexOf("van")==0) { //처음 시작이 van
                        int begin = contents.indexOf(" ") + 1;  
                        //  to 1111 안녕하세요 일 경우 처음 빈칸 다음자리부터
                        String vanid = contents.substring(begin); 
                        //contents에서 해당 부분을 찾아 vanid에 대입
                        
                        outMsg.println(VAN+"/"+id + "/"+ vanid); // 각 내용을 /로 구분해서 출력
                     
                     }
                     else {
                        outMsg.println(NOMAL+"/"+id + "/" + contents);
                     
                        int len = contents.length();
                        if(len>30){
                           try {
                              msgOut.append("30자를 초과하여 도배방지를 위해 1분간 입력을 제한합니다.\n"); 
                            //해당 클라이언트에서 채팅창에 메시지 출력
                            msgInput.setText(""); //입력창 비우기
                            msgInput.setEditable(false); // 채팅입력칸 수정불가
                            Thread.sleep(60000); //60초간 재우기
                            msgInput.setEditable(true);//다시 살림
                         
                            
                           } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                         }
                           
                        } //if
                  
                     }//else
                     msgInput.setText("");
                 } //run
              };// thread
              thread.start();
              
           } //else if(obj == msgInput)
       }//action
       
       // 회원정보 수정 메서드 호출
       private void updateUserInfo(String id, String password, String name, String gender, String birth, String email, int account) {
           // 여기에서 Database 클래스의 updateUserInfo 메서드 호출
           Database database = new Database();
           database.updateUserInfo(id, password, name, gender, birth, email, account);
       }
       
       // "회원정보" 창 열기
       private void openMemberInfoDialog() {
           JFrame memberInfoFrame = new JFrame("회원정보 수정");
           memberInfoFrame.setSize(300, 350);

           JPanel memberInfoPanel = new JPanel();
           memberInfoPanel.setLayout(new BoxLayout(memberInfoPanel, BoxLayout.Y_AXIS)); // 세로 정렬

           // Label 및 TextField 추가
           JLabel idLabel = new JLabel("ID");
           JLabel pwLabel = new JLabel("Password");
           JLabel nameLabel = new JLabel("이름");
           JLabel genderLabel = new JLabel("성별");
           JLabel birthLabel = new JLabel("생년월일");
           JLabel EmailLabel = new JLabel("E-mail");
           JLabel accountLabel = new JLabel("계좌번호");

           JTextField idTextField = new JTextField("test");
           JTextField pwTextField = new JTextField();
           JTextField nameTextField = new JTextField();
           JTextField genderTextField = new JTextField();
           JTextField birthTextField = new JTextField();
           JTextField emailTextField = new JTextField();
           JTextField accountTextField = new JTextField();

           // TextField 크기 설정
           idTextField.setPreferredSize(new Dimension(160, 30));
           pwTextField.setPreferredSize(new Dimension(160, 30));
           nameTextField.setPreferredSize(new Dimension(160, 30));
           genderTextField.setPreferredSize(new Dimension(160, 30));
           birthTextField.setPreferredSize(new Dimension(160, 30));
           emailTextField.setPreferredSize(new Dimension(160, 30));
           accountTextField.setPreferredSize(new Dimension(160, 30));

           // Label 및 TextField 추가
           memberInfoPanel.add(createLabelAndFieldPanel("ID", idTextField));
           memberInfoPanel.add(createLabelAndFieldPanel("Password", pwTextField));
           memberInfoPanel.add(createLabelAndFieldPanel("이름", nameTextField));
           memberInfoPanel.add(createLabelAndFieldPanel("성별", genderTextField));
           memberInfoPanel.add(createLabelAndFieldPanel("생년월일", birthTextField));
           memberInfoPanel.add(createLabelAndFieldPanel("E-mail", emailTextField));
           memberInfoPanel.add(createLabelAndFieldPanel("계좌번호", accountTextField));

           memberInfoFrame.add(memberInfoPanel);
           memberInfoFrame.setLocationRelativeTo(null);
           memberInfoFrame.setVisible(true);
           
           int result = JOptionPane.showConfirmDialog(jframe, memberInfoPanel, "회원정보 수정", JOptionPane.OK_CANCEL_OPTION);

           if (result == JOptionPane.OK_OPTION) {
               String id = idTextField.getText();
               String password = pwTextField.getText();
               String name = nameTextField.getText();
               String gender = genderTextField.getText();
               String birth = birthTextField.getText();
               String email = emailTextField.getText();
               int account = Integer.parseInt(accountTextField.getText());

               // 회원정보 수정 메서드 호출
               updateUserInfo(id, password, name, gender, birth, email, account);
           }
       }
       
       // Label과 TextField를 포함하는 패널을 생성하는 메서드
       private JPanel createLabelAndFieldPanel(String labelText, JTextField textField) {
           JPanel panel = new JPanel();
           JLabel label = new JLabel(labelText);
           panel.add(label);
           panel.add(textField);
           return panel;
       }

       
                           
public void run() {

    String msg;
    String[] rmsg;


    status = true;

    while(status) { //수신부
        try {
       
            msg = inMsg.readLine();
            rmsg = msg.split("/");
            int commend = Integer.parseInt(rmsg[0]);
            //0번 인덱스에 있을 명령어를 INT형으로 형변환
            switch (commend) {
            
                case WISPER: { //귓속말이 온 경우
                   msgOut.append(rmsg[1] + ">>"+rmsg[2] + "\n" + rmsg[3] +"\n");
                   //귓속말을 보낸 사람과 받은사람을 채팅창에 표시   
                   break;
                
                }
            case CPLIST: { //채팅참가자 리스트가 온 경우
               String []userlist = rmsg[1].split(",");
               // 1번 인덱스에 있는 참가자 ID SET을 ,를 구분자로 하여 userlist배열에 담기 
               int size = userlist.length;
               listOut.setText(" "); //참가자 리스트창 비우기
               
               for(int i = 0;i<size;i++) { // 요소 하나씩 읽어들여서 참가자 리스트에 추가
                  listOut.append(userlist[i]);
                  listOut.append("\n");
               }
               
               break;
            }
            case VAN:{
               clayout.show(tab, "login"); //로그인버튼 바꾸기
               stopClient();
               
            }
            case ERR_DUP:{ //id 중복으로 접속이 팅겼을 경우에 처리
               
               stopClient();
               msgOut.append(rmsg[1] + ">"+rmsg[2] + "\n");
               break;
            }
            default:
               msgOut.append(rmsg[1] + ">"+rmsg[2] + "\n");
               break;
         }//switch
            
            msgOut.setCaretPosition(msgOut.getDocument().getLength());
        } catch(Exception e) {
            // e.printStackTrace();
            status = false;
        }
    }//while

    System.out.println("[MultiChatClient]" + thread.getName() + "종료됨");
}

public static void main(String[] args) {
    // Swing 작업을 실행하는 부분
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            String roomName = null;
         MultiChatClient mcc = new MultiChatClient("192.168.0.4", roomName);
        }
    });
}

}
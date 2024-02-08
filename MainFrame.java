package me;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
   
   /* Panel */
   JPanel basePanel = new JPanel(new BorderLayout());
   JPanel centerPanel = new JPanel(new BorderLayout());
   JPanel westPanel = new JPanel();
   JPanel eastPanel = new JPanel();
   JPanel southPanel = new JPanel();
   
   /* Label */
   private JLabel idL = new JLabel("아이디");
    private JLabel pwL = new JLabel("비밀번호");
   private JLabel portL = new JLabel("포트");
    private JLabel ipL = new JLabel("IP 주소");
   
   /* TextField */
    private JTextField id = new JTextField();
    private JPasswordField pw = new JPasswordField();
    private JTextField port = new JTextField("####");
    private JTextField ip = new JTextField("###.###.###.###");
   
   /* Button */
   JButton loginBtn = new JButton("로그인");
   JButton joinBtn = new JButton("회원가입");
   JButton exitBtn = new JButton("프로그램 종료");
   
   Operator o = null;
   private String serverIP;
   
   MainFrame(Operator _o){
      o = _o;
      
      setTitle("로그인");
      
      /* Panel 크기 작업 */
      centerPanel.setPreferredSize(new Dimension(260, 80));
      westPanel.setPreferredSize(new Dimension(210, 75));
      eastPanel.setPreferredSize(new Dimension(90, 75));
      southPanel.setPreferredSize(new Dimension(290, 40));
      
      /* Label 크기 작업 */
      idL.setPreferredSize(new Dimension(50, 30));
        pwL.setPreferredSize(new Dimension(50, 30));
        portL.setPreferredSize(new Dimension(50, 30));
        ipL.setPreferredSize(new Dimension(50, 30));
      
      /* TextField 크기 작업 */
        id.setPreferredSize(new Dimension(140, 30));
        pw.setPreferredSize(new Dimension(140, 30));
        port.setPreferredSize(new Dimension(140, 30));
        ip.setPreferredSize(new Dimension(140, 30));
      
      /* Button 크기 작업 */
      loginBtn.setPreferredSize(new Dimension(75, 63));
      joinBtn.setPreferredSize(new Dimension(135, 25));
      exitBtn.setPreferredSize(new Dimension(135, 25));
      
      /* Panel 추가 작업 */
      setContentPane(basePanel);   //panel을 기본 컨테이너로 설정
      
      basePanel.add(centerPanel, BorderLayout.CENTER);
      basePanel.add(southPanel, BorderLayout.SOUTH);
      centerPanel.add(westPanel, BorderLayout.WEST);
      centerPanel.add(eastPanel, BorderLayout.EAST);
      
      westPanel.setLayout(new FlowLayout());
      eastPanel.setLayout(new FlowLayout());
      southPanel.setLayout(new FlowLayout());
      
      /* westPanel 컴포넌트 */
      westPanel.setLayout(new GridLayout(4, 2));
      westPanel.add(idL);
        westPanel.add(id);
        westPanel.add(pwL);
        westPanel.add(pw);
        westPanel.add(portL);
        westPanel.add(port);
        westPanel.add(ipL);
        westPanel.add(ip);
        
        // 각 Label에 가운데 정렬 적용
        for (Component component : westPanel.getComponents()) {
            if (component instanceof JLabel) {
                ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
            } else if (component instanceof JTextField) {
                ((JTextField) component).setHorizontalAlignment(JTextField.CENTER);
            }
        }
      
      /* eastPanel 컴포넌트 */
      eastPanel.add(loginBtn);
      
      /* southPanel 컴포넌트 */
      southPanel.add(exitBtn);
      southPanel.add(joinBtn);
      
      /* Button 이벤트 리스너 추가 */
      ButtonListener bl = new ButtonListener();
      
      loginBtn.addActionListener(bl);
      exitBtn.addActionListener(bl);
      joinBtn.addActionListener(bl);
      
      setSize(350, 200);
      setLocationRelativeTo(null);
      setVisible(true);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   
   public String getId() {
      return id.getText();
   }
   
   public MainFrame(MainFrame mainFrame, String serverIP2) {
      // TODO Auto-generated constructor stub
   }

   /* Button 이벤트 리스너 */
   class ButtonListener implements ActionListener{
      @Override
      public void actionPerformed(ActionEvent e) {
         JButton b = (JButton)e.getSource();
         
         // 로그인 버튼 눌렸을 때의 동작 추가
            System.out.println("ID: " + id.getText());
            System.out.println("Password: " + new String(pw.getPassword()));
            System.out.println("Port: " + port.getText());
            System.out.println("IP: " + ip.getText());
         
         /* TextField에 입력된 아이디와 비밀번호를 변수에 초기화 */
         String uid = id.getText();
         String upass = "";
         for(int i=0; i<pw.getPassword().length; i++) {
            upass = upass + pw.getPassword()[i];
         }
         
         /* 게임종료 버튼 이벤트 */
         if(b.getText().equals("프로그램 종료")) {
            System.out.println("프로그램 종료");
            System.exit(0);
         }
         
         /* 회원가입 버튼 이벤트 */
         else if(b.getText().equals("회원가입")) {
            o.jf.setVisible(true);
         }
         
         /* 로그인 버튼 이벤트 */
         else if(b.getText().equals("로그인")) {
            if(uid.equals("") || upass.equals("")) {
               JOptionPane.showMessageDialog(null, "아이디와 비밀번호 모두 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
               System.out.println("로그인 실패 > 로그인 정보 미입력");
            }
            
            else if(uid != null && upass != null) {
               if(o.db.logincheck(uid, upass) != null) {   //이 부분이 데이터베이스에 접속해 로그인 정보를 확인하는 부분이다.
                  System.out.println("로그인 성공");
                  JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다");
                  
                  // 로그인 성공 시 ChatlistApp 생성
                     o.createChatlistApp();
                     
               } else {
                  System.out.println("로그인 실패 > 로그인 정보 불일치");
                  JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다");
               }
            }
         }
      }
   }

   public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }
   
   public void showLoginFrame() {
        SwingUtilities.invokeLater(() -> {
            new MainFrame(MainFrame.this, serverIP); // 추가된 부분
        });
    }

}
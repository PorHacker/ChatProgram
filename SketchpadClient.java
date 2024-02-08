package me;

import java.awt.*;
import java.io.*;import java.net.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;import java.util.*;
public class SketchpadClient extends JFrame implements ActionListener, ItemListener {
   
   //서버 소켓 및 입출력 스트림
   Socket sock;
   BufferedReader Receive;
   OutputStream ostream;
   InputStream istream;
   BufferedReader keyRead;
   ObjectOutputStream oos; 
   ObjectInputStream ois;
   PrintWriter pwrite;
   Object obj;
   
   //프로그램 시작 메소드
   public static void main(String[] args) throws Exception {
      SketchpadClient spc = new SketchpadClient("클라이언트 그림판");   
   }
   
   //화면 크기
   static final int WIDTH = 800;
   static final int HEIGHT = 600;
   //그림 그리기 변수들
   Color c = Color.black;
   Color background = Color.white;
   Color choosed;
   int upperLeftX, upperLeftY;
   int width, height;
   int x1, y1, x2, y2;
   int drawingCount =0;
   boolean fill = false; boolean erasure = false;
   boolean clear = false;
   String drawColor = new String("black");
   String drawShape = new String("line");
   JTextField color = new JTextField(10);
   JTextField position = new JTextField("(0,0)", 10);
   ButtonGroup fillOutline = new ButtonGroup(); 
   String[] fileNames = {"open", "save"};
   String[] colorNames = { "black", "blue", "cyan", "gray", "green", "magenta", "red", "white", "yellow" };
   String[] shapeNames = { "line" };

   ArrayList<DrawingObject> drObj = new ArrayList<DrawingObject>();
   
   JFileChooser fileChooser = new JFileChooser();
   JColorChooser colorchooser = new JColorChooser();

   JFrame about = new JFrame("About Sketchpad");
   JTabbedPane jtp = new JTabbedPane();
   JPanel north1 = new JPanel();
   JPanel colorpan = new JPanel(); JPanel shapepan = new JPanel();
   JPanel checkpan = new JPanel();
   JPanel north2 = new JPanel();
   JPanel groundpan = new JPanel();
   JPanel sizepan = new JPanel();
   JPanel south = new JPanel();
   CenterJPanel canvas = new CenterJPanel();
   int drcount=0;

   //생성자
   public SketchpadClient(String s) throws Exception {
      super(s);
      this.setVisible(true);
      //화면 크기 설정
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(new Dimension(screenSize.height,screenSize.width/2));
      
      setLayout(new BorderLayout());
      setBackground(Color.yellow);
      this.add(south, BorderLayout.SOUTH);
      this.add(canvas);
      
      jtp.setPreferredSize(new Dimension(100,100));
      jtp.addTab("Home", null, north1);
      north1.setLayout(new GridLayout(1,4,5,5));
      north1.add(colorpan);
      north1.setBackground(Color.white);

      north2.setLayout(new GridLayout(1,2));
      north2.add(groundpan); north2.add(sizepan);
      this.add(jtp, BorderLayout.NORTH);
      
      initializeJTextFields();
      initializeMenuComponents();
      initializeTabbedPaneButtons();
      connected();   //서버에 연결
      
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent event) {
            System.exit(0);
         }
      });

   }
   
   //서버 연결 메소드
   void connected() throws Exception{
      System.out.println("클라이언트입니다. 연결을 시작합니다.");
      Socket sock = new Socket("192.168.45.245",8888);
      ostream = sock.getOutputStream();
      
      pwrite = new PrintWriter(ostream, true);
      istream = sock.getInputStream();
      oos = new ObjectOutputStream(ostream);
      ois = new ObjectInputStream(istream);
      
      //연결 확인 메시지 전송
      pwrite.println("연결 완료....");
      System.out.println("연결완료\n");
      pwrite.flush();
      
      //서버로부터 DrawingObject 수신
      while((obj = ois.readObject())!=null) {
         System.out.println("\nanother one");
         DrawingObject a = (DrawingObject) obj;
         drObj.add(a);
         canvas.repaint();
      }
      
      pwrite.close();
   }
   
   //그림을 그릴 수 있는 중앙 패널 클래스
   class CenterJPanel extends JPanel {
      CenterJPanel() {
         setBackground(Color.white);
         this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event)
            {
               upperLeftX = 0;
               upperLeftY = 0;
               width = 0;
               height = 0;
               x1 = event.getX();
               y1 = event.getY();
               Graphics g = getGraphics();
               g.drawString(".", x1, y1);
               displayMouseCoordinates(event.getX(), event.getY());
            }
            
            public void mouseReleased(MouseEvent event)
            {
               displayMouseCoordinates(event.getX(), event.getY());
               x2 = event.getX();
               y2 = event.getY();
               upperLeftX = Math.min(x1, x2);
               upperLeftY = Math.min(y1,y2);
               width = Math.abs(x1 -x2);
               height = Math.abs(y1 - y2);

               DrawingObject newObj = new DrawingObject();
               newObj.x1 = x1;newObj.x2 = x2;newObj.y1 = y1;newObj.y2 = y2;newObj.shape = drawShape;
               newObj.upperLeftX = upperLeftX;newObj.upperLeftY = upperLeftY;newObj.width = width;
               newObj.height = height;newObj.fill = fill;newObj.color = drawColor;
               
               drObj.add(newObj);
               canvas.repaint();
               
            }
         });
         this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent event)
            {
               Graphics g = getGraphics();
               x2 = event.getX();
               y2 = event.getY();
               displayMouseCoordinates(event.getX(), event.getY());
            }

            public void mouseMoved(MouseEvent event) {
               displayMouseCoordinates(event.getX(), event.getY());
            }
         });
         
      }

      //repaint 함수: Client Drawing Object를 그리고 Server Drawing Object를 업데이트
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         this.setBackground(background);
         Iterator <DrawingObject> iterator =drObj.iterator();
         
         if(clear == true) {
            drObj.removeAll(drObj);
            clear= false;
         }
         
         
         while(iterator.hasNext()) {
            DrawingObject newObj = iterator.next();
            for(int index= 0;index !=colorNames.length;index++) {
               if(newObj.color.equals(colorNames[index])){
                  switch (index) {
                  case 0:
                     c = Color.black;g.setColor(c);
                     break;
                  case 1:
                     c = Color.blue;g.setColor(c);
                     break;
                  case 2:
                     c = Color.cyan;g.setColor(c);
                     break;
                  case 3:
                     c = Color.gray;g.setColor(c);
                     break;
                  case 4:
                     c = Color.green;g.setColor(c);
                     break;
                  case 5:
                     c = Color.magenta;g.setColor(c);
                     break;
                  case 6:
                     c = Color.red;g.setColor(c);
                     break;
                  case 7:
                     c = Color.white;g.setColor(c);
                     break;
                  case 8:
                     c = Color.yellow;g.setColor(c);
                  }
               }
               if (newObj.shape.equals("line"))
                  g.drawLine(newObj.x1, newObj.y1, newObj.x2, newObj.y2);
               else if (newObj.shape.equals("square") && newObj.fill)
                  g.fillRect(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.width);
               else if (newObj.shape.equals("square") && !newObj.fill)
                  g.drawRect(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.width);
               else if (newObj.shape.equals("rectangle") && newObj.fill)
                  g.fillRect(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.height);
               else if (newObj.shape.equals("rectangle") && !newObj.fill)
                  g.drawRect(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.height);
               else if (newObj.shape.equals("circle") && newObj.fill)
                  g.fillOval(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.width);
               else if (newObj.shape.equals("circle") && !newObj.fill)
                  g.drawOval(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.width);
               else if (newObj.shape.equals("ellipse") && newObj.fill)
                  g.fillOval(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.height);
               else if (newObj.shape.equals("ellipse") && !newObj.fill)
                  g.drawOval(newObj.upperLeftX, newObj.upperLeftY, newObj.width, newObj.height);
            }
         }
      }
      
   }

   private void initializeJTextFields() {
      color.setText(drawColor);
      south.add(color);
      south.add(position);
   }
   
   private void initializeMenuComponents() {
      JMenuBar bar = new JMenuBar();
      
      JMenu file = new JMenu("Files");
      JMenuItem fileitem1 = new JMenuItem("Open");
      JMenuItem fileitem2 = new JMenuItem("Save");
      file.add(fileitem1);file.add(fileitem2);
      fileitem1.addActionListener(this);fileitem2.addActionListener(this);
      bar.add(file);
      
      setJMenuBar(bar);
   }

   //JTabbedPane 설정
   private void initializeTabbedPaneButtons() {
      JPanel color1 = new JPanel(); color1.setLayout(new GridLayout(2,4));
      colorpan.setLayout(new BorderLayout());
      JLabel colorlab = new JLabel("Color");
      JButton red = new JButton();   red.setBackground(Color.red);      red.addActionListener(this);
      JButton blue = new JButton();   blue.setBackground(Color.blue);      blue.addActionListener(this);
      JButton yellow = new JButton();   yellow.setBackground(Color.yellow);   yellow.addActionListener(this);
      JButton green = new JButton();   green.setBackground(Color.green);   green.addActionListener(this);
      JButton cyan = new JButton();   cyan.setBackground(Color.cyan);      cyan.addActionListener(this);
      JButton white = new JButton();   white.setBackground(Color.white);   white.addActionListener(this);
      JButton black = new JButton();   black.setBackground(Color.black);   black.addActionListener(this);
      JButton more = new JButton("More");   more.setBackground(Color.white);more.addActionListener(this);
      red.setActionCommand("red");blue.setActionCommand("blue");yellow.setActionCommand("yellow");
      green.setActionCommand("green");cyan.setActionCommand("cyan");white.setActionCommand("white");
      black.setActionCommand("black");
      more.setActionCommand("colorIcon");
      colorpan.add(colorlab, BorderLayout.SOUTH); colorpan.add(color1);
      color1.add(red); color1.add(blue); color1.add(yellow); color1.add(green);color1.add(cyan);
      color1.add(white);color1.add(black); color1.add(more);
      colorlab.setHorizontalAlignment(SwingConstants.CENTER); colorlab.setVisible(true);
   }



   public void actionPerformed(ActionEvent event)
   {
      Object source = event.getActionCommand();
      
      
      
      //DrawingObject 색상 설정
      if(source.equals("colorIcon")) {
         Color selected = colorchooser.showDialog(null, "color", Color.yellow);
         choosed=selected;
         drawColor = selected.toString();
         color.setText(drawColor);
      }
      
      //JFileChooser 설정
      if(source.equals("Open")) {
         fileChooser.setVisible(true);
         int result = fileChooser.showOpenDialog(this);
         if(result == JFileChooser.CANCEL_OPTION)
            fileChooser.setVisible(false);
         
         File fileName = fileChooser.getSelectedFile();
         
         if(fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(this,"Invalid Name", "Invalid Name", JOptionPane.ERROR_MESSAGE);
         }
         loadFile(fileName);
      }else if(source.equals("Save")){
         fileChooser.setVisible(true);
         int result = fileChooser.showOpenDialog(this);
         if(result == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
         }
         File fileName = fileChooser.getSelectedFile();
         if((fileName == null) || (fileName.getName().contentEquals("")))
            JOptionPane.showMessageDialog(this,"Invalid Name", "Invalid Name", JOptionPane.ERROR_MESSAGE);
         saveFile(drObj, fileName);
      }
      
      
      //색상 선택
      for (int index = 0; index != colorNames.length; index++)
         if (source.equals(colorNames[index])) {
            drawColor = colorNames[index];
            color.setText(drawColor);
            return;
         }
   }


   //마우스 좌표 표시
   protected void displayMouseCoordinates(int X, int Y)
   {
      position.setText("[" + String.valueOf(X) + "," + String.valueOf(Y) + "]");
   }
   
   //JFileChooser 설정
   public void loadFile(File filename) {
      try {
         FileInputStream fis = new FileInputStream(filename);
         ObjectInputStream in = new ObjectInputStream(fis);
         drObj = (ArrayList<DrawingObject>)in.readObject();
         repaint();in.close();
      }catch(Exception e) {
         System.out.println(e);
      }
   }
   //파일 저장 메소드
   public void saveFile(ArrayList <DrawingObject> arr, File filename) {
      try {
         FileOutputStream fos = new FileOutputStream(filename);
         ObjectOutputStream out = new ObjectOutputStream(fos);
         out.writeObject(arr);
         out.flush();out.close();
      }catch(IOException e) {
         System.out.println(e);
      }
   }

   @Override
   public void itemStateChanged(ItemEvent e) {
      
   }
   
}
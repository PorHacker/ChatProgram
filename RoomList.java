package Chatlist1;

import javax.swing.*;

import me.MultiChatClient;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomList extends JFrame {
    public RoomList() {
        setTitle("List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 전체 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 채팅방 목록 패널 생성
        JPanel chatListPanel = new JPanel();
        chatListPanel.setLayout(new BoxLayout(chatListPanel, BoxLayout.Y_AXIS));

        // 친구 목록 패널 생성
        JPanel friendListPanel = new JPanel();
        friendListPanel.setLayout(new BoxLayout(friendListPanel, BoxLayout.Y_AXIS));

        // 채팅방 목록과 친구 목록을 추가
        addChatRoom(chatListPanel, "방만들기");
        addFriend(friendListPanel, "친구1", "친구1과의 채팅방");
        addFriend(friendListPanel, "친구2", "친구2과의 채팅방");
        addFriend(friendListPanel, "친구3", "친구3과의 채팅방");
        addFriend(friendListPanel, "친구4", "친구4과의 채팅방");
        addFriend(friendListPanel, "친구5", "친구5과의 채팅방");
        
        // 패널을 mainPanel에 추가
        mainPanel.add(Box.createVerticalStrut(20)); // 간격 조절
        mainPanel.add(new JLabel("새 채팅방 만들기"));
        mainPanel.add(Box.createVerticalStrut(10)); // 간격 조절
        mainPanel.add(chatListPanel);
        mainPanel.add(Box.createVerticalStrut(20)); // 간격 조절
        mainPanel.add(new JLabel("친구 목록"));
        mainPanel.add(Box.createVerticalStrut(10)); // 간격 조절
        mainPanel.add(friendListPanel);

        // 스크롤 가능한 영역 생성
        JScrollPane scrollPane = new JScrollPane(mainPanel);

        // 전체 프레임에 스크롤 가능한 영역 추가
        getContentPane().add(scrollPane);

        setSize(300, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 채팅방을 목록에 추가하는 메서드
    private void addChatRoom(JPanel chatListPanel, String roomName) {
        JButton chatRoomButton = new JButton(roomName);
        chatRoomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 채팅방을 클릭할 때 CreateRoom 열기
                if("방만들기".equals(roomName)) {
                	openCreateRoom();
                }
            }
            
            // CreateRoom 열기
            private void openCreateRoom() {
            	SwingUtilities.invokeLater(new Runnable() {
            		@Override
            		public void run() {
            			//CreateRoom를 생성하고 보여주기
            			CreateRoom createRoom = new CreateRoom();
            			createRoom.setVisible(true);
            		}
            	});
            }
        });

     // 채팅방 목록에 버튼 추가
        chatListPanel.add(chatRoomButton);

        // 간격을 조절하기 위해 빈 공간 추가
        chatListPanel.add(Box.createVerticalStrut(10));
    }
    
    // 친구를 목록에 추가하는 메서드
    private void addFriend(JPanel friendListPanel, String friendName, String roomName) {
        JButton friendButton = new JButton(friendName);
        friendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        friendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 친구를 클릭할 때 해당 친구와의 채팅창 대신 MultiChatClient 열기
                openMultiChatClient("192.168.0.4", roomName);
            }
        });

        // 친구 목록에 버튼 추가
        friendListPanel.add(friendButton);

        // 간격을 조절하기 위해 빈 공간 추가
        friendListPanel.add(Box.createVerticalStrut(10));
    }
    
    // MultiChatClient를 열어주는 메서드
    private void openMultiChatClient(String ip, String roomName) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	MultiChatClient multiChatClient = new MultiChatClient("192.168.0.4", roomName);
                multiChatClient.connectServer(); // 생성 후 서버에 연결
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RoomList();
            }
        });
    }
}
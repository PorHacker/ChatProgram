package Chatlist1;

import javax.swing.*;

import me.MultiChatClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRoom extends JFrame {
    private JPanel buttonPanel;

    public CreateRoom() {
        setTitle("새 채팅방 만들기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // 버튼을 여러개 추가
        addButton("친구 1");
        buttonPanel.add(Box.createVerticalStrut(10)); // 간격 조절
        addButton("친구 2");
        buttonPanel.add(Box.createVerticalStrut(10)); // 간격 조절
        addButton("친구 3");
        buttonPanel.add(Box.createVerticalStrut(10)); // 간격 조절
        addButton("친구 4");
        buttonPanel.add(Box.createVerticalStrut(10)); // 간격 조절
        addButton("친구 5");
        
        // 만들기 버튼 추가
        JButton confirmButton = new JButton("만들기");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 선택된 버튼을 확인하고 처리
                handleSelectedButtons();
            }
        });
        
        // 취소 버튼 추가
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 취소 버튼 클릭 시 닫기
                dispose();
            }
        });

        // 패널을 담은 프레임과 확인, 취소 버튼을 추가
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalGlue()); // 가운데 정렬을 위한 여백 추가
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue()); // 가운데 정렬을 위한 여백 추가
        
        JPanel buttonContainerPanel = new JPanel(new GridBagLayout());
        buttonContainerPanel.add(buttonPanel, new GridBagConstraints() {{
            insets.right = 15; // 오른쪽 여백 조절
        }});
        mainPanel.add(buttonContainerPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel);
        

        // 전체 프레임에 패널 추가
        getContentPane().add(mainPanel);

        setSize(300, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addButton(String label) {
        JToggleButton button = new JToggleButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 버튼의 선택 여부에 따라 처리
                if (button.isSelected()) {
                    // 선택되었을 때의 동작
                    System.out.println(label + " is selected.");
                } else {
                    // 선택이 해제되었을 때의 동작
                    System.out.println(label + " is deselected.");
                }
            }
        });

        buttonPanel.add(button);
    }

    private void handleSelectedButtons() {
    	// 선택된 버튼을 확인하고 처리하는 로직을 작성
        Component[] components = buttonPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JToggleButton) {
                JToggleButton button = (JToggleButton) component;
                if (button.isSelected()) {
                    // 선택된 버튼 처리
                    String roomName = button.getText();
                    System.out.println(roomName);

                    // 두 개의 매개변수를 사용하여 MultiChatClient 인스턴스화
                    SwingUtilities.invokeLater(() -> new MultiChatClient("172.30.1.8", roomName));
                    
                    // 필요한 경우 다른 로직 추가
                    // ...
                }
            }
        }

        // 채팅방 생성 또는 다음 클래스로 넘어가는 로직 추가
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> new CreateRoom());
    }
}
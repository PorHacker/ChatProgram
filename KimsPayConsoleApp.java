package Chatlist1;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import me.MultiChatClient;

import javax.swing.*;
import java.awt.*;

public class KimsPayConsoleApp extends JFrame {
    public KimsPayConsoleApp() {
 
        setTitle("Kims Pay 창");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 간격 추가
        panel.add(Box.createVerticalStrut(60));

        // Kims Pay로 정산하기 버튼
        JButton settleButton = new JButton("Kims Pay로 정산하기");
        settleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 적절한 로직 추가
                System.out.println(" - Kims Pay로 정산하기를 선택하셨습니다.");
            }
        });
        panel.add(settleButton);

        // 간격 추가
        panel.add(Box.createVerticalStrut(20));

        // 계좌번호로 보내기 버튼
        JButton sendToAccountButton = new JButton("계좌번호로 보내기");
        sendToAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 적절한 로직 추가
                System.out.println(" - 계좌번호로 보내기를 선택하셨습니다.");
            }
        });
        panel.add(sendToAccountButton);

        // 간격 추가
        panel.add(Box.createVerticalStrut(60));
        
     // 나가기 버튼
        JButton backButton = new JButton("Kims Pay 나가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 현재 창을 닫는 로직
                dispose();
            }
        });
        panel.add(backButton);

        // 간격 추가
        panel.add(Box.createVerticalStrut(10));

        // 버튼들을 중앙에 정렬
        for (Component c : panel.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }

        add(panel);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 테스트용으로 생성자에 빈 문자열 전달
                new KimsPayConsoleApp();
            }
        });
    }
}
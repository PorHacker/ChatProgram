package me;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JoinFrame extends JFrame {
    /* Panel */
    JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5)); // 8행 2열의 GridLayout으로 변경, 간격 추가

    /* Label */
    JLabel idL = new JLabel("ID");
    JTextField idLField = new JTextField();
    JLabel pwL = new JLabel("Password");
    JTextField pwLField = new JTextField();
    JLabel nameL = new JLabel("Name");
    JTextField nameLField = new JTextField();
    JLabel genderL = new JLabel("Gender");
    JTextField genderLField = new JTextField();
    JLabel birthL = new JLabel("Birth");
    JTextField birthLField = new JTextField();
    JLabel emailL = new JLabel("Email");
    JTextField emailLField = new JTextField();
    JLabel accountL = new JLabel("Account");
    JTextField accountLField = new JTextField();

    /* TextField */
    JTextField id = new JTextField();
    JPasswordField pw = new JPasswordField();
    JTextField name = new JTextField();
    JTextField gender = new JTextField();
    JTextField birth = new JTextField();
    JTextField email = new JTextField();
    JTextField account = new JTextField();

    /* Button */
    JButton joinBtn = new JButton("가입하기");
    JButton cancelBtn = new JButton("가입취소");

    Operator o = null;

    JoinFrame(Operator _o) {
        o = _o;

        setTitle("회원가입");

        /* Button 크기 작업 */
        joinBtn.setPreferredSize(new Dimension(95, 25));
        cancelBtn.setPreferredSize(new Dimension(95, 25));

        /* Panel 추가 작업 */
        setContentPane(panel);

        panel.add(idL);
        panel.add(id);

        panel.add(pwL);
        panel.add(pw);

        panel.add(nameL);
        panel.add(name);

        panel.add(genderL);
        panel.add(gender);

        panel.add(birthL);
        panel.add(birth);

        panel.add(emailL);
        panel.add(email);
        
        panel.add(accountL);
        panel.add(account);

        panel.add(cancelBtn);
        panel.add(joinBtn);

        /* Button 이벤트 리스너 추가 */
        ButtonListener bl = new ButtonListener();

        cancelBtn.addActionListener(bl);
        joinBtn.addActionListener(bl);

        setSize(300, 250); // 크기 조정
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /* Button 이벤트 리스너 */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();

            /* TextField에 입력된 회원 정보들을 변수에 초기화 */
            String uid = id.getText();
            String upass = "";
            for (int i = 0; i < pw.getPassword().length; i++) {
                upass = upass + pw.getPassword()[i];
            }

            /* 가입취소 버튼 이벤트 */
            if (b.getText().equals("가입취소")) {
                dispose();
            }

            /* 가입하기 버튼 이벤트 */
            else if (b.getText().equals("가입하기")) {
                if (uid.equals("") || upass.equals("") || name.getText().equals("") || gender.getText().equals("") || birth.getText().equals("") || email.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "모든 정보를 기입해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("회원가입 실패 > 회원정보 미입력");
                } else {
                    if (o.db.joinCheck(uid, upass, name.getText(), gender.getText(), Integer.parseInt(birth.getText()), email.getText(), Integer.parseInt(account.getText()))) {
                        System.out.println("회원가입 성공");
                        JOptionPane.showMessageDialog(null, "회원가입에 성공하였습니다");
                        dispose();
                    } else {
                        System.out.println("회원가입 실패");
                        JOptionPane.showMessageDialog(null, "회원가입에 실패하였습니다");
                        id.setText("");
                        pw.setText("");
                    }
                }
            }
        }
    }
}

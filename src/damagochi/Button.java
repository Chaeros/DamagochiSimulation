package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Button extends JFrame {

	private JPanel contentPane;
	private JTextField textField;//공
	private JTextField textField_1;//방
	private JTextField textField_2;//체
	private JTextField textField_3;//애정
	private JTextField textField_4;//공복
	private JTextField textField_6;//민첩성
	private JLabel lblNewLabel_1;
	ImageIcon imageIcon;
	private JLabel lblNewLabel = new JLabel();//이미지
	private JPanel panel_1;
	String name;
	double count1 = 1;//공격력
	double count2 = 0;//방어력
	double count3 = 10;//체력
	double count4 = 10,count5 = 10;//애정도,공복도
	double speed = 0;//스피드
	int level = 1;//현재레벨
	double levelp = 0;//현재 경허치량
	double maxlvp = 100;//최대 경험치량 레벨업시 1.3배증가
	private JTextField textField_5;//변경사항표시
	private int characterNum;
	String id;//id
	
	LoginFrame lf = new LoginFrame();
	String IDtemp = lf.IDtemp;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {//자체 테스트 실행용
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Button frame = new Button();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	Connection con = null;  // DBMS 로그인 정보 입력
	Statement stmt = null;  // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
	ResultSet result = null;//결과
	String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost 아이피에서 chattingservice 스키마에 접근, 대한민국 시간대(물음표필수)
	String user = "root";   //DB ID
	String passwd = "6272"; //DB PW
	
	private void character() {//db에서 캐릭터 정보를 가져온다
		try {
			Class.forName("com.mysql.jdbc.Driver");  // jdbc드라이버 가져옴
		} catch(java.lang.ClassNotFoundException e1) {
			System.err.print("ClassNotFoundException: "); 
			System.err.println("드라이버 로딩 오류: " + e1.getMessage());
			return;
		} 
		
		try {
			con = DriverManager.getConnection(url, user, passwd);  //Connection 객체 getter메소드로 호출 //URL,(DB)ID,PW 입력  ->db에 접근
			stmt = con.createStatement(); // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
			result = stmt.executeQuery("SELECT *"
					+ " FROM characters WHERE id='"+IDtemp+"'");
			
			while(result.next()) {
				count1 = result.getDouble("power");
				count2 = result.getDouble("defense");
				count3 = result.getDouble("stamina");
				count4 = result.getDouble("love");
				count5 = result.getDouble("starvation");
				level = result.getInt("level");
				levelp = result.getDouble("experience");
				maxlvp = result.getDouble("sumExperience");
				speed = result.getDouble("speed");
				name = result.getString("name");
				characterNum = result.getInt("characterNum");
				
			}
			
			System.out.println("SELECT *"
					+ " FROM characters WHERE id='"+IDtemp+"'");
			imageIcon = new ImageIcon("D:\\DamaghchiSimulationImages/"+characterNum+"/normal1.png");
			lblNewLabel = new JLabel(imageIcon);
			
			
			stmt.close();
			con.close();
			
		} catch(SQLException ex) {
			System.err.println("Select 오류: " + ex.getMessage());
		}
	}
	
	private void levelup() {//레벨업
		if(levelp >= maxlvp) {
			levelp = 0;
			level++;
			maxlvp *= 1.3;
			speed++;
			textField_6.setText(""+ speed);
			lblNewLabel_1.setText("레벨 :"+ level);
			textField_5.setText("레벨업!!");
			panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
			//이미지
			if(level == 10) {
				if(count1<count2) {
					JOptionPane.showMessageDialog(null, "방어형으로 진화!", "진화!!", JOptionPane.INFORMATION_MESSAGE);
					characterNum = 2;
					imageIcon = new ImageIcon("D:\\DamaghchiSimulationImages/"+characterNum+"/normal1.png");
					lblNewLabel.setIcon(imageIcon);
				}else if(count2<count1) {
					JOptionPane.showMessageDialog(null, "공격형으로 진화!", "진화!!", JOptionPane.INFORMATION_MESSAGE);
					characterNum = 3;
					imageIcon = new ImageIcon("D:\\DamaghchiSimulationImages/"+characterNum+"/normal1.png");
					lblNewLabel.setIcon(imageIcon);
				}else if(count1==count2) {
					JOptionPane.showMessageDialog(null, "히든!!! 진화!", "진화!!", JOptionPane.INFORMATION_MESSAGE);
					characterNum = 4;
					imageIcon = new ImageIcon("D:\\DamaghchiSimulationImages/"+characterNum+"/normal1.png");
					lblNewLabel.setIcon(imageIcon);
				}
			}
		}
	}
	
	
	Button(String _id) {
		this.id=_id;
		lf.setVisible(false);
		character();
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 795, 629);
		setVisible(true);
		Dimension frameSize = this.getSize(); // 프레임 사이즈
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		//setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.textHighlight);
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("메뉴");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("저장");
		mnNewMenu.add(mntmNewMenuItem_1);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					Class.forName("com.mysql.jdbc.Driver");  // jdbc드라이버 가져옴
				} catch(java.lang.ClassNotFoundException e1) {
					System.err.print("ClassNotFoundException: "); 
					System.err.println("드라이버 로딩 오류: " + e1.getMessage());
					return;
				} 
            	
            	try {
            		con = DriverManager.getConnection(url, user, passwd);  //Connection 객체 getter메소드로 호출 //URL,(DB)ID,PW 입력  ->db에 접근
					//stmt = con.createStatement(); // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
					//result = stmt.executeQuery("SELECT * FROM characters");
					
					
					String sql = "UPDATE characters SET power = '"+count1+"',defense = '"+count2+"',level = '"+level+"',love = '"+count4+"'"
							+ ",starvation = '"+count5+"',stamina = '"+count3+"',experience = '"+levelp+"'"
							+ ",sumExperience = '"+maxlvp+"',speed = '"+speed+"',characterNum = '"+characterNum+"' WHERE id = '"+IDtemp+"'";
					
					System.out.println(sql);
					
					
					
					//새로운 능력치 정보 업데이트
					PreparedStatement pre = con.prepareStatement(sql);
					pre.executeUpdate();//db로 전송
					
					System.out.println("Characters Information updating Successfully!");
					JOptionPane.showMessageDialog(null, "무사히 캐릭터정보가 저장되었어.", "Message", JOptionPane.INFORMATION_MESSAGE);
					pre.close();
					stmt.close();
					con.close();
					
            		
            	} catch(SQLException ex) {
            		System.err.println("Select 오류: " + ex.getMessage());
            	}
            	
            	
         		}
            });
		
		JMenuItem mntmNewMenuItem = new JMenuItem("로그아웃");
		mnNewMenu.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int result = JOptionPane.showConfirmDialog(null,
						"로그인 화면으로 돌아갑니다.", "경고",
						JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					//System.exit(0);
					setVisible(false);
				LoginFrame LF = new LoginFrame();
				}
				else {
					
				}
         		}
            });
		
		
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.info);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();//경험치 바 베이스 패널
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panel.setBounds(121, 426, 278, 36);
		contentPane.add(panel);
		panel.setLayout(null);
		
		panel_1 = new JPanel();//경험치 바
		panel_1.setBackground(Color.GREEN);
		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
		panel.add(panel_1);
		
		lblNewLabel_1 = new JLabel("레벨 :"+ level);
		lblNewLabel_1.setFont(new Font("나눔고딕", Font.BOLD, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(35, 426, 72, 29);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(57, 82, 319, 332);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("훈련");
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 20));
		btnNewButton.setBounds(571, 82, 158, 51);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	 String tr = JOptionPane.showInputDialog(null, "무슨훈련을 받을거냐?", "훈련",JOptionPane.OK_CANCEL_OPTION);
            	 switch(tr) {
            	 case "a":
            	 case "ap":
            	 case "공격력":
            		 
             		if(count4 < 1) {
             			JOptionPane.showMessageDialog(null, "애정도가 부족해..", "큰일이다!!!", JOptionPane.ERROR_MESSAGE);
             		}else {
             			if (count5 < 1) {
                 			JOptionPane.showMessageDialog(null, "공복도가 부족해..", "큰일이다!!!", JOptionPane.ERROR_MESSAGE);
                 		}else {
                 			count5--;
                     		textField_4.setText(count5 +"");
                     		count4--;
                     		textField_3.setText(count4 +"");
                     		count1++;
                     		textField.setText(count1 +"");
                     		textField_5.setText("공격력이 +1 증가했어!!");
                     		levelp+=10;
                     		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
                     		levelup();
                 		}
             			
             		}
             		break;
            	 case "d":
            	 case "dp":
            	 case "방어력":
            		 
            		 if(count4 < 1) {
              			JOptionPane.showMessageDialog(null, "애정도가 부족해..", "큰일이다!!!", JOptionPane.ERROR_MESSAGE);
              		}else {
              			if (count5 < 1) {
                  			JOptionPane.showMessageDialog(null, "공복도가 부족해..", "큰일이다!!!", JOptionPane.ERROR_MESSAGE);
                  		}else {
                  			count5--;
                      		textField_4.setText(count5 +"");
                      		count4--;
                      		textField_3.setText(count4 +"");
                      		count2++;
                     		textField_1.setText(count2 +"");
                     		textField_5.setText("방어력이 +1 증가했어!!");
                     		levelp+=10;
                     		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
                     		levelup();
                  		}
              			
              		}
             		break;
            	 case "h":
            	 case "hp":
            	 case "체력":
            		 
            		 if(count4 < 1) {
               			JOptionPane.showMessageDialog(null, "애정도가 부족해..", "큰일이다!!!", JOptionPane.ERROR_MESSAGE);
               		}else {
               			if (count5 < 1) {
                   			JOptionPane.showMessageDialog(null, "공복도가 부족해..", "큰일이다!!!", JOptionPane.ERROR_MESSAGE);
                   		}else {
                   			count5--;
                       		textField_4.setText(count5 +"");
                       		count4--;
                       		textField_3.setText(count4 +"");
                       		count3+=5;
                     		textField_2.setText(count3 +"");
                     		textField_5.setText("체력력이 +5 증가했어!!");
                     		levelp+=10;
                     		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
                     		levelup();
                   		}
               			
               		}
             		break;
             		default :
             			JOptionPane.showMessageDialog(null, "'공격력(a,ap)','방어력(d,dp)','체력(h,hp)' 중에 입력하도록해", "다시입력해줘", JOptionPane.ERROR_MESSAGE);
             		break;
            	 }
            }
        });
		
		JButton button = new JButton("밥주기");
		button.setFont(new Font("굴림", Font.BOLD, 20));
		button.setBounds(571, 165, 158, 51);
		contentPane.add(button);
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	 count5+=2;
            	 textField_4.setText(count5 +"");
            	 textField_5.setText("공복도가 +2 증가했어!!!");
            	 levelp+=1;
            	 panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
            	 levelup();
            }
        });
		
		JButton button_1 = new JButton("산책");
		button_1.setFont(new Font("굴림", Font.BOLD, 20));
		button_1.setBounds(571, 246, 158, 51);
		contentPane.add(button_1);
		button_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (count5 < 1) {
           			JOptionPane.showMessageDialog(null, "공복도가 부족해..", "큰일이다!!!", JOptionPane.ERROR_MESSAGE);
           		}else {
           		 count4+=2;
               	 textField_3.setText(count4 +"");
               	 count5--;
               	 textField_4.setText(count5 +"");
               	 textField_5.setText("애정도가 +2 증가했어!!!");
               	 levelp+=5;
               	panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
               	levelup();
           		}
            	 
            }
        });
		
		JLabel label = new JLabel("공격력");
		label.setForeground(Color.RED);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(35, 477, 62, 18);
		contentPane.add(label);
		
		textField = new JTextField(""+ count1);
		textField.setForeground(Color.RED);
		textField.setBounds(111, 474, 80, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("방어력");
		label_1.setForeground(Color.BLUE);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(35, 500, 62, 18);
		contentPane.add(label_1);
		
		textField_1 = new JTextField(""+ count2);
		textField_1.setForeground(Color.BLUE);
		textField_1.setColumns(10);
		textField_1.setBounds(111, 497, 80, 21);
		contentPane.add(textField_1);
		
		JLabel label_2 = new JLabel("체력");
		label_2.setForeground(Color.GREEN);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(35, 526, 62, 18);
		contentPane.add(label_2);
		
		textField_2 = new JTextField(""+ count3);
		textField_2.setForeground(Color.GREEN);
		textField_2.setColumns(10);
		textField_2.setBounds(111, 523, 80, 21);
		contentPane.add(textField_2);
		
		JLabel label_3 = new JLabel("애정도");
		label_3.setForeground(Color.PINK);
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(214, 477, 62, 18);
		contentPane.add(label_3);
		
		textField_3 = new JTextField(""+ count4);
		textField_3.setForeground(Color.PINK);
		textField_3.setColumns(10);
		textField_3.setBounds(290, 474, 80, 21);
		contentPane.add(textField_3);
		
		JLabel label_4 = new JLabel("공복도");
		label_4.setForeground(Color.BLACK);
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(214, 500, 62, 18);
		contentPane.add(label_4);
		
		textField_4 = new JTextField(""+ count5);
		textField_4.setColumns(10);
		textField_4.setBounds(290, 497, 80, 21);
		contentPane.add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setForeground(SystemColor.desktop);
		textField_5.setFont(new Font("굴림", Font.BOLD, 16));
		textField_5.setBackground(SystemColor.textHighlightText);
		textField_5.setEnabled(false);
		textField_5.setEditable(false);
		textField_5.setBounds(58, 51, 318, 18);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel(name);//이름
		lblNewLabel_2.setBackground(Color.WHITE);
		lblNewLabel_2.setForeground(Color.BLACK);
		lblNewLabel_2.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(146, 12, 130, 27);
		contentPane.add(lblNewLabel_2);
		
		JLabel label_5 = new JLabel("민첩성");
		label_5.setForeground(Color.CYAN);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(214, 526, 62, 18);
		contentPane.add(label_5);
		
		textField_6 = new JTextField(""+ speed);//민천성
		textField_6.setForeground(Color.CYAN);
		textField_6.setColumns(10);
		textField_6.setBounds(290, 523, 80, 21);
		contentPane.add(textField_6);
		
		JButton btnNewButton_1 = new JButton("경험치 실험");
		btnNewButton_1.setBounds(460, 426, 105, 27);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("PVP");
		btnNewButton_2.setFont(new Font("굴림", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_2.setBounds(571, 320, 158, 51);
		contentPane.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Client client = new Client(id);
            	setVisible(false);
            	client.setVisible(true);
            	client.cancel.addActionListener(client.new MyActionListener());
            	client.cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	client.setVisible(false);
                    	setVisible(true);
                    }
                });
            }
        });
		
		btnNewButton_1.addActionListener(new ActionListener() {//실험버튼 나중에 삭제하면됨.
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	levelp+=50;
            	panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
            	System.out.println("levelp/maxlvp*272(panel_1) :" + ((double)levelp/(double)maxlvp)*272);
            	levelup();
            }
        });
		
		
		
		
		
		
		
	}
}
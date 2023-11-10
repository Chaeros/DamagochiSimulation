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
	private JTextField textField;//��
	private JTextField textField_1;//��
	private JTextField textField_2;//ü
	private JTextField textField_3;//����
	private JTextField textField_4;//����
	private JTextField textField_6;//��ø��
	private JLabel lblNewLabel_1;
	ImageIcon imageIcon;
	private JLabel lblNewLabel = new JLabel();//�̹���
	private JPanel panel_1;
	String name;
	double count1 = 1;//���ݷ�
	double count2 = 0;//����
	double count3 = 10;//ü��
	double count4 = 10,count5 = 10;//������,������
	double speed = 0;//���ǵ�
	int level = 1;//���緹��
	double levelp = 0;//���� ����ġ��
	double maxlvp = 100;//�ִ� ����ġ�� �������� 1.3������
	private JTextField textField_5;//�������ǥ��
	private int characterNum;
	String id;//id
	
	LoginFrame lf = new LoginFrame();
	String IDtemp = lf.IDtemp;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {//��ü �׽�Ʈ �����
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
	Connection con = null;  // DBMS �α��� ���� �Է�
	Statement stmt = null;  // ���� DBMS�� ����� ��ų ������ ������ ���� ��ü(�˻�, ����, ���� ��)
	ResultSet result = null;//���
	String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost �����ǿ��� chattingservice ��Ű���� ����, ���ѹα� �ð���(����ǥ�ʼ�)
	String user = "root";   //DB ID
	String passwd = "6272"; //DB PW
	
	private void character() {//db���� ĳ���� ������ �����´�
		try {
			Class.forName("com.mysql.jdbc.Driver");  // jdbc����̹� ������
		} catch(java.lang.ClassNotFoundException e1) {
			System.err.print("ClassNotFoundException: "); 
			System.err.println("����̹� �ε� ����: " + e1.getMessage());
			return;
		} 
		
		try {
			con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
			stmt = con.createStatement(); // ���� DBMS�� ����� ��ų ������ ������ ���� ��ü(�˻�, ����, ���� ��)
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
			System.err.println("Select ����: " + ex.getMessage());
		}
	}
	
	private void levelup() {//������
		if(levelp >= maxlvp) {
			levelp = 0;
			level++;
			maxlvp *= 1.3;
			speed++;
			textField_6.setText(""+ speed);
			lblNewLabel_1.setText("���� :"+ level);
			textField_5.setText("������!!");
			panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
			//�̹���
			if(level == 10) {
				if(count1<count2) {
					JOptionPane.showMessageDialog(null, "��������� ��ȭ!", "��ȭ!!", JOptionPane.INFORMATION_MESSAGE);
					characterNum = 2;
					imageIcon = new ImageIcon("D:\\DamaghchiSimulationImages/"+characterNum+"/normal1.png");
					lblNewLabel.setIcon(imageIcon);
				}else if(count2<count1) {
					JOptionPane.showMessageDialog(null, "���������� ��ȭ!", "��ȭ!!", JOptionPane.INFORMATION_MESSAGE);
					characterNum = 3;
					imageIcon = new ImageIcon("D:\\DamaghchiSimulationImages/"+characterNum+"/normal1.png");
					lblNewLabel.setIcon(imageIcon);
				}else if(count1==count2) {
					JOptionPane.showMessageDialog(null, "����!!! ��ȭ!", "��ȭ!!", JOptionPane.INFORMATION_MESSAGE);
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
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		//setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.textHighlight);
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("�޴�");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("����");
		mnNewMenu.add(mntmNewMenuItem_1);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					Class.forName("com.mysql.jdbc.Driver");  // jdbc����̹� ������
				} catch(java.lang.ClassNotFoundException e1) {
					System.err.print("ClassNotFoundException: "); 
					System.err.println("����̹� �ε� ����: " + e1.getMessage());
					return;
				} 
            	
            	try {
            		con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
					//stmt = con.createStatement(); // ���� DBMS�� ����� ��ų ������ ������ ���� ��ü(�˻�, ����, ���� ��)
					//result = stmt.executeQuery("SELECT * FROM characters");
					
					
					String sql = "UPDATE characters SET power = '"+count1+"',defense = '"+count2+"',level = '"+level+"',love = '"+count4+"'"
							+ ",starvation = '"+count5+"',stamina = '"+count3+"',experience = '"+levelp+"'"
							+ ",sumExperience = '"+maxlvp+"',speed = '"+speed+"',characterNum = '"+characterNum+"' WHERE id = '"+IDtemp+"'";
					
					System.out.println(sql);
					
					
					
					//���ο� �ɷ�ġ ���� ������Ʈ
					PreparedStatement pre = con.prepareStatement(sql);
					pre.executeUpdate();//db�� ����
					
					System.out.println("Characters Information updating Successfully!");
					JOptionPane.showMessageDialog(null, "������ ĳ���������� ����Ǿ���.", "Message", JOptionPane.INFORMATION_MESSAGE);
					pre.close();
					stmt.close();
					con.close();
					
            		
            	} catch(SQLException ex) {
            		System.err.println("Select ����: " + ex.getMessage());
            	}
            	
            	
         		}
            });
		
		JMenuItem mntmNewMenuItem = new JMenuItem("�α׾ƿ�");
		mnNewMenu.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int result = JOptionPane.showConfirmDialog(null,
						"�α��� ȭ������ ���ư��ϴ�.", "���",
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
		
		JPanel panel = new JPanel();//����ġ �� ���̽� �г�
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panel.setBounds(121, 426, 278, 36);
		contentPane.add(panel);
		panel.setLayout(null);
		
		panel_1 = new JPanel();//����ġ ��
		panel_1.setBackground(Color.GREEN);
		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
		panel.add(panel_1);
		
		lblNewLabel_1 = new JLabel("���� :"+ level);
		lblNewLabel_1.setFont(new Font("�������", Font.BOLD, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(35, 426, 72, 29);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(57, 82, 319, 332);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("�Ʒ�");
		btnNewButton.setFont(new Font("����", Font.BOLD, 20));
		btnNewButton.setBounds(571, 82, 158, 51);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	 String tr = JOptionPane.showInputDialog(null, "�����Ʒ��� �����ų�?", "�Ʒ�",JOptionPane.OK_CANCEL_OPTION);
            	 switch(tr) {
            	 case "a":
            	 case "ap":
            	 case "���ݷ�":
            		 
             		if(count4 < 1) {
             			JOptionPane.showMessageDialog(null, "�������� ������..", "ū���̴�!!!", JOptionPane.ERROR_MESSAGE);
             		}else {
             			if (count5 < 1) {
                 			JOptionPane.showMessageDialog(null, "�������� ������..", "ū���̴�!!!", JOptionPane.ERROR_MESSAGE);
                 		}else {
                 			count5--;
                     		textField_4.setText(count5 +"");
                     		count4--;
                     		textField_3.setText(count4 +"");
                     		count1++;
                     		textField.setText(count1 +"");
                     		textField_5.setText("���ݷ��� +1 �����߾�!!");
                     		levelp+=10;
                     		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
                     		levelup();
                 		}
             			
             		}
             		break;
            	 case "d":
            	 case "dp":
            	 case "����":
            		 
            		 if(count4 < 1) {
              			JOptionPane.showMessageDialog(null, "�������� ������..", "ū���̴�!!!", JOptionPane.ERROR_MESSAGE);
              		}else {
              			if (count5 < 1) {
                  			JOptionPane.showMessageDialog(null, "�������� ������..", "ū���̴�!!!", JOptionPane.ERROR_MESSAGE);
                  		}else {
                  			count5--;
                      		textField_4.setText(count5 +"");
                      		count4--;
                      		textField_3.setText(count4 +"");
                      		count2++;
                     		textField_1.setText(count2 +"");
                     		textField_5.setText("������ +1 �����߾�!!");
                     		levelp+=10;
                     		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
                     		levelup();
                  		}
              			
              		}
             		break;
            	 case "h":
            	 case "hp":
            	 case "ü��":
            		 
            		 if(count4 < 1) {
               			JOptionPane.showMessageDialog(null, "�������� ������..", "ū���̴�!!!", JOptionPane.ERROR_MESSAGE);
               		}else {
               			if (count5 < 1) {
                   			JOptionPane.showMessageDialog(null, "�������� ������..", "ū���̴�!!!", JOptionPane.ERROR_MESSAGE);
                   		}else {
                   			count5--;
                       		textField_4.setText(count5 +"");
                       		count4--;
                       		textField_3.setText(count4 +"");
                       		count3+=5;
                     		textField_2.setText(count3 +"");
                     		textField_5.setText("ü�·��� +5 �����߾�!!");
                     		levelp+=10;
                     		panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
                     		levelup();
                   		}
               			
               		}
             		break;
             		default :
             			JOptionPane.showMessageDialog(null, "'���ݷ�(a,ap)','����(d,dp)','ü��(h,hp)' �߿� �Է��ϵ�����", "�ٽ��Է�����", JOptionPane.ERROR_MESSAGE);
             		break;
            	 }
            }
        });
		
		JButton button = new JButton("���ֱ�");
		button.setFont(new Font("����", Font.BOLD, 20));
		button.setBounds(571, 165, 158, 51);
		contentPane.add(button);
		button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	 count5+=2;
            	 textField_4.setText(count5 +"");
            	 textField_5.setText("�������� +2 �����߾�!!!");
            	 levelp+=1;
            	 panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
            	 levelup();
            }
        });
		
		JButton button_1 = new JButton("��å");
		button_1.setFont(new Font("����", Font.BOLD, 20));
		button_1.setBounds(571, 246, 158, 51);
		contentPane.add(button_1);
		button_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (count5 < 1) {
           			JOptionPane.showMessageDialog(null, "�������� ������..", "ū���̴�!!!", JOptionPane.ERROR_MESSAGE);
           		}else {
           		 count4+=2;
               	 textField_3.setText(count4 +"");
               	 count5--;
               	 textField_4.setText(count5 +"");
               	 textField_5.setText("�������� +2 �����߾�!!!");
               	 levelp+=5;
               	panel_1.setBounds(3, 3, (int)(((double)levelp/(double)maxlvp)*(double)272), 29);
               	levelup();
           		}
            	 
            }
        });
		
		JLabel label = new JLabel("���ݷ�");
		label.setForeground(Color.RED);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(35, 477, 62, 18);
		contentPane.add(label);
		
		textField = new JTextField(""+ count1);
		textField.setForeground(Color.RED);
		textField.setBounds(111, 474, 80, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("����");
		label_1.setForeground(Color.BLUE);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(35, 500, 62, 18);
		contentPane.add(label_1);
		
		textField_1 = new JTextField(""+ count2);
		textField_1.setForeground(Color.BLUE);
		textField_1.setColumns(10);
		textField_1.setBounds(111, 497, 80, 21);
		contentPane.add(textField_1);
		
		JLabel label_2 = new JLabel("ü��");
		label_2.setForeground(Color.GREEN);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(35, 526, 62, 18);
		contentPane.add(label_2);
		
		textField_2 = new JTextField(""+ count3);
		textField_2.setForeground(Color.GREEN);
		textField_2.setColumns(10);
		textField_2.setBounds(111, 523, 80, 21);
		contentPane.add(textField_2);
		
		JLabel label_3 = new JLabel("������");
		label_3.setForeground(Color.PINK);
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(214, 477, 62, 18);
		contentPane.add(label_3);
		
		textField_3 = new JTextField(""+ count4);
		textField_3.setForeground(Color.PINK);
		textField_3.setColumns(10);
		textField_3.setBounds(290, 474, 80, 21);
		contentPane.add(textField_3);
		
		JLabel label_4 = new JLabel("������");
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
		textField_5.setFont(new Font("����", Font.BOLD, 16));
		textField_5.setBackground(SystemColor.textHighlightText);
		textField_5.setEnabled(false);
		textField_5.setEditable(false);
		textField_5.setBounds(58, 51, 318, 18);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel(name);//�̸�
		lblNewLabel_2.setBackground(Color.WHITE);
		lblNewLabel_2.setForeground(Color.BLACK);
		lblNewLabel_2.setFont(new Font("����", Font.BOLD, 18));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(146, 12, 130, 27);
		contentPane.add(lblNewLabel_2);
		
		JLabel label_5 = new JLabel("��ø��");
		label_5.setForeground(Color.CYAN);
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(214, 526, 62, 18);
		contentPane.add(label_5);
		
		textField_6 = new JTextField(""+ speed);//��õ��
		textField_6.setForeground(Color.CYAN);
		textField_6.setColumns(10);
		textField_6.setBounds(290, 523, 80, 21);
		contentPane.add(textField_6);
		
		JButton btnNewButton_1 = new JButton("����ġ ����");
		btnNewButton_1.setBounds(460, 426, 105, 27);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("PVP");
		btnNewButton_2.setFont(new Font("����", Font.BOLD | Font.ITALIC, 20));
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
		
		btnNewButton_1.addActionListener(new ActionListener() {//�����ư ���߿� �����ϸ��.
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
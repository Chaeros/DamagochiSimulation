package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;


public class LoginFrame extends JFrame{
	Container logincp;
	JLabel idLabel = new JLabel("      ID");
	JLabel pwLabel = new JLabel("      PW");
	JPanel panel;
	JLabel lblNewLabel;
	JTextField idTextField = new JTextField();
	JPasswordField pwTextField = new JPasswordField();
	JButton loginBtn = new JButton("�α���");
	JButton subscribeBtn = new JButton("ȸ������");
	public SubscribeFrame SubscribeFrame;
	SubscribeFrame sub = new SubscribeFrame();
	public static String IDtemp;//ID ����
	
	boolean loginConfirm =false;
	
	 Timer timer1 = new Timer(30, new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	        
	            if(panel.getHeight() != 135){
	            	panel.setBounds(0,0,LoginFrame.this.getSize().width,panel.getHeight() + 5);
	                if(panel.getHeight() == 135){
	                    timer1.stop();
	                }
	            }
	        }
	    });
	 
	 Timer timer2 = new Timer(30, new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	        
	            if(panel.getHeight() != 0){
	            	panel.setBounds(0,0,LoginFrame.this.getSize().width,panel.getHeight() - 5);
	                if(panel.getHeight() == 0){
	                    timer2.stop();
	                }
	            }
	        }
	    });
	 private final JTextField textField = new JTextField();
	
	LoginFrame() {
		textField.setText("DAMAGOCHI\nSIMULATION");
		textField.setFont(new Font("����", Font.BOLD, 30));
		textField.setBackground(UIManager.getColor("PasswordField.inactiveForeground"));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(117, 27, 676, 171);
		textField.setColumns(10);
		getContentPane().setBackground(UIManager.getColor("Button.darkShadow"));   //�α��� â ����
		/*String dirStr = "D:\\DamaghchiSimulationImages";
		String dirStr1 = "D:\\DamaghchiSimulationImages\\1";
		String dirStr2 = "D:\\DamaghchiSimulationImages\\2";
		String dirStr3 = "D:\\DamaghchiSimulationImages\\3";
		//��θ� ���ڿ��� ���� ���� �ִ�
	    File newFile2 = new File(dirStr);
	    File newFile21 = new File(dirStr1);
	    File newFile22 = new File(dirStr2);
	    File newFile23 = new File(dirStr3);
	    if(newFile2.mkdir()){   //������� ���丮�� ������ �������� �������
	      System.out.println("���� ��ġ �Ϸ�.");
	    }else{
	      System.out.println("�̹� ���ϼ�ġ�� �Ǿ��ֽ��ϴ�.");
	    }
	    
	    if(newFile21.mkdir()){
			Connection con = null;  // DBMS �α��� ���� �Է�
			Statement stmt = null;  // ���� DBMS�� ����� ��ų ������ ������ ���� ��ü(�˻�, ����, ���� ��)
			String url = "jdbc:mysql://localhost/Damagochi?serverTimezone=Asia/Seoul";  //localhost �����ǿ��� chattingservice ��Ű���� ����, ���ѹα� �ð���(����ǥ�ʼ�)
			String user = "root";   //DB ID
			String passwd = "6272"; //DB PW

			try {
				Class.forName("com.mysql.jdbc.Driver");  // jdbc����̹� ������
			} catch(java.lang.ClassNotFoundException e1) {
				System.err.print("ClassNotFoundException: "); 
				System.err.println("����̹� �ε� ����: " + e1.getMessage());
				return;
			} 

			try {
				con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
				stmt = con.createStatement(); // ������ �Ѱ��� Statement ��ü ����, db�� ���α׷����κ��� ����� ���� �� Statement��ü�� �޴´�. ���� �� ��ü�� ��ų ����� �����ϴ� ��ü(���� �� �Ƚ�Ŵ)
	            
				//�̹��� �ҷ�����
				Statement stmt2 = con.createStatement();
		        ResultSet rs = stmt2.executeQuery("SELECT * FROM images WHERE characterNum='1'");
		        
		        try {
		           if(rs.next()){
		               InputStream is = rs.getBinaryStream("normal1");   //�̹��� 250*250
		               InputStream is2 = rs.getBinaryStream("attack1");
		               InputStream is3 = rs.getBinaryStream("attack2");
		               InputStream is4 = rs.getBinaryStream("defence1");
		               String sql ="D:\\DamaghchiSimulationImages\\1/normal1.PNG";
		               String sql2 ="D:\\DamaghchiSimulationImages\\1/attack1.PNG";
		               String sql3 ="D:\\DamaghchiSimulationImages\\1/attack2.PNG";
		               String sql4 ="D:\\DamaghchiSimulationImages\\1/defence1.PNG";
		               FileOutputStream fos = new FileOutputStream(sql);
		               FileOutputStream fos2 = new FileOutputStream(sql2);
		               FileOutputStream fos3 = new FileOutputStream(sql3);
		               FileOutputStream fos4 = new FileOutputStream(sql4);
		               byte[] buff = new byte[8192];
		               byte[] buff2 = new byte[8192];
		               byte[] buff3 = new byte[8192];
		               byte[] buff4 = new byte[8192];
		               int len;
		               int len2;
		               int len3;
		               int len4;
		               while( (len = is.read(buff)) > 0){
		                   fos.write(buff, 0, len);
		               }
		               while( (len2 = is2.read(buff2)) > 0){
		                   fos2.write(buff2, 0, len2);
		               }
		               while( (len3 = is3.read(buff3)) > 0){
		                   fos3.write(buff3, 0, len3);
		               }
		               while( (len4 = is4.read(buff4)) > 0){
		                   fos4.write(buff4, 0, len4);
		               }
		               System.out.println("�̹��� �ҷ����� ����");
		               fos.close();
		               is.close();
		               rs.close();
		               
		               fos2.close();
		               is2.close();
		               
		               fos3.close();
		               is3.close();
		               
		               fos4.close();
		               is4.close();
		           }
		        }
		        catch(Exception e2)
		        {
		        	System.err.println("Select ����: " + e2.getMessage());
		        	System.out.println("�̹��� �ҷ����� ����");
		        }
				stmt.close();
				con.close();	
				
				stmt.close();
				con.close();	

			} catch(SQLException ex) {
				System.err.println("Select ����: " + ex.getMessage());
			}
	    }
	    if(newFile22.mkdir()){}
	    if(newFile23.mkdir()){}*/
	    
		setTitle("login"); 
		logincp = getContentPane();
		logincp.setLayout(null); // �����̳ʿ� ��ġ�����ڸ� ������� ����. (������Ʈ ��ġ/ũ�� ���������� ����.)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // �������� X Ŭ������ â�� �ݾ��� �� ���α׷��� ���� ��Ű�� �޼ҵ�

		//���� ������Ʈ�� ������ ��ġ ����
		idLabel.setBounds(54,246,80,22);
		pwLabel.setBounds(54,337,80,22);
		
		idTextField.setBounds(197,246,200,22);
		pwTextField.setBounds(197,337,200,22);
		loginBtn.setFont(new Font("����", Font.BOLD, 30));
		loginBtn.setForeground(Color.BLACK);
		
		loginBtn.setBounds(593,245,200,114);
		subscribeBtn.setBounds(593,384,200,25);
		
		//�����̳� cp�� ������Ʈ ����
		logincp.add(idLabel);
		logincp.add(pwLabel);
		logincp.add(idTextField);
		logincp.add(pwTextField);
		logincp.add(loginBtn);
		logincp.add(subscribeBtn);
		
		panel = new JPanel();
		panel.setBackground(UIManager.getColor("List.selectionBackground"));
		panel.setBounds(0, 0, 0, 0);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setFont(new Font("����", Font.BOLD, 15));
		lblNewLabel.setBounds(44, 80, 631, 23);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Ȯ��");
		btnNewButton.setFont(new Font("����", Font.BOLD, 15));
		btnNewButton.setBounds(764, 48, 80, 80);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	timer2.start();
	            }
	        });
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("��й�ȣ ���̱�");
		chckbxNewCheckBox.setBounds(407, 335, 131, 27);
		getContentPane().add(chckbxNewCheckBox);
		
		getContentPane().add(textField);
		chckbxNewCheckBox.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	 if(chckbxNewCheckBox.isSelected()){
	            		 pwTextField.setEchoChar((char)0);
	                 }else{
	                	 pwTextField.setEchoChar('*');
	                 }
	            }
	        });
		
		loginBtn.addActionListener(new MyActionListener());
		subscribeBtn.addActionListener(new MyActionListener());
		pwTextField.addActionListener(new MyEnterActionListener());
		//setLocationRelativeTo(null);
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������
		this.setLocation((screenSize.width - frameSize.width)/4, (screenSize.height - frameSize.height)/4);
		setResizable(false);
		setSize(911,510); 
		setVisible(true); 
	}
	
	public static void main(String[] args)
	{
		LoginFrame log = new LoginFrame();
	}
	
	class MyEnterActionListener implements ActionListener{  // �ؽ�Ʈ�ʵ� �����Է½� �׼ǰ�ü �޴� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JTextField b=(JTextField)e.getSource();
			
			if(b==pwTextField)
			{
				Connection con = null;  // DBMS �α��� ���� �Է�
				Statement stmt = null;  // ���� DBMS�� ����� ��ų ������ ������ ���� ��ü(�˻�, ����, ���� ��)
				String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost �����ǿ��� chattingservice ��Ű���� ����, ���ѹα� �ð���(����ǥ�ʼ�)
				String user = "root";   //DB ID
				String passwd = "6272"; //DB PW

				try {
					Class.forName("com.mysql.jdbc.Driver");  // jdbc����̹� ������
				} catch(java.lang.ClassNotFoundException e1) {
					System.err.print("ClassNotFoundException: "); 
					System.err.println("����̹� �ε� ����: " + e1.getMessage());
					return;
				} 

				try {
					con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
					stmt = con.createStatement(); // ������ �Ѱ��� Statement ��ü ����, db�� ���α׷����κ��� ����� ���� �� Statement��ü�� �޴´�. ���� �� ��ü�� ��ų ����� �����ϴ� ��ü(���� �� �Ƚ�Ŵ)
		            
					// client ���̺� �ִ� ��� ���ڵ� �˻� 	
					ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
					System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
				    String tempID="";
					String tempPW="";
			
					// result ��ü�� ����� ���� ����κ��� ���� ���� ����
					while (result.next()) { 
						tempID=result.getString("id");
						tempPW=result.getString("pw");
						System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
						if(idTextField.getText().equals(result.getString("id")) && pwTextField.getText().equals(result.getString("pw")))
						{
							setVisible(false);
							loginConfirm =true;
							System.out.println("�α伺��");
							if(result.getString("logConfirm").equals("unexist")) // ó�� �����ϴ� Ŭ���̾�Ʈ�� ��(�ش� id�� ĳ���Ͱ� �������� ���� ��)
							{
								CreateCharacter createCharacter = new CreateCharacter(idTextField.getText()); //ĳ���� ���� ������
							}
							else // ó�� �����ϴ� Ŭ���̾�Ʈ�� �ƴ� ��(�ش� id�� ĳ���Ͱ� �̹� ������ ��)
							{
				                
								JOptionPane.showMessageDialog(null, "OK ����Ȯ�οϷ�.", "ErroMessage", JOptionPane.INFORMATION_MESSAGE);
								IDtemp=tempID;//���̵� ����
								System.out.println("����� : "+IDtemp);//����� ���̵� Ȯ�ο�
								Button button = new Button(tempID);
							}
							break;
						}
					}
					if(!tempID.equals("") && !tempPW.equals(""))
					{
						if(!idTextField.getText().equals(tempID) || !pwTextField.getText().equals(tempPW) )
						{
							System.out.println("�α����");
							lblNewLabel.setText("Invalide Username Or Password");
							lblNewLabel.setForeground(Color.RED);
				            timer1.start();
							//JOptionPane.showMessageDialog(null, "���������ʴ� ID�� PW �Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE);//���̾�α�
							
						}
					}
					stmt.close();
					con.close();	

				} catch(SQLException ex) {
					System.err.println("Select ����: " + ex.getMessage());
				}
			}
			
		}
		
	}
	
	class MyActionListener implements ActionListener{	// ��ư �̺�Ʈ �߻��� �۵��ϴ� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();  // e�� ���� ��ư�� ��ü�� �޾��ְ� �������� b�� �����Ѵ�.
			
			if(b==loginBtn)
			{
				Connection con = null;  // DBMS �α��� ���� �Է�
				Statement stmt = null;  // ���� DBMS�� ����� ��ų ������ ������ ���� ��ü(�˻�, ����, ���� ��)
				String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost �����ǿ��� chattingservice ��Ű���� ����, ���ѹα� �ð���(����ǥ�ʼ�)
				String user = "root";   //DB ID
				String passwd = "admin"; //DB PW

				try {
					Class.forName("com.mysql.jdbc.Driver");  // jdbc����̹� ������
				} catch(java.lang.ClassNotFoundException e1) {
					System.err.print("ClassNotFoundException: "); 
					System.err.println("����̹� �ε� ����: " + e1.getMessage());
					return;
				} 

				try {
					con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
					stmt = con.createStatement(); // ������ �Ѱ��� Statement ��ü ����, db�� ���α׷����κ��� ����� ���� �� Statement��ü�� �޴´�. ���� �� ��ü�� ��ų ����� �����ϴ� ��ü(���� �� �Ƚ�Ŵ)
		            
					// client ���̺� �ִ� ��� ���ڵ� �˻� 	
					ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
					System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
					String tempID="";
					String tempPW="";
			
					// result ��ü�� ����� ���� ����κ��� ���� ���� ����
					while (result.next()) { 
						tempID=result.getString("id");
						tempPW=result.getString("pw");
						System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
						if(idTextField.getText().equals(result.getString("id")) && pwTextField.getText().equals(result.getString("pw")))
						{
							setVisible(false);
							loginConfirm =true;
							System.out.println("�α伺��");
							if(result.getString("logConfirm").equals("unexist")) // ó�� �����ϴ� Ŭ���̾�Ʈ�� ��(�ش� id�� ĳ���Ͱ� �������� ���� ��)
							{
								CreateCharacter createCharacter = new CreateCharacter(idTextField.getText()); //ĳ���� ���� ������
							}
							else // ó�� �����ϴ� Ŭ���̾�Ʈ�� �ƴ� ��(�ش� id�� ĳ���Ͱ� �̹� ������ ��)
							{
								JOptionPane.showMessageDialog(null, "OK ����Ȯ�οϷ�.", "ErroMessage", JOptionPane.INFORMATION_MESSAGE);
								IDtemp=tempID;//���̵� ����
								System.out.println("����� : "+IDtemp);//���̵� ���� Ȯ�ο�
								Button button = new Button(tempID);
							}
							break;
						}
					}
					if(!tempID.equals("") && !tempPW.equals(""))
					{
						if(!idTextField.getText().equals(tempID) || !pwTextField.getText().equals(tempPW) )
						{
							System.out.println("�α����");
							//JOptionPane.showMessageDialog(null, "���������ʴ� ID�� PW �Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE);//���̾�α�
							lblNewLabel.setText("���������ʴ� ID�� PW �Դϴ�.");
							lblNewLabel.setForeground(Color.RED);
				            timer1.start();
						}
					}
					stmt.close();
					con.close();	

				} catch(SQLException ex) {
					System.err.println("Select ����: " + ex.getMessage());
				}
			}
			else if(b==subscribeBtn)
			{
				//SubscribeFrame subscribe = new SubscribeFrame();
				sub.setVisible(true);
			}
			
		}
	}
}
package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class SubscribeFrame extends JFrame{
	Container logincp;
	JLabel idLabel = new JLabel(" ID   �Է�");
	JLabel pwLabel = new JLabel(" PW �Է�");
	JLabel pwConfirmLael = new JLabel(" PW Ȯ��");
	JTextField idTextField = new JTextField();
	JTextField pwTextField = new JTextField();
	JTextField nameTextField = new JTextField();
	JPasswordField pwConfirmTextField = new JPasswordField();
	JButton subscribeBtn = new JButton("ȸ�����");
	JButton cancelBtn = new JButton("���");
	
	boolean loginConfirm =false;
	
	
	SubscribeFrame() {   //�α��� â ����
		setTitle("ȸ������"); 
		logincp = getContentPane();
		logincp.setLayout(null); // �����̳ʿ� ��ġ�����ڸ� ������� ����. (������Ʈ ��ġ/ũ�� ���������� ����.)
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // �������� X Ŭ������ â�� �ݾ��� �� ���α׷��� ���� ��Ű�� �޼ҵ�

		//���� ������Ʈ�� ������ ��ġ ����
		idLabel.setBounds(10,5,80,22);
		pwLabel.setBounds(10,30,80,22);
		pwConfirmLael.setBounds(10,55,80,22);
		
		idTextField.setBounds(70,5,200,22);
		pwTextField.setBounds(70,30,200,22);
		pwConfirmTextField.setBounds(70,55,200,22);
		
		subscribeBtn.setBounds(10,87,130,25);
		cancelBtn.setBounds(140,87,130,25);
		
		//�����̳� cp�� ������Ʈ ����
		logincp.add(idLabel);
		logincp.add(pwLabel);
		logincp.add(pwConfirmLael);
		logincp.add(idTextField);
		logincp.add(pwTextField);
		logincp.add(pwConfirmTextField);
		logincp.add(subscribeBtn);
		logincp.add(cancelBtn);
		
		subscribeBtn.addActionListener(new MyActionListener());
		cancelBtn.addActionListener(new MyActionListener());
		idTextField.addActionListener(new MyEnterActionListener());
		pwTextField.addActionListener(new MyEnterActionListener());
		pwConfirmTextField.addActionListener(new MyEnterActionListener());
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(300,160); 
		setVisible(false); 
	}
	
	class MyEnterActionListener implements ActionListener{  // �ؽ�Ʈ�ʵ� �����Է½� �׼ǰ�ü �޴� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e)  // �ؽ�Ʈ�� ��Ŀ���� �������� �� Enter�� �Է��ϸ� �̺�Ʈ �߻�
		{
			if(idTextField.getText().equals("")) // id�ؽ�Ʈ �ʵ� ���Է� ��
			{
				JOptionPane.showMessageDialog(null, "ID�� �Է����� �����̽��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
			}
			else if(pwTextField.getText().equals("")) // pw�ؽ�Ʈ �ʵ� ���Է� ��
			{
				JOptionPane.showMessageDialog(null, "PW�� �Է����� �����̽��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
			}
			else if(pwConfirmTextField.getText().equals("")) //pw Ȯ�� �ؽ�Ʈ�ʵ� ���Է� ��
			{
				JOptionPane.showMessageDialog(null, "PW������ �Է����� �����̽��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
			}
			else // ��� �ؽ�Ʈ �ʵ� �Է� ��, db�� �Է����� ������
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
	
					
					ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
					System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
					String tempID="";
					String tempPW="";
			
					// result ��ü�� ����� ���� ����κ��� ���� ���� ����
					while (result.next()) { 
						tempID=result.getString("id");
						tempPW=result.getString("pw");
						System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
						if(idTextField.getText().equals(result.getString("id"))) // �Է��� id�� ������ �����ϴ� id�� ��
						{
							JOptionPane.showMessageDialog(null, "������ �����ϴ� ID�Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
							stmt.close();
							con.close();
							return;
						}
					}
					
					if(!pwTextField.getText().equals(pwConfirmTextField.getText()))  // pw�� pwȮ�ο� �Է� �� ���� ��ġ���� ���� ��
					{
						JOptionPane.showMessageDialog(null, "�Է��� �� PW�� ��ġ���� �ʽ��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
						stmt.close();
						con.close();
						return;
					}
					
					System.out.println("INSERT INTO Login(id,pw,logConfirm) values('"
							+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");  // ���Թ� Ȯ�ο�
					
					
					//���ο� Ŭ���̾�Ʈ ���� ����
					PreparedStatement pre = con.prepareStatement("INSERT INTO Login(id,pw,logConfirm) values('"
							+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");
			
					pre.executeUpdate(); // db�� ����
					System.out.println("Subscriber Information Inserting Successfully!");
					JOptionPane.showMessageDialog(null, "���������� ȸ�����ԵǾ����ϴ�.", "Message", JOptionPane.INFORMATION_MESSAGE); 
					pre.close();
					stmt.close();
					con.close();	
					setVisible(false);
	
				} catch(SQLException ex) {
					System.err.println("Select ����: " + ex.getMessage());
				}
			}
		}
		
	}
	
	class MyActionListener implements ActionListener{	// ��ư �̺�Ʈ �߻��� �۵��ϴ� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			JButton b = (JButton)e.getSource();  // e�� ���� ��ư�� ��ü�� �޾��ְ� �������� b�� �����Ѵ�.
			
			if(b==subscribeBtn)
			{
				if(idTextField.getText().equals(""))  // id�ؽ�Ʈ �ʵ� ���Է� ��
				{
					JOptionPane.showMessageDialog(null, "ID�� �Է����� �����̽��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
				}
				else if(pwTextField.getText().equals("")) //pw�ؽ�Ʈ �ʵ� ���Է½�
				{
					JOptionPane.showMessageDialog(null, "PW�� �Է����� �����̽��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
				}
				else if(pwConfirmTextField.getText().equals("")) // pwȮ�� �ؽ�Ʈ �ʵ� ���Է� ��
				{
					JOptionPane.showMessageDialog(null, "PW������ �Է����� �����̽��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
				}
				else  // ��� �ؽ�Ʈ �ʵ� �Է� ��, db�� �Է����� ������
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
		
						
						ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
						System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
						String tempID="";
						String tempPW="";
				
						// result ��ü�� ����� ���� ����κ��� ���� ���� ����
						while (result.next()) { 
							tempID=result.getString("id");
							tempPW=result.getString("pw");
							System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
							if(idTextField.getText().equals(result.getString("id"))) // �Է��� id�� ������ �����ϴ� id�� ��
							{
								JOptionPane.showMessageDialog(null, "������ �����ϴ� ID�Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
								stmt.close();
								con.close();
								return;
							}
						}
						
						if(!pwTextField.getText().equals(pwConfirmTextField.getText()))  // pw�� pwȮ�ο� �Է� �� ���� ��ġ���� ���� ��
						{
							JOptionPane.showMessageDialog(null, "�Է��� �� PW�� ��ġ���� �ʽ��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
							stmt.close();
							con.close();
							return;
						}
						
						System.out.println("INSERT INTO Login(id,pw,logConfirm) values('"
								+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");  // ���Թ� Ȯ�ο�
						
						
						//���ο� Ŭ���̾�Ʈ ���� ����
						PreparedStatement pre = con.prepareStatement("INSERT INTO Login(id,pw,logConfirm) values('"
								+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");
				
						pre.executeUpdate(); // db�� ����
						System.out.println("Subscriber Information Inserting Successfully!");
						JOptionPane.showMessageDialog(null, "���������� ȸ�����ԵǾ����ϴ�.", "Message", JOptionPane.INFORMATION_MESSAGE); 
						pre.close();
						stmt.close();
						con.close();	
						setVisible(false);
	
					} catch(SQLException ex) {
						System.err.println("Select ����: " + ex.getMessage());
					}
				}
			}
			
		}
			
	}
}

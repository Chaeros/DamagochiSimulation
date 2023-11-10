package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class CreateCharacter extends JFrame{   // ������ Ŭ���̾�Ʈ�� ó������ �α������� ��(������ ĳ���Ͱ� ���� ��) ���� ������
	JLabel explainLa = new JLabel("������ ĳ������ �̸��� �������ּ���.");
	JTextField determineNameTf = new JTextField();
	JButton queryBt = new JButton("ID�ߺ�Ȯ��");
	JLabel resultLa = new JLabel();
	JButton createBtn = new JButton("�����ϱ�");
	String id;
	
	CreateCharacter(String id)
	{
		this.id=id;
		setTitle("ĳ���� ����"); 
		getContentPane();
		setLayout(null); // �����̳ʿ� ��ġ�����ڸ� ������� ����. (������Ʈ ��ġ/ũ�� ���������� ����.)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // �������� X Ŭ������ â�� �ݾ��� �� ���α׷��� ���� ��Ű�� �޼ҵ�
		
		explainLa.setBounds(10,5,220,22);
		determineNameTf.setBounds(10,30,220,22);
		resultLa.setBounds(235,5,100,22);
		queryBt.setBounds(235,30,100,22);
		createBtn.setBounds(100,55,100,22);
		//resultBt
		
		add(explainLa);
		add(determineNameTf);
		add(resultLa);
		add(queryBt);
		add(createBtn);
		
		queryBt.addActionListener(new MyActionListener());
		createBtn.addActionListener(new MyActionListener());
		determineNameTf.addActionListener(new MyEnterActionListener());
		
		setSize(360,123); 
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	class MyActionListener implements ActionListener{	// ��ư �̺�Ʈ �߻��� �۵��ϴ� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();  // e�� ���� ��ư�� ��ü�� �޾��ְ� �������� b�� �����Ѵ�.
			
			if(b==queryBt)  // �Է��� ĳ���� �̸� ������ �����ϴ��� Ȯ���� ���� �̺�Ʈ
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
					ResultSet result = stmt.executeQuery("SELECT * FROM characters"); 
					String tempNAME="";
			
					// result ��ü�� ����� ���� ����κ��� ���� ���� ����
					while (result.next()) { 
						tempNAME=result.getString("name");
						if(tempNAME.equals(determineNameTf.getText()))
						{
							explainLa.setText("    ���Ұ�");
							return;
						}
					}
					explainLa.setText("    ��밡��");
					stmt.close();
					con.close();	

				} catch(SQLException ex) {
					System.err.println("Select ����: " + ex.getMessage());
				}
			}
			else if(b==createBtn)
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
					ResultSet result = stmt.executeQuery("SELECT * FROM characters"); 
					String tempNAME="";
			
					// result ��ü�� ����� ���� ����κ��� ���� ���� ����
					while (result.next()) { 
						tempNAME=result.getString("name");
						if(tempNAME.equals(determineNameTf.getText()))
						{
							JOptionPane.showMessageDialog(null, "�̹� �����ϴ� ĳ���͸��Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					System.out.println("INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvaion,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)");  // ���Թ� Ȯ�ο�
					
					String sql="INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvation,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)";
					
					//���ο� Ŭ���̾�Ʈ ���� ����
					PreparedStatement pre = con.prepareStatement(sql);
					pre.executeUpdate(); // db�� ����
					
					System.out.println("Characters Information Inserting Successfully!");
					
					// login���̺��� ĳ�������翩�� �� ����
					PreparedStatement pre0 = con.prepareStatement("UPDATE login SET logConfirm='exist' WHERE id='"+id+"'");
					pre0.execute();
					System.out.println("ĳ���� exist�� ����");
					
 
							
					setVisible(false);
					JOptionPane.showMessageDialog(null, "���������� ĳ���Ͱ� �����Ǿ����ϴ�.", "Message", JOptionPane.INFORMATION_MESSAGE); 
					pre.close();
					stmt.close();
					con.close();	
					pre0.close();

				} catch(SQLException ex) {
					System.err.println("Select ����: " + ex.getMessage());
				}
			}
			
		}
	}
	
	class MyEnterActionListener implements ActionListener{  // �ؽ�Ʈ�ʵ� �����Է½� �׼ǰ�ü �޴� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JTextField b=(JTextField)e.getSource();
			
			if(b==determineNameTf)
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
					ResultSet result = stmt.executeQuery("SELECT * FROM characters"); 
					String tempNAME="";
			
					// result ��ü�� ����� ���� ����κ��� ���� ���� ����
					while (result.next()) { 
						tempNAME=result.getString("name");
						if(tempNAME.equals(determineNameTf.getText()))
						{
							JOptionPane.showMessageDialog(null, "�̹� �����ϴ� ĳ���͸��Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					System.out.println("INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvaion,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)");  // ���Թ� Ȯ�ο�
					
					String sql="INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvation,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)";
					
					//���ο� Ŭ���̾�Ʈ ���� ����
					PreparedStatement pre = con.prepareStatement(sql);
					pre.executeUpdate(); // db�� ����
					
					System.out.println("Characters Information Inserting Successfully!");
					
					// login���̺��� ĳ�������翩�� �� ����
					PreparedStatement pre0 = con.prepareStatement("UPDATE login SET logConfirm='exist' WHERE id='"+id+"'");
					pre0.execute();
					System.out.println("ĳ���� exist�� ����");
					
 
							
					setVisible(false);
					JOptionPane.showMessageDialog(null, "���������� ĳ���Ͱ� �����Ǿ����ϴ�.", "Message", JOptionPane.INFORMATION_MESSAGE);
					Button button = new Button(id);
					pre.close();
					stmt.close();
					con.close();	
					pre0.close();

				} catch(SQLException ex) {
					System.err.println("Select ����: " + ex.getMessage());
				}
			}
			
		}
		
	}

}

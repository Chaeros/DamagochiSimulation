package damagochi;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import Damagochi_Server.Server.*;
import damagochi.PvPWar.*;

import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame{
	//Ŭ���̾�Ʈ ���� ���� ��ü��
	Socket mySocket = null;
	MessageListener msgListener = null;
	Scanner scn = new Scanner(System.in);
	
	OutputStream outStream =null;
	DataOutputStream dataOutStream = null;
	InputStream inStream;
	DataInputStream dataInStream;
	JButton newroom=new JButton("�游���");
	JButton cancel=new JButton("���ư���");
	
	Container roomPanel = new Container();
	
	int roomCount=0;
	
	ArrayList<RoomName> roomNameList = new ArrayList<>();
	ArrayList<RoomLabel> roomLabel = new ArrayList<>();
	
	PvPWar pvp;
	
	String enterRoomName;
	String id;
	boolean firstChat=true;
	
	WindowAdapter win2 = new MyWindowActionListener2();
	
	Client(String id)  //db������ ���� ���� ĳ���� �ɷ�ġ ������ �⺻Ű�� id������ �޾ƿ´�.
	{
		this.id=id;
		try {
			mySocket = new Socket("localhost",12345); // �ش� IP, port�� ������ ���� ��û �� �����ϴ� ���� ��ȯ
			System.out.println("Client> ������ ������ �Ǿ����ϴ�.");
			msgListener = new MessageListener(mySocket);
			outStream = mySocket.getOutputStream();
			dataOutStream= new DataOutputStream(outStream);
			dataOutStream.writeUTF(id); 
			msgListener.start(); // �޽��� ������ ������ ����
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		setContentPane(new MyPanel());
		setLayout(null);
		setTitle("����"); 
		
		roomPanel.setBounds(40,50,420,200);
		GridLayout grid = new GridLayout(2, 2,20,20);
		roomPanel.setLayout(grid);
		add(roomPanel);
		
		setVisible(true);
		
		setLayout(null);
		
		add(newroom);
		newroom.setBounds(40, 270, 200, 80);
		add(cancel);
		cancel.setBounds(260, 270, 200, 80);
		
		setSize(500, 400);
		
		newroom.addActionListener(new MyActionListener());
		cancel.addActionListener(new MyActionListener());
		
		WindowAdapter win = new MyWindowActionListener();
		addWindowListener(win); // ������â�� ���õ� �׼Ǹ����� add
	}
	
	class MyPanel extends JPanel {  //�׵θ� �� ���м� ������ ���� Ŭ����
		ImageIcon icon = new ImageIcon("D:\\DamaghchiSimulationImages\\back\\pvp1.PNG"); 
		Image img = icon.getImage();
		
		public void paintComponent(Graphics g) { 
			super.paintComponent(g); 
			//g.setColor(Color.gray);
			//g.fillRect(0, 0, 570, 310);    //����
			g.drawRect(40,50,200,89); // �� ������ �׵θ�
			g.drawRect(260,50,200,89); // �� ������ �׵θ�
			g.drawRect(40,161,200,89); // �� ������ �׵θ� 250-89=150+11
			g.drawRect(260,161,200,89); // �� ������ �׵θ�
			//g.drawImage(img, 20,20, this);
			
		} 
	}
	
	class RoomName
	{
		String name;
		JButton roomBtn;
	}
	class RoomLabel
	{
		JLabel roomLabel = new JLabel("                  [�� ���Դϴ�.]");
	}
	
	class MyEnterActionListener implements ActionListener{  // �ؽ�Ʈ�ʵ� �����Է½� �׼ǰ�ü �޴� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JTextField b=(JTextField)e.getSource();
			
			if(b==pvp.chatTf)
			{
				try
				{
					outStream = mySocket.getOutputStream();
					dataOutStream= new DataOutputStream(outStream);
					dataOutStream.writeUTF("$instruct:Chat/"+id+"/"+enterRoomName+"/"+pvp.chatTf.getText());
				}
				catch(Exception e2) 
				{
					System.out.println(e2.toString());
				}
			}
		}
		
	}
	
	class MyActionListener implements ActionListener{	// ��ư �̺�Ʈ �߻��� �۵��ϴ� �׼Ǹ�����
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();  // e�� ���� ��ư�� ��ü�� �޾��ְ� �������� b�� �����Ѵ�.
			if(b==newroom) // �� ����� ��ư Ŭ�� ��
			{
				if(roomCount==4)// ������ �����ϴ� ���� 4���� ��
				{
					JOptionPane.showMessageDialog(null, "������ ���� �� ������ �Ұ��մϴ�.", "Message", JOptionPane.ERROR_MESSAGE);
				}
				else // ������ �����ϴ� ���� 4�� �̸��� ��
				{
					String name = JOptionPane.showInputDialog("���� �� ������ �Է����ּ���."); 
					for(int i=0;i<roomNameList.size();i++)
					{
						if(name.equals(roomNameList.get(i).name)) // �Է��� �� ������ ������ �����ϴ� �� �����̸� ��� ���̾�α� ���
						{
							JOptionPane.showMessageDialog(null, "�̹� �����ϴ� �� �����Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE); 
							return;
						}
					}
					try
					{
						pvp=new PvPWar(name);
						pvp.leaveRoomBtn.addActionListener(new MyActionListener());
						pvp.chatTransmitBtn.addActionListener(new MyActionListener());
						pvp.chatTf.addActionListener(new MyEnterActionListener());
						pvp.stateBtn.addActionListener(new MyActionListener());
						pvp.addWindowListener(win2); // ������â�� ���õ� �׼Ǹ����� add
						enterRoomName=name;
						
						outStream = mySocket.getOutputStream();
						dataOutStream= new DataOutputStream(outStream);
						dataOutStream.writeUTF("$instruct:NewRoom"+"/"+name+"/"+id);
						
						pvp.setVisible(true);
						setVisible(false);
					}
					catch(Exception e2) 
					{
						System.out.println(e2.toString());
					}
				}
			}
			else if(b==cancel)
			{
				try
				{
					outStream = mySocket.getOutputStream();
					dataOutStream= new DataOutputStream(outStream);
					dataOutStream.writeUTF("$instruct:Logout"+"/"+id);
					System.out.println(id+"logout ��ȣ ����");
				}
				catch(Exception e3)
				{
					e3.printStackTrace();
				}
			}
			else
			{
				if(pvp==null)
				{
					
				}
				else
				{
					if(b==pvp.leaveRoomBtn)
					{
						System.out.println("pvpleave��ư �۵�");
						try
						{
							outStream = mySocket.getOutputStream();
							dataOutStream= new DataOutputStream(outStream);
							dataOutStream.writeUTF("$instruct:LeaveRoom/"+pvp.roomName+"/"+id);
						}
						catch(Exception e2) 
						{
							System.out.println(e2.toString());
						}
					}
					else if(b==pvp.chatTransmitBtn)
					{
						try
						{
							outStream = mySocket.getOutputStream();
							dataOutStream= new DataOutputStream(outStream);
							dataOutStream.writeUTF("$instruct:Chat/"+id+"/"+enterRoomName+"/"+pvp.chatTf.getText());
						}
						catch(Exception e2) 
						{
							System.out.println(e2.toString());
						}
					}
					else if(b==pvp.stateBtn)
					{
						if(b.getText().equals("�غ�Ϸ�")) {
							pvp.stateBtn.setText("���");
							try {
								outStream = mySocket.getOutputStream();
								dataOutStream= new DataOutputStream(outStream);
								dataOutStream.writeUTF("$instruct:ready");
							}catch(Exception e1)
							{
								System.out.println(e1.toString());
							}
						}
						else if(b.getText().equals("���")) {
							pvp.stateBtn.setText("�غ�Ϸ�");
							try {
								outStream = mySocket.getOutputStream();
								dataOutStream= new DataOutputStream(outStream);
								dataOutStream.writeUTF("$instruct:unready");
							}catch(Exception e2)
							{
								System.out.println(e2.toString());
							}
						}
					}
				}
				
				for(int i=0;i<roomNameList.size();i++)
				{
					if(b==roomNameList.get(i).roomBtn)
					{
						System.out.println("�������ư");
						try
						{
							outStream = mySocket.getOutputStream();
							dataOutStream= new DataOutputStream(outStream);
							dataOutStream.writeUTF("$instruct:EnterRoom/"+roomNameList.get(i).name+"/"+id);
						}
						catch(Exception e2) 
						{
							System.out.println(e2.toString());
						}
					}
				}
			}
		}
	}
	
	class MessageListener extends Thread{ // �����κ��� ���۵Ǵ� �޽����� �޾Ƶ��̴� ������
		Socket socket;
		
		String P1_HP;
		String P1_FULLHP;
		String P1_BeDamaged;
		String P1_ID;
		
		String P2_HP;
		String P2_FULLHP;
		String P2_BeDamaged;
		String P2_ID;
		
		MessageListener(Socket _s){
			this.socket=_s;
		}
		
		public void run()
		{
			try {
				inStream = this.socket.getInputStream();
				dataInStream = new DataInputStream(inStream);
				String welcomeMsg = dataInStream.readUTF(); // ������ ������ ���ڿ��� ���� ������ �����·� �ִ´�.
				System.out.println(welcomeMsg);
				
				while(true) 
				{
					String msg = dataInStream.readUTF(); // ������ ������ ���ڿ��� ���� ������ �����·� �ִ´�.
					System.out.println(msg);
					String line[]=msg.split("\\/"); //  '/'�� ���ڿ��� �������´�.
					System.out.println(line[0]);
					
					String chatMessage="";
					System.out.println("line.length = " +line.length);
					for(int a=1;a<line.length;a++)
					{
						chatMessage=chatMessage+line[a];
					}
					System.out.println("chatMessage = " +chatMessage);
					
					if(line[0].equals("$instruct:ExistRoom"))  // Ŭ���̾�Ʈ�� ���� ��, ������ �����ϴ� �� ������ �����κ��� ���� ����.
					{
						roomNameList.add(new RoomName());
						roomNameList.get(roomCount).name=line[1];
						roomNameList.get(roomCount).roomBtn=new JButton(line[1]);
						roomNameList.get(roomCount).roomBtn.addActionListener(new MyActionListener());
						roomCount++;
						
					}
					else if(line[0].equals("$instruct:EndExistRoom")) // ������ �����ϴ� ���� ��� ������ ���� �� �����κ��� ���� �Ϸ� ��ȣ�� ����.
					{
						for(int i=0;i<roomCount;i++) // �޾ƿ� ���, �гο� ����
						{
							roomPanel.add(roomNameList.get(i).roomBtn);
						}
						for(int i=0;i<4-roomCount;i++) // 2*2 �׸��� ���̾ƿ��� ������Ʈ�� �� ä������ ������ ����� ������ ���ϱ� ������ ������ ������Ʈ �� ��ŭ ���� �������ش�.
						{
							roomLabel.add(new RoomLabel());
							roomPanel.add(roomLabel.get(i).roomLabel);
						}
					}
					else if(line[0].equals("$instruct:NewRoom"))  // ���ο� ���� ���� �� ��, �����κ��� �ش� ���� ������ ���� ����.
					{
						roomPanel.removeAll(); // �׸��� ���̾ƿ� �г��� ��� ������Ʈ ����
						System.out.println("�Ϸ�:"+roomLabel.size());
						System.out.println("����");
						roomNameList.add(new RoomName());
						roomNameList.get(roomCount).name=line[1];
						roomNameList.get(roomCount).roomBtn=new JButton(line[1]);
						roomNameList.get(roomCount).roomBtn.addActionListener(new MyActionListener());
						roomCount++;
						
						for(int i=0;i<roomCount;i++) // 2*2 �׸��� ���̾ƿ��� ������Ʈ�� �� ä������ ������ ����� ������ ���ؼ� ������ ������Ʈ �� ��ŭ ���� �������ش�.
						{
							roomPanel.add(roomNameList.get(i).roomBtn);
						}
						for(int i=0;i<4-roomCount;i++) // �׸��� ���̾ƿ��� �� ä���ֱ� ���� �� �гο� �߰�
						{
							roomLabel.add(new RoomLabel());
							roomPanel.add(roomLabel.get(i).roomLabel);
						}
						setSize(501, 401);
						setSize(500, 400);
					}
					else if(line[0].equals("$instruct:NewRoomCreator"))  // pvp �� ������ Ȯ������ ����
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
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+id+"'"); 
					
							// result ��ü�� ����� ���� ����κ��� ���� ���� ����
							while (result.next()) { 								
								pvp.player1LevelLa.setText("Lv."+result.getString("level"));
								pvp.player1NameLa.setText(result.getString("name"));
								pvp.player1StaminaLa.setText(result.getString("stamina")+"/"+result.getString("stamina"));
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+result.getString("characterNum")+"/normal1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								pvp.player1Stamina.setBounds(70,10,130,20);
							}
							stmt.close();
							con.close();	

						} catch(SQLException ex) {
							System.err.println("Select ����: " + ex.getMessage());
						}
						
						pvp.setVisible(true);
						setVisible(false);
					}
					else if(line[0].equals("$instruct:LeaveRoom"))  // pvp �� ������ Ȯ������ ����
					{
						System.out.println("$instruct:LeaveRoom����");
						pvp.player1LevelLa.setText("");
						pvp.player1NameLa.setText("");
						pvp.player1StaminaLa.setText("");
						pvp.player1ImageIcon=null;
						pvp.player1ImageLa.setIcon(null);
						pvp.player1DamageLa.setText("");
						pvp.player1Stamina.setBounds(70,10,0,20);
						
						pvp.player2LevelLa.setText("");
						pvp.player2NameLa.setText("");
						pvp.player2StaminaLa.setText("");
						pvp.player2ImageIcon=null;
						pvp.player2ImageLa.setIcon(null);
						pvp.player2DamageLa.setText("");
						pvp.player2Stamina.setBounds(360,10,0,20);
						setVisible(true);
						pvp.setVisible(false);
						
						pvp=null;
					}
					else if(line[0].equals("$instruct:RoomRemove"))  // pvp �� ������ ���� ���� ����
					{
						for(int i=0;i<roomNameList.size();i++)
						{
							if(roomNameList.get(i).name.equals(line[1]))
							{
								roomNameList.remove(i);
								roomCount--;
								
								roomPanel.removeAll(); // �׸��� ���̾ƿ� �г��� ��� ������Ʈ ����
								for(int j=0;j<roomCount;j++) // 2*2 �׸��� ���̾ƿ��� ������Ʈ�� �� ä������ ������ ����� ������ ���ؼ� ������ ������Ʈ �� ��ŭ ���� �������ش�.
								{
									roomPanel.add(roomNameList.get(j).roomBtn);
								}
								for(int k=0;k<4-roomCount;k++) // �׸��� ���̾ƿ��� �� ä���ֱ� ���� �� �гο� �߰�
								{
									roomLabel.add(new RoomLabel());
									roomPanel.add(roomLabel.get(k).roomLabel);
								}
								setSize(501, 401);
								setSize(500, 400);
								
								break;
							}
						}
					}
					else if(line[0].equals("$instruct:Player1ID"))  // ������ pvp �� player1�� ���̵� �޾ƿ´�.
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
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[1]+"'"); 
					
							// result ��ü�� ����� ���� ����κ��� ���� ���� ����
							while (result.next()) { 						
								if(pvp==null)
								{
									pvp = new PvPWar(line[2]);
									enterRoomName=line[2];
									pvp.leaveRoomBtn.addActionListener(new MyActionListener());
									pvp.chatTransmitBtn.addActionListener(new MyActionListener());
									pvp.chatTf.addActionListener(new MyEnterActionListener());
									pvp.stateBtn.addActionListener(new MyActionListener());
									pvp.addWindowListener(win2); // ������â�� ���õ� �׼Ǹ����� add
									pvp.setVisible(true);
									setVisible(false);
								}
								pvp.player1LevelLa.setText("Lv."+result.getString("level"));
								pvp.player1NameLa.setText(result.getString("name"));
								pvp.player1StaminaLa.setText(result.getString("stamina")+"/"+result.getString("stamina"));
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+result.getString("characterNum")+"/normal1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								pvp.player1Stamina.setBounds(70,10,130,20);
							}
							stmt.close();
							con.close();	

						} catch(SQLException ex) {
							System.err.println("Select ����: " + ex.getMessage());
						}
						
						//�ڽ� ���� �ҷ�����
						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
							stmt = con.createStatement(); // ������ �Ѱ��� Statement ��ü ����, db�� ���α׷����κ��� ����� ���� �� Statement��ü�� �޴´�. ���� �� ��ü�� ��ų ����� �����ϴ� ��ü(���� �� �Ƚ�Ŵ)
				            
							// client ���̺� �ִ� ��� ���ڵ� �˻� 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+id+"'"); 
					
							// result ��ü�� ����� ���� ����κ��� ���� ���� ����
							while (result.next()) { 		
								pvp.player2LevelLa.setText("Lv."+result.getString("level"));
								pvp.player2NameLa.setText(result.getString("name"));
								pvp.player2StaminaLa.setText(result.getString("stamina")+"/"+result.getString("stamina"));
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+result.getString("characterNum")+"/normal1.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
								pvp.player2Stamina.setBounds(360,10,130,20);
							}
							stmt.close();
							con.close();	

						} catch(SQLException ex) {
							System.err.println("Select ����: " + ex.getMessage());
						}
					}
					else if(line[0].equals("$instruct:Player2ID"))  // ������ pvp �� player2�� ���̵� �޾ƿ´�.
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

						//������� �ҷ�����
						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
							stmt = con.createStatement(); // ������ �Ѱ��� Statement ��ü ����, db�� ���α׷����κ��� ����� ���� �� Statement��ü�� �޴´�. ���� �� ��ü�� ��ų ����� �����ϴ� ��ü(���� �� �Ƚ�Ŵ)
				            
							// client ���̺� �ִ� ��� ���ڵ� �˻� 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[1]+"'"); 
					
							// result ��ü�� ����� ���� ����κ��� ���� ���� ����
							while (result.next()) { 		
								if(pvp==null)
								{
									pvp = new PvPWar(line[2]);
									enterRoomName=line[2];
									pvp.leaveRoomBtn.addActionListener(new MyActionListener());
									pvp.chatTransmitBtn.addActionListener(new MyActionListener());
									pvp.chatTf.addActionListener(new MyEnterActionListener());
									pvp.stateBtn.addActionListener(new MyActionListener());
									pvp.addWindowListener(win2); // ������â�� ���õ� �׼Ǹ����� add
									pvp.setVisible(true);
									setVisible(false);
								}
								pvp.player2LevelLa.setText("Lv."+result.getString("level"));
								pvp.player2NameLa.setText(result.getString("name"));
								pvp.player2StaminaLa.setText(result.getString("stamina")+"/"+result.getString("stamina"));
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+result.getString("characterNum")+"/normal1.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
								pvp.player2Stamina.setBounds(360,10,130,20);
							}
							stmt.close();
							con.close();	

						} catch(SQLException ex) {
							System.err.println("Select ����: " + ex.getMessage());
						}
						
						//�ڽ� ���� �ҷ�����
						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection ��ü getter�޼ҵ�� ȣ�� //URL,(DB)ID,PW �Է�  ->db�� ����
							stmt = con.createStatement(); // ������ �Ѱ��� Statement ��ü ����, db�� ���α׷����κ��� ����� ���� �� Statement��ü�� �޴´�. ���� �� ��ü�� ��ų ����� �����ϴ� ��ü(���� �� �Ƚ�Ŵ)
				            
							// client ���̺� �ִ� ��� ���ڵ� �˻� 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+id+"'"); 
					
							// result ��ü�� ����� ���� ����κ��� ���� ���� ����
							while (result.next()) { 		
								pvp.player1LevelLa.setText("Lv."+result.getString("level"));
								pvp.player1NameLa.setText(result.getString("name"));
								pvp.player1StaminaLa.setText(result.getString("stamina")+"/"+result.getString("stamina"));
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+result.getString("characterNum")+"/normal1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								pvp.player1Stamina.setBounds(70,10,130,20);
							}
							stmt.close();
							con.close();	

						} catch(SQLException ex) {
							System.err.println("Select ����: " + ex.getMessage());
						}
						
						
					}
					else if(line[0].equals("$instruct:FullMember"))  // �����Ϸ��� ���� �ο��� �� á�� �� �޴� ��ɾ�
					{
						JOptionPane.showMessageDialog(null, "�����ο��� ��� �� ���Դϴ�.", "Message", JOptionPane.ERROR_MESSAGE);
					}
					else if(line[0].equals("$instruct:Player1Leave"))  // ������ ���� player1�� ������ ��
					{
						pvp.player1LevelLa.setText("Lv.");
						pvp.player1NameLa.setText("");
						pvp.player1StaminaLa.setText("");
						pvp.player1ImageIcon=null;
						pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
						pvp.player1Stamina.setBounds(70,10,0,20);
						pvp.stateBtn.setVisible(false);
						pvp.stateLa.setVisible(true);
						pvp.player1DamageLa.setText("");
						pvp.player2DamageLa.setText("");
					}
					else if(line[0].equals("$instruct:Player2Leave"))  // ������ ���� player2�� ������ ��
					{
						pvp.player2LevelLa.setText("Lv.");
						pvp.player2NameLa.setText("");
						pvp.player2StaminaLa.setText("");
						pvp.player2ImageIcon=null;
						pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
						pvp.player2Stamina.setBounds(360,10,0,20);
						pvp.stateBtn.setVisible(false);
						pvp.stateLa.setVisible(true);
						pvp.player1DamageLa.setText("");
						pvp.player2DamageLa.setText("");
					}
					else if(line[0].equals("$instruct:Chat"))  // ������ ���� player2�� ������ ��
					{
						if(firstChat==true) {
							pvp.chatLog=chatMessage;
							firstChat=false;
						}
						else
						{
							pvp.chatLog=pvp.chatLog+"\n"+chatMessage;
						}
						pvp.chatTArea.setText(pvp.chatLog);
						pvp.chatTf.setText("");
					}
					/*else if(line[0].equals("$instruct:Ready"))  // �غ��ư Ȱ��ȭ
					{
						pvp.stateLa.setVisible(false);
						pvp.stateBtn.setVisible(true);
						pvp.stateBtn.addActionListener(new MyActionListener());
						
						setSize(501, 401);
						setSize(500, 400);
					}*/
					
					//�߰�
					if(line[0].equals("$instruct:readyPlayer"))
					{//�÷��̾ 2���� �Ǹ� ��⸦ ����
						pvp.stateBtn.setVisible(true);
						pvp.leaveRoomLa.setVisible(true);
						pvp.leaveRoomBtn.setVisible(true);
						pvp.stateLa.setVisible(false);
					}
					else if(line[0].equals("$instruct:start")) {
						//�÷��̾� 2���� ready�� ������ ������ ����
						P1_ID=line[1];
						P2_ID=line[2];
						SetID SI=new SetID(P1_ID,P2_ID);
						String dummy[]=SI.result.split("/");
						pvp.player1LevelLa.setText("Lv. "+ dummy[0]);
						pvp.player1NameLa.setText(dummy[1]);
						pvp.player1ImageLa.setText(dummy[2]);
						pvp.player2LevelLa.setText("Lv. "+ dummy[3]);
						pvp.player2NameLa.setText(dummy[4]);
						pvp.player2ImageLa.setText(dummy[5]);
						
					}
					else if(line[0].equals("$instruct:setHP")) {
						P1_HP=line[1];
						P2_HP=line[2];
						P1_FULLHP=P1_HP;
						P2_FULLHP=P2_HP;
						//HP������ ����
						pvp.player1StaminaLa.setText(P1_HP+"/"+P1_FULLHP);
						pvp.player2StaminaLa.setText(P2_HP+"/"+P2_FULLHP);
					}
					else if(line[0].equals("$instruct:attack")) {
						pvp.stateBtn.setVisible(false);
						pvp.stateLa.setText("         ���� ��");
						pvp.stateLa.setVisible(true);
						pvp.leaveRoomBtn.setVisible(false);
						if(line[1].equals(P1_ID)) {
							P1_HP=line[3];
							P1_BeDamaged=line[2];
							try {   // ���� �ִϸ��̼�
								sleep(200);
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/player2attack1.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
								sleep(200);
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[4]+"/defence1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/player2attack2.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
								pvp.player1DamageLa.setText("-"+P1_BeDamaged);
								sleep(200);
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/player2attack1.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
								sleep(200);
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/normal1.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[4]+"/normal1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							pvp.player1DamageLa.setText("");
							pvp.player2DamageLa.setText("");
						}
						else if(line[1].equals(P2_ID)) {
							P2_HP=line[3];
							P2_BeDamaged=line[2];
							try {   // ���� �ִϸ��̼�
								sleep(200);
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/player1attack1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								sleep(200);
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/player1attack2.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[4]+"/defence1.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
								pvp.player2DamageLa.setText("-"+P2_BeDamaged);
								sleep(200);
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/player1attack1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								sleep(200);
								pvp.player1ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[5]+"/normal1.PNG");
								pvp.player1ImageLa.setIcon(pvp.player1ImageIcon);
								pvp.player2ImageIcon=new ImageIcon("D:\\DamaghchiSimulationImages\\"+line[4]+"/normal1.PNG");
								pvp.player2ImageLa.setIcon(pvp.player2ImageIcon);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							pvp.player2DamageLa.setText("");
							pvp.player1DamageLa.setText("");
						}//������ �ϸ� ���ݴ��� ������ ����
						pvp.player1StaminaLa.setText(P1_HP+"/"+P1_FULLHP);
						pvp.player2StaminaLa.setText(P2_HP+"/"+P2_FULLHP);
						
						double staminaGage1x = Double.parseDouble(P1_HP) /Double.parseDouble(P1_FULLHP);
						double staminaGage2x = Double.parseDouble(P2_HP) /Double.parseDouble(P2_FULLHP);
						
						System.out.println(staminaGage1x + "and" + staminaGage2x );
						System.out.println((int)(130.0*staminaGage1x) + "and" + (int)(130.0*staminaGage2x) );
						
						pvp.player1Stamina.setBounds(70,10,(int)(130.0*staminaGage1x),20);
						pvp.player2Stamina.setBounds(360,10,(int)(130.0*staminaGage2x),20);
						
						//GUI����
					}
					else if(line[0].equals("$instruct:win")) {
						//ĳ���Ͱ� �¸��ϸ� �¸��� ���
						if(line[1].equals(P2_ID)) {
							System.out.println("�÷��̾� 2 �¸�");
							pvp.stateBtn.setVisible(true);
							pvp.leaveRoomLa.setVisible(false);
							pvp.leaveRoomBtn.setVisible(true);
							pvp.stateLa.setVisible(false);
							pvp.stateBtn.setText("�غ�Ϸ�");
							pvp.stateLa.setText("         ���� ��");
							pvp.player1DamageLa.setText("");
							pvp.player2DamageLa.setText("");
							pvp.player1Stamina.setBounds(70,10,130,20);
							pvp.player2Stamina.setBounds(360,10,130,20);
							pvp.player1StaminaLa.setText(P1_FULLHP+"/"+P1_FULLHP);
							pvp.player2StaminaLa.setText(P2_FULLHP+"/"+P2_FULLHP);
							if(P2_ID.equals(id))
							{
								PvPResultFrame pvpResult = new PvPResultFrame(id,"win");
							}
							else
							{
								PvPResultFrame pvpResult = new PvPResultFrame(id,"defeat");
							}
						}
						if(line[1].equals(P1_ID)) {
							System.out.println("�÷��̾� 1 �¸�");
							pvp.stateBtn.setVisible(true);
							pvp.leaveRoomLa.setVisible(false);
							pvp.leaveRoomBtn.setVisible(true);
							pvp.stateLa.setVisible(false);
							pvp.stateBtn.setText("�غ�Ϸ�");
							pvp.stateLa.setText("         ���� ��");
							pvp.player1DamageLa.setText("");
							pvp.player2DamageLa.setText("");
							pvp.player1Stamina.setBounds(70,10,130,20);
							pvp.player2Stamina.setBounds(360,10,130,20);
							pvp.player1StaminaLa.setText(P1_FULLHP+"/"+P1_FULLHP);
							pvp.player2StaminaLa.setText(P2_FULLHP+"/"+P2_FULLHP);
							if(P1_ID.equals(id))
							{
								PvPResultFrame pvpResult = new PvPResultFrame(id,"win");
							}
							else
							{
								PvPResultFrame pvpResult = new PvPResultFrame(id,"defeat");
							}
						}
					}
				}
				
			}catch(IOException e) {
				System.out.println(e.toString());
			}
			
		}
		
	}
	
	class MyWindowActionListener extends WindowAdapter{ // ������ â �׼ǰ� ���õ� �׼� ������(X��ư ���� â ���� �� �׼ǹޱ����� ���)
		@Override
		public void windowClosing(WindowEvent e)
		{
			int safe = JOptionPane.showConfirmDialog(null, "���α׷��� �����Ͻðڽ��ϱ�?",  "���α׷� ����", JOptionPane.YES_NO_OPTION);

			if(safe == JOptionPane.YES_OPTION){  // yes ������ �����
				try {
					outStream = mySocket.getOutputStream();
					dataOutStream= new DataOutputStream(outStream);
					dataOutStream.writeUTF("$instruct:Logout"+"/"+id);
					System.exit(0);		
				}catch(Exception e2)
				{
					System.out.println(e2.toString());
				}
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // �������� X Ŭ������ â�� �ݾ��� �� ���α׷��� ���� ��Ű�� �޼ҵ�

			} else if (safe == JOptionPane.NO_OPTION) {
			    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//cancel ������ ����ȵ�
			    setVisible(true);
			}
		}
	}
	
	class MyWindowActionListener2 extends WindowAdapter{ // ������ â �׼ǰ� ���õ� �׼� ������(X��ư ���� â ���� �� �׼ǹޱ����� ���)
		@Override
		public void windowClosing(WindowEvent e)
		{
			System.out.println("1�۵�");
			int safe = JOptionPane.showConfirmDialog(null, "PvP���� �� Ż�� �� �������� �߻��� �� �ֽ��ϴ�. ���� Ż���Ͻðڽ��ϱ�?",  "���α׷� ����", JOptionPane.YES_NO_OPTION);

			if(safe == JOptionPane.YES_OPTION){  // yes ������ �����
				System.out.println("2�۵�");
				try {
					outStream = mySocket.getOutputStream();
					dataOutStream= new DataOutputStream(outStream);
					dataOutStream.writeUTF("$instruct:Logout"+"/"+id);
					System.exit(0);		
				}catch(Exception e2)
				{
					System.out.println(e2.toString());
				}
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // �������� X Ŭ������ â�� �ݾ��� �� ���α׷��� ���� ��Ű�� �޼ҵ�

			} else if (safe == JOptionPane.NO_OPTION) {
				System.out.println("3�۵�");
			    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//cancel ������ ����ȵ�
			}
		}
	}
	
}
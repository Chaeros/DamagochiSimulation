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
	//클라이언트 소켓 관련 객체들
	Socket mySocket = null;
	MessageListener msgListener = null;
	Scanner scn = new Scanner(System.in);
	
	OutputStream outStream =null;
	DataOutputStream dataOutStream = null;
	InputStream inStream;
	DataInputStream dataInStream;
	JButton newroom=new JButton("방만들기");
	JButton cancel=new JButton("돌아가기");
	
	Container roomPanel = new Container();
	
	int roomCount=0;
	
	ArrayList<RoomName> roomNameList = new ArrayList<>();
	ArrayList<RoomLabel> roomLabel = new ArrayList<>();
	
	PvPWar pvp;
	
	String enterRoomName;
	String id;
	boolean firstChat=true;
	
	WindowAdapter win2 = new MyWindowActionListener2();
	
	Client(String id)  //db내용을 쓰기 위해 캐릭터 능력치 정보의 기본키인 id정보를 받아온다.
	{
		this.id=id;
		try {
			mySocket = new Socket("localhost",12345); // 해당 IP, port의 서버로 연결 요청 후 연결하는 소켓 반환
			System.out.println("Client> 서버로 연결이 되었습니다.");
			msgListener = new MessageListener(mySocket);
			outStream = mySocket.getOutputStream();
			dataOutStream= new DataOutputStream(outStream);
			dataOutStream.writeUTF(id); 
			msgListener.start(); // 메시지 리스너 스레드 실행
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		setContentPane(new MyPanel());
		setLayout(null);
		setTitle("대기실"); 
		
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
		addWindowListener(win); // 윈도우창과 관련된 액션리스너 add
	}
	
	class MyPanel extends JPanel {  //테두리 및 구분선 생성을 위한 클래스
		ImageIcon icon = new ImageIcon("D:\\DamaghchiSimulationImages\\back\\pvp1.PNG"); 
		Image img = icon.getImage();
		
		public void paintComponent(Graphics g) { 
			super.paintComponent(g); 
			//g.setColor(Color.gray);
			//g.fillRect(0, 0, 570, 310);    //배경색
			g.drawRect(40,50,200,89); // 방 나가기 테두리
			g.drawRect(260,50,200,89); // 방 나가기 테두리
			g.drawRect(40,161,200,89); // 방 나가기 테두리 250-89=150+11
			g.drawRect(260,161,200,89); // 방 나가기 테두리
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
		JLabel roomLabel = new JLabel("                  [빈 방입니다.]");
	}
	
	class MyEnterActionListener implements ActionListener{  // 텍스트필드 엔터입력시 액션객체 받는 액션리스너
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
	
	class MyActionListener implements ActionListener{	// 버튼 이벤트 발생시 작동하는 액션리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();  // e가 누른 버튼의 객체를 받아주고 참조변수 b에 대입한다.
			if(b==newroom) // 방 만들기 버튼 클릭 시
			{
				if(roomCount==4)// 기존에 존재하는 방이 4개일 때
				{
					JOptionPane.showMessageDialog(null, "공간이 없어 방 생성이 불가합니다.", "Message", JOptionPane.ERROR_MESSAGE);
				}
				else // 기존에 존재하는 방이 4개 미만일 때
				{
					String name = JOptionPane.showInputDialog("만들 방 제목을 입력해주세요."); 
					for(int i=0;i<roomNameList.size();i++)
					{
						if(name.equals(roomNameList.get(i).name)) // 입력한 방 제목이 기존에 존재하는 방 제목이면 경고 다이얼로그 출력
						{
							JOptionPane.showMessageDialog(null, "이미 존재하는 방 제목입니다.", "Message", JOptionPane.ERROR_MESSAGE); 
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
						pvp.addWindowListener(win2); // 윈도우창과 관련된 액션리스너 add
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
					System.out.println(id+"logout 신호 보냄");
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
						System.out.println("pvpleave버튼 작동");
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
						if(b.getText().equals("준비완료")) {
							pvp.stateBtn.setText("취소");
							try {
								outStream = mySocket.getOutputStream();
								dataOutStream= new DataOutputStream(outStream);
								dataOutStream.writeUTF("$instruct:ready");
							}catch(Exception e1)
							{
								System.out.println(e1.toString());
							}
						}
						else if(b.getText().equals("취소")) {
							pvp.stateBtn.setText("준비완료");
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
						System.out.println("방입장버튼");
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
	
	class MessageListener extends Thread{ // 서버로부터 전송되는 메시지를 받아들이는 스레드
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
				String welcomeMsg = dataInStream.readUTF(); // 서버가 보내는 문자열을 받을 때까지 대기상태로 있는다.
				System.out.println(welcomeMsg);
				
				while(true) 
				{
					String msg = dataInStream.readUTF(); // 서버가 보내는 문자열을 받을 때까지 대기상태로 있는다.
					System.out.println(msg);
					String line[]=msg.split("\\/"); //  '/'로 문자열을 구분짓는다.
					System.out.println(line[0]);
					
					String chatMessage="";
					System.out.println("line.length = " +line.length);
					for(int a=1;a<line.length;a++)
					{
						chatMessage=chatMessage+line[a];
					}
					System.out.println("chatMessage = " +chatMessage);
					
					if(line[0].equals("$instruct:ExistRoom"))  // 클라이언트가 접속 시, 기존에 존재하던 방 정보들 서버로부터 전송 받음.
					{
						roomNameList.add(new RoomName());
						roomNameList.get(roomCount).name=line[1];
						roomNameList.get(roomCount).roomBtn=new JButton(line[1]);
						roomNameList.get(roomCount).roomBtn.addActionListener(new MyActionListener());
						roomCount++;
						
					}
					else if(line[0].equals("$instruct:EndExistRoom")) // 기존에 존재하던 방의 모든 정보를 받은 뒤 서버로부터 전송 완료 신호를 받음.
					{
						for(int i=0;i<roomCount;i++) // 받아온 방들, 패널에 부착
						{
							roomPanel.add(roomNameList.get(i).roomBtn);
						}
						for(int i=0;i<4-roomCount;i++) // 2*2 그리드 레이아웃에 컴포넌트를 꽉 채워주지 않으면 모양이 예쁘지 못하기 때문에 부족한 컴포넌트 수 만큼 라벨을 삽입해준다.
						{
							roomLabel.add(new RoomLabel());
							roomPanel.add(roomLabel.get(i).roomLabel);
						}
					}
					else if(line[0].equals("$instruct:NewRoom"))  // 새로운 방이 생성 될 때, 서버로부터 해당 방의 정보를 전송 받음.
					{
						roomPanel.removeAll(); // 그리드 레이아웃 패널의 모든 컴포넌트 삭제
						System.out.println("완료:"+roomLabel.size());
						System.out.println("뉴룸");
						roomNameList.add(new RoomName());
						roomNameList.get(roomCount).name=line[1];
						roomNameList.get(roomCount).roomBtn=new JButton(line[1]);
						roomNameList.get(roomCount).roomBtn.addActionListener(new MyActionListener());
						roomCount++;
						
						for(int i=0;i<roomCount;i++) // 2*2 그리드 레이아웃에 컴포넌트를 꽉 채워주지 않으면 모양이 예쁘지 못해서 부족한 컴포넌트 수 만큼 라벨을 삽입해준다.
						{
							roomPanel.add(roomNameList.get(i).roomBtn);
						}
						for(int i=0;i<4-roomCount;i++) // 그리드 레이아웃을 다 채워주기 위해 라벨 패널에 추가
						{
							roomLabel.add(new RoomLabel());
							roomPanel.add(roomLabel.get(i).roomLabel);
						}
						setSize(501, 401);
						setSize(500, 400);
					}
					else if(line[0].equals("$instruct:NewRoomCreator"))  // pvp 방 나가기 확인전송 받음
					{
						Connection con = null;  // DBMS 로그인 정보 입력
						Statement stmt = null;  // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
						String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost 아이피에서 chattingservice 스키마에 접근, 대한민국 시간대(물음표필수)
						String user = "root";   //DB ID
						String passwd = "6272"; //DB PW

						try {
							Class.forName("com.mysql.jdbc.Driver");  // jdbc드라이버 가져옴
						} catch(java.lang.ClassNotFoundException e1) {
							System.err.print("ClassNotFoundException: "); 
							System.err.println("드라이버 로딩 오류: " + e1.getMessage());
							return;
						} 

						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection 객체 getter메소드로 호출 //URL,(DB)ID,PW 입력  ->db에 접근
							stmt = con.createStatement(); // 할일을 넘겨줄 Statement 객체 생성, db는 프로그램으로부터 명령을 받을 때 Statement객체로 받는다. 따라서 이 객체는 시킬 명령을 저장하는 객체(아직 일 안시킴)
				            
							// client 테이블에 있는 모든 레코드 검색 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+id+"'"); 
					
							// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
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
							System.err.println("Select 오류: " + ex.getMessage());
						}
						
						pvp.setVisible(true);
						setVisible(false);
					}
					else if(line[0].equals("$instruct:LeaveRoom"))  // pvp 방 나가기 확인전송 받음
					{
						System.out.println("$instruct:LeaveRoom동작");
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
					else if(line[0].equals("$instruct:RoomRemove"))  // pvp 방 없어짐 내용 전송 받음
					{
						for(int i=0;i<roomNameList.size();i++)
						{
							if(roomNameList.get(i).name.equals(line[1]))
							{
								roomNameList.remove(i);
								roomCount--;
								
								roomPanel.removeAll(); // 그리드 레이아웃 패널의 모든 컴포넌트 삭제
								for(int j=0;j<roomCount;j++) // 2*2 그리드 레이아웃에 컴포넌트를 꽉 채워주지 않으면 모양이 예쁘지 못해서 부족한 컴포넌트 수 만큼 라벨을 삽입해준다.
								{
									roomPanel.add(roomNameList.get(j).roomBtn);
								}
								for(int k=0;k<4-roomCount;k++) // 그리드 레이아웃을 다 채워주기 위해 라벨 패널에 추가
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
					else if(line[0].equals("$instruct:Player1ID"))  // 입장한 pvp 방 player1의 아이디를 받아온다.
					{
						Connection con = null;  // DBMS 로그인 정보 입력
						Statement stmt = null;  // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
						String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost 아이피에서 chattingservice 스키마에 접근, 대한민국 시간대(물음표필수)
						String user = "root";   //DB ID
						String passwd = "6272"; //DB PW

						try {
							Class.forName("com.mysql.jdbc.Driver");  // jdbc드라이버 가져옴
						} catch(java.lang.ClassNotFoundException e1) {
							System.err.print("ClassNotFoundException: "); 
							System.err.println("드라이버 로딩 오류: " + e1.getMessage());
							return;
						} 

						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection 객체 getter메소드로 호출 //URL,(DB)ID,PW 입력  ->db에 접근
							stmt = con.createStatement(); // 할일을 넘겨줄 Statement 객체 생성, db는 프로그램으로부터 명령을 받을 때 Statement객체로 받는다. 따라서 이 객체는 시킬 명령을 저장하는 객체(아직 일 안시킴)
				            
							// client 테이블에 있는 모든 레코드 검색 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[1]+"'"); 
					
							// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
							while (result.next()) { 						
								if(pvp==null)
								{
									pvp = new PvPWar(line[2]);
									enterRoomName=line[2];
									pvp.leaveRoomBtn.addActionListener(new MyActionListener());
									pvp.chatTransmitBtn.addActionListener(new MyActionListener());
									pvp.chatTf.addActionListener(new MyEnterActionListener());
									pvp.stateBtn.addActionListener(new MyActionListener());
									pvp.addWindowListener(win2); // 윈도우창과 관련된 액션리스너 add
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
							System.err.println("Select 오류: " + ex.getMessage());
						}
						
						//자신 정보 불러오기
						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection 객체 getter메소드로 호출 //URL,(DB)ID,PW 입력  ->db에 접근
							stmt = con.createStatement(); // 할일을 넘겨줄 Statement 객체 생성, db는 프로그램으로부터 명령을 받을 때 Statement객체로 받는다. 따라서 이 객체는 시킬 명령을 저장하는 객체(아직 일 안시킴)
				            
							// client 테이블에 있는 모든 레코드 검색 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+id+"'"); 
					
							// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
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
							System.err.println("Select 오류: " + ex.getMessage());
						}
					}
					else if(line[0].equals("$instruct:Player2ID"))  // 입장한 pvp 방 player2의 아이디를 받아온다.
					{
						Connection con = null;  // DBMS 로그인 정보 입력
						Statement stmt = null;  // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
						String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost 아이피에서 chattingservice 스키마에 접근, 대한민국 시간대(물음표필수)
						String user = "root";   //DB ID
						String passwd = "6272"; //DB PW

						try {
							Class.forName("com.mysql.jdbc.Driver");  // jdbc드라이버 가져옴
						} catch(java.lang.ClassNotFoundException e1) {
							System.err.print("ClassNotFoundException: "); 
							System.err.println("드라이버 로딩 오류: " + e1.getMessage());
							return;
						} 

						//상대정보 불러오기
						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection 객체 getter메소드로 호출 //URL,(DB)ID,PW 입력  ->db에 접근
							stmt = con.createStatement(); // 할일을 넘겨줄 Statement 객체 생성, db는 프로그램으로부터 명령을 받을 때 Statement객체로 받는다. 따라서 이 객체는 시킬 명령을 저장하는 객체(아직 일 안시킴)
				            
							// client 테이블에 있는 모든 레코드 검색 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[1]+"'"); 
					
							// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
							while (result.next()) { 		
								if(pvp==null)
								{
									pvp = new PvPWar(line[2]);
									enterRoomName=line[2];
									pvp.leaveRoomBtn.addActionListener(new MyActionListener());
									pvp.chatTransmitBtn.addActionListener(new MyActionListener());
									pvp.chatTf.addActionListener(new MyEnterActionListener());
									pvp.stateBtn.addActionListener(new MyActionListener());
									pvp.addWindowListener(win2); // 윈도우창과 관련된 액션리스너 add
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
							System.err.println("Select 오류: " + ex.getMessage());
						}
						
						//자신 정보 불러오기
						try {
							con = DriverManager.getConnection(url, user, passwd);  //Connection 객체 getter메소드로 호출 //URL,(DB)ID,PW 입력  ->db에 접근
							stmt = con.createStatement(); // 할일을 넘겨줄 Statement 객체 생성, db는 프로그램으로부터 명령을 받을 때 Statement객체로 받는다. 따라서 이 객체는 시킬 명령을 저장하는 객체(아직 일 안시킴)
				            
							// client 테이블에 있는 모든 레코드 검색 	
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+id+"'"); 
					
							// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
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
							System.err.println("Select 오류: " + ex.getMessage());
						}
						
						
					}
					else if(line[0].equals("$instruct:FullMember"))  // 접속하려는 방의 인원이 꽉 찼을 때 받는 명령어
					{
						JOptionPane.showMessageDialog(null, "제한인원이 모두 찬 방입니다.", "Message", JOptionPane.ERROR_MESSAGE);
					}
					else if(line[0].equals("$instruct:Player1Leave"))  // 접속한 방의 player1이 나갔을 때
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
					else if(line[0].equals("$instruct:Player2Leave"))  // 접속한 방의 player2가 나갔을 때
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
					else if(line[0].equals("$instruct:Chat"))  // 접속한 방의 player2가 나갔을 때
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
					/*else if(line[0].equals("$instruct:Ready"))  // 준비버튼 활성화
					{
						pvp.stateLa.setVisible(false);
						pvp.stateBtn.setVisible(true);
						pvp.stateBtn.addActionListener(new MyActionListener());
						
						setSize(501, 401);
						setSize(500, 400);
					}*/
					
					//추가
					if(line[0].equals("$instruct:readyPlayer"))
					{//플레이어가 2명이 되면 대기를 실행
						pvp.stateBtn.setVisible(true);
						pvp.leaveRoomLa.setVisible(true);
						pvp.leaveRoomBtn.setVisible(true);
						pvp.stateLa.setVisible(false);
					}
					else if(line[0].equals("$instruct:start")) {
						//플레이어 2명이 ready를 누르면 정보를 저장
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
						//HP정보를 저장
						pvp.player1StaminaLa.setText(P1_HP+"/"+P1_FULLHP);
						pvp.player2StaminaLa.setText(P2_HP+"/"+P2_FULLHP);
					}
					else if(line[0].equals("$instruct:attack")) {
						pvp.stateBtn.setVisible(false);
						pvp.stateLa.setText("         대전 중");
						pvp.stateLa.setVisible(true);
						pvp.leaveRoomBtn.setVisible(false);
						if(line[1].equals(P1_ID)) {
							P1_HP=line[3];
							P1_BeDamaged=line[2];
							try {   // 공격 애니메이션
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
							try {   // 공격 애니메이션
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
						}//공격을 하면 공격당한 정보를 저장
						pvp.player1StaminaLa.setText(P1_HP+"/"+P1_FULLHP);
						pvp.player2StaminaLa.setText(P2_HP+"/"+P2_FULLHP);
						
						double staminaGage1x = Double.parseDouble(P1_HP) /Double.parseDouble(P1_FULLHP);
						double staminaGage2x = Double.parseDouble(P2_HP) /Double.parseDouble(P2_FULLHP);
						
						System.out.println(staminaGage1x + "and" + staminaGage2x );
						System.out.println((int)(130.0*staminaGage1x) + "and" + (int)(130.0*staminaGage2x) );
						
						pvp.player1Stamina.setBounds(70,10,(int)(130.0*staminaGage1x),20);
						pvp.player2Stamina.setBounds(360,10,(int)(130.0*staminaGage2x),20);
						
						//GUI구현
					}
					else if(line[0].equals("$instruct:win")) {
						//캐릭터가 승리하면 승리를 출력
						if(line[1].equals(P2_ID)) {
							System.out.println("플레이어 2 승리");
							pvp.stateBtn.setVisible(true);
							pvp.leaveRoomLa.setVisible(false);
							pvp.leaveRoomBtn.setVisible(true);
							pvp.stateLa.setVisible(false);
							pvp.stateBtn.setText("준비완료");
							pvp.stateLa.setText("         대전 중");
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
							System.out.println("플레이어 1 승리");
							pvp.stateBtn.setVisible(true);
							pvp.leaveRoomLa.setVisible(false);
							pvp.leaveRoomBtn.setVisible(true);
							pvp.stateLa.setVisible(false);
							pvp.stateBtn.setText("준비완료");
							pvp.stateLa.setText("         대전 중");
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
	
	class MyWindowActionListener extends WindowAdapter{ // 윈도우 창 액션과 관련된 액션 리스너(X버튼 눌러 창 닫을 때 액션받기위해 사용)
		@Override
		public void windowClosing(WindowEvent e)
		{
			int safe = JOptionPane.showConfirmDialog(null, "프로그램을 종료하시겠습니까?",  "프로그램 질문", JOptionPane.YES_NO_OPTION);

			if(safe == JOptionPane.YES_OPTION){  // yes 프레임 종료됨
				try {
					outStream = mySocket.getOutputStream();
					dataOutStream= new DataOutputStream(outStream);
					dataOutStream.writeUTF("$instruct:Logout"+"/"+id);
					System.exit(0);		
				}catch(Exception e2)
				{
					System.out.println(e2.toString());
				}
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // 프레임의 X 클릭으로 창을 닫았을 때 프로그램을 종료 시키는 메소드

			} else if (safe == JOptionPane.NO_OPTION) {
			    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//cancel 프레임 종료안됨
			    setVisible(true);
			}
		}
	}
	
	class MyWindowActionListener2 extends WindowAdapter{ // 윈도우 창 액션과 관련된 액션 리스너(X버튼 눌러 창 닫을 때 액션받기위해 사용)
		@Override
		public void windowClosing(WindowEvent e)
		{
			System.out.println("1작동");
			int safe = JOptionPane.showConfirmDialog(null, "PvP대전 중 탈주 시 불이익이 발생할 수 있습니다. 정말 탈주하시겠습니까?",  "프로그램 질문", JOptionPane.YES_NO_OPTION);

			if(safe == JOptionPane.YES_OPTION){  // yes 프레임 종료됨
				System.out.println("2작동");
				try {
					outStream = mySocket.getOutputStream();
					dataOutStream= new DataOutputStream(outStream);
					dataOutStream.writeUTF("$instruct:Logout"+"/"+id);
					System.exit(0);		
				}catch(Exception e2)
				{
					System.out.println(e2.toString());
				}
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // 프레임의 X 클릭으로 창을 닫았을 때 프로그램을 종료 시키는 메소드

			} else if (safe == JOptionPane.NO_OPTION) {
				System.out.println("3작동");
			    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//cancel 프레임 종료안됨
			}
		}
	}
	
}
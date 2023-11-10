package Damagochi_Server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Server {
	ServerSocket ss=null;
	
	static ArrayList<ConnectionToClient> clients = new ArrayList<>();
	static ArrayList<Room> room = new ArrayList<>();
	
	static int roomCount=0;
	
	public static void main(String[] args) {
		Server server=new Server();
		try {
			server.ss = new ServerSocket(12345);
			System.out.println("Server> Server Socket is created");
			while (true) 
			{
				Socket socket =server.ss.accept();
				ConnectionToClient c = new ConnectionToClient(socket);
				server.clients.add(c);
				c.start();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
  
	static class Room{
		int playerCount=0;
		String roomName;
		
		Socket player1;
		String player1Id;
		double player1Attack;
		double player1Defense;
		double player1Stamina;
		double player1Speed;
		
		Socket player2;
		String player2Id;
		double player2Attack;
		double player2Defense;
		double player2Stamina;
		double player2Speed;
		
		boolean player1Ready=false;
		boolean player2Ready=false;
	}

	static class ConnectionToClient extends Thread 
	{
		Socket socket;
		String characterName;
		
		
		OutputStream outStream;
		DataOutputStream dataOutStream;
		
		InputStream inStream;
		DataInputStream dataInStream;
		
		String clientId;
		PlayBettle play=null;
		
	    ConnectionToClient(Socket socket) 
	    {
	    	this.socket = socket;
	    }
	      
	    public void run() {
	    	System.out.println("Server > " + this.socket.toString() + "is connected.");
	        try {
	        	inStream = this.socket.getInputStream();
				dataInStream = new DataInputStream(inStream);
				
				outStream = this.socket.getOutputStream();
				dataOutStream = new DataOutputStream(outStream);
				
				this.clientId = dataInStream.readUTF(); // Ŭ���̾�Ʈ���� ������ ���ڿ��� ��ٷȴٰ� �޾Ƽ� msg�� ����
				System.out.println(clientId+"����");
				dataOutStream.writeUTF("Welcome");
				
				for(int i=0;i<room.size();i++) // ���ο� Ŭ���̾�Ʈ ���� ��, ������ �����ϴ� �� ���� �ش� Ŭ���̾�Ʈ���� ����
				{
					dataOutStream.writeUTF("$instruct:ExistRoom"+"/"+room.get(i).roomName);
				}
				
				dataOutStream.writeUTF("$instruct:EndExistRoom");
				
				while(true) {
					String msg = dataInStream.readUTF(); // Ŭ���̾�Ʈ���� ������ ���ڿ��� ��ٷȴٰ� �޾Ƽ� msg�� ����
					System.out.println("Server> " + this.socket.toString() + " : " + msg);
					String line[]=msg.split("\\/"); //  '/'�� ���ڿ��� �������´�.
					System.out.println(line[0]);
					String chatMessage="";
					System.out.println("line.length = " +line.length);
					for(int a=3;a<line.length;a++)
					{
						chatMessage=chatMessage+line[a];
					}
					System.out.println("chatMessage = " +chatMessage);
					if(line[0].equals("$instruct:NewRoom"))
					{
						room.add(new Room());
						room.get(roomCount).player1=this.socket;
						room.get(roomCount).player1Id=line[2];
						room.get(roomCount).playerCount++;
						room.get(roomCount).roomName=line[1];
						
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
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[2]+"'"); 
					
							// result ��ü�� ����� ���� ����κ��� ���� ���� ����
							while (result.next()) { 								
								room.get(roomCount).player1Attack=result.getDouble("power");
								room.get(roomCount).player1Defense=result.getDouble("defense");
								room.get(roomCount).player1Speed=result.getDouble("speed");
								room.get(roomCount).player1Stamina=result.getDouble("stamina");
							}
							roomCount++;
							stmt.close();
							con.close();	

						} catch(SQLException ex) {
							System.err.println("Select ����: " + ex.getMessage());
						}
						
						for(int i=0;i<clients.size();i++)
						{
							System.out.println(clients.size());
							outStream = clients.get(i).socket.getOutputStream();
							dataOutStream = new DataOutputStream(outStream);
							dataOutStream.writeUTF("$instruct:NewRoom"+"/"+line[1]);
						}
						
						//���� ������ Ŭ���̾�Ʈ���Ը� ������ ��ɾ�� �ڽ��� ������ ������ �����ϵ��� �������� ����
						outStream = socket.getOutputStream();
						dataOutStream = new DataOutputStream(outStream);
						dataOutStream.writeUTF("$instruct:NewRoomCreator");
					}
					else if(line[0].equals("$instruct:LeaveRoom")) // pvp������ ������ ��ư ��û �޾��� ��
					{
						for(int i=0;i<room.size();i++)
						{
							if(line[1].equals(room.get(i).roomName))  //�ش� ���� ���� �ο��� 0���� �Ǹ� �� �����ϰ� �� Ŭ���̾�Ʈ���� ���� ����
							{
								room.get(i).playerCount--;
								room.get(i).player1Ready=false;
								room.get(i).player2Ready=false;
								if(room.get(i).playerCount==0) // Ŭ���̾�Ʈ�� ������ �濡 �����ִ� �ο��� 0���� �� ���
								{
									for(int j=0;j<clients.size();j++)
									{
										outStream = clients.get(j).socket.getOutputStream();
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:RoomRemove"+"/"+room.get(i).roomName);
									}
									room.remove(i); // �� ����
									roomCount--;
								}
								else // Ŭ���̾�Ʈ�� ������ �濡 �����ִ� �ο��� 1���� �� ���
								{
									System.out.println("1step");
									System.out.println(line[2] + "and" + room.get(i).player1Id);
									if(line[2].equals(room.get(i).player1Id))
									{
										System.out.println("2step");
										outStream = room.get(i).player2.getOutputStream();
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:Player1Leave");
										room.get(i).player1=null;
									}
									else if(line[2].equals(room.get(i).player2Id))
									{
										System.out.println("3step");
										outStream = room.get(i).player1.getOutputStream();
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:Player2Leave");
										room.get(i).player2=null;
									}
								}
							}
						}
						outStream = socket.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
						dataOutStream = new DataOutputStream(outStream);
						dataOutStream.writeUTF("$instruct:LeaveRoom");
					}
					else if(line[0].equals("$instruct:Logout")) // ����ڰ� pvp�� �������
					{
						for(int i=0;i<clients.size();i++)
						{
							if(clients.get(i).clientId.equals(line[1]))
							{
								System.out.println(clients.get(i).clientId+"������ �������� �������ϴ�.");
								clients.remove(i);
								return;
							}
						}
					}
					else if(line[0].equals("$instruct:EnterRoom")) // ����ڰ� ������ ������ �濡 �����
					{
						int roomNum=0;
						for(int i=0;i<room.size();i++)
						{
							if(line[1].equals(room.get(i).roomName))
							{
								roomNum=i;
								if(room.get(i).playerCount==2) // �� �����ο��� ��á�� ��
								{
									outStream = socket.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
									dataOutStream = new DataOutputStream(outStream);
									dataOutStream.writeUTF("$instruct:FullMember");
								}
								else // �濡 �ο��� ��� �����ʾ� ���� ���� ������ ��
								{
									if(room.get(i).player1==null)  // player1 �ڸ��� ������� ��
									{
										room.get(i).player1=this.socket;
										room.get(i).player1Id=line[2];
										room.get(i).playerCount++;
										
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
											ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[2]+"'"); 
									
											// result ��ü�� ����� ���� ����κ��� ���� ���� ����
											while (result.next()) { 								
												room.get(i).player1Attack=result.getDouble("power");
												room.get(i).player1Defense=result.getDouble("defense");
												room.get(i).player1Speed=result.getDouble("speed");
												room.get(i).player1Stamina=result.getDouble("stamina");
											}
											stmt.close();
											con.close();	
	
										} catch(SQLException ex) {
											System.err.println("Select ����: " + ex.getMessage());
										}
									}
									else  // player2 �ڸ��� ������� ��
									{
										room.get(i).player2=this.socket;
										room.get(i).player2Id=line[2];
										room.get(i).playerCount++;
										
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
											ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[2]+"'"); 
									
											// result ��ü�� ����� ���� ����κ��� ���� ���� ����
											while (result.next()) { 								
												room.get(i).player2Attack=result.getDouble("power");
												room.get(i).player2Defense=result.getDouble("defense");
												room.get(i).player2Speed=result.getDouble("speed");
												room.get(i).player2Stamina=result.getDouble("stamina");
											}
											stmt.close();
											con.close();	
	
										} catch(SQLException ex) {
											System.err.println("Select ����: " + ex.getMessage());
										}
									}
									room.get(i).player1Ready=false;
									room.get(i).player2Ready=false;
									
									outStream = room.get(i).player1.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
									dataOutStream = new DataOutputStream(outStream);
									dataOutStream.writeUTF("$instruct:Player2ID/"+room.get(i).player2Id+"/"+room.get(i).roomName);
									//dataOutStream.writeUTF("$instruct:Ready");
									
									outStream = room.get(i).player2.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
									dataOutStream = new DataOutputStream(outStream);
									dataOutStream.writeUTF("$instruct:Player1ID/"+room.get(i).player1Id+"/"+room.get(i).roomName);
									//dataOutStream.writeUTF("$instruct:Ready");
									
									outStream = room.get(roomNum).player1.getOutputStream();
									dataOutStream = new DataOutputStream(outStream);
									dataOutStream.writeUTF("$instruct:readyPlayer");
									
									outStream = room.get(roomNum).player2.getOutputStream();
									dataOutStream = new DataOutputStream(outStream);
									dataOutStream.writeUTF("$instruct:readyPlayer");
									break;
								}
							}
						}
					}
					else if(line[0].equals("$instruct:Chat")) // ä�� �ý���
					{
						for(int i=0;i<room.size();i++)
						{
							if(room.get(i).roomName.equals(line[2]))
							{
								if(room.get(i).player1!=null)
								{
									if(room.get(i).player1Id.equals(line[1]))
									{
										outStream = room.get(i).player1.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										if(room.get(i).player2!=null)
										{
											outStream = room.get(i).player2.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
											dataOutStream = new DataOutputStream(outStream);
											dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										}
									}
								}
								
								if(room.get(i).player2!=null)
								{
									if(room.get(i).player2Id.equals(line[1]))
									{
										outStream = room.get(i).player2.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										if(room.get(i).player1!=null)
										{
											outStream = room.get(i).player1.getOutputStream(); //��û�� �ش� Ŭ���̾�Ʈ���Ը� ����
											dataOutStream = new DataOutputStream(outStream);
											dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										}
									}
								}
							}
						}
					}
					//�߰�
					
					else if(line[0].equals("$instruct:ready")) 
					{//�÷��̾ �غ�ϷḦ ������ �� ����
						for(int i=0;i<roomCount;i++) {
							if(room.get(i).player1.equals(this.socket)) {
								room.get(i).player1Ready=true;
								System.out.println("P1OK");
							}
							else if(room.get(i).player2.equals(this.socket)) {
								room.get(i).player2Ready=true;
								System.out.println("P2OK");
							}
						}
						
						for(int i=0;i<roomCount;i++) {
							if(room.get(i).player1Ready&&room.get(i).player2Ready) {
								//�÷��̾�1�� �÷��̾�2�� ���ÿ� �غ�ϷḦ ������ ����
								outStream = room.get(i).player1.getOutputStream();
								dataOutStream = new DataOutputStream(outStream);
								dataOutStream.writeUTF("$instruct:start/"+room.get(i).player1Id+"/"+room.get(i).player2Id);
								
								outStream = room.get(i).player2.getOutputStream();
								dataOutStream = new DataOutputStream(outStream);
								dataOutStream.writeUTF("$instruct:start/"+room.get(i).player1Id+"/"+room.get(i).player2Id);
								//PvpWar��ü�� �÷��̾���� ���̵� ����
								play = new PlayBettle(room.get(i).player1,room.get(i).player2,room.get(i).player1Id,room.get(i).player2Id,i);
								play.start();
								//���������� �ο�� ������ ����
							}
						}
					}
					else if(line[0].equals("$instruct:unready"))
					{//�÷��̾ �غ�ϷḦ ����ϸ� ����
						for(int i=0;i<roomCount;i++) {
							if(room.get(i).player1.equals(this.socket)) {
								room.get(i).player1Ready=false;
								System.out.println("P1 NO OK");
							}
							else if(room.get(i).player2.equals(this.socket)) {
								room.get(i).player2Ready=false;
								System.out.println("P2 NO OK");
							}
						}
					}
					
				}
	        }catch (Exception e) {
	        	e.printStackTrace();
	        }
	    } // run �޼ҵ�
	} //Ŭ���̾�Ʈ ������ Ŭ����
} // Server Ŭ����

class PlayBettle extends Thread{
	Socket p1;
	Socket p2;
	OutputStream P1outStream;
	OutputStream P2outStream;
	
	DataOutputStream P1dataOutStream;
	DataOutputStream P2dataOutStream;
	
	InputStream P1inStream;
	InputStream P2inStream;
	
	DataInputStream P1dataInStream;
	DataInputStream P2dataInStream;
	
	String P1Id;
	String P2Id;
	
	int roomNum;
	
	PlayBettle(Socket _p1,Socket _p2,String _pI1,String _pI2,int roomNum){
		this.p1=_p1;
		this.p2=_p2;
		this.P1Id=_pI1;
		this.P2Id=_pI2;
		this.roomNum=roomNum;
	}
	@Override
	public void run() {
		double [] power=new double[2];
		double [] defense=new double[2];
		double[] speed=new double[2];
		double [] stamina=new double[2];
		String []Player=new String[2];
		String [] characaterNum = new String[2];
		try {
			P1inStream = p1.getInputStream();
			P1dataInStream = new DataInputStream(P1inStream);
			
			P2inStream = p2.getInputStream();
			P2dataInStream = new DataInputStream(P2inStream);
		
			P1outStream = p1.getOutputStream();
			P1dataOutStream = new DataOutputStream(P1outStream);

			P2outStream = p2.getOutputStream();
			P2dataOutStream = new DataOutputStream(P2outStream);
			
			BringData BD=new BringData(P1Id,P2Id);
			String []dummy=BD.result.split("/");
			//DB���� ���� �÷��̾���� ĳ������ ������ �ҷ���

			P1dataOutStream.writeUTF("$instruct:setHP/"+dummy[3]+"/"+dummy[7]);
			P2dataOutStream.writeUTF("$instruct:setHP/"+dummy[3]+"/"+dummy[7]);
			//ĳ���Ͱ� ó�� ������ �ִ� HP�� ����
			if(Double.parseDouble(dummy[2])>=Double.parseDouble(dummy[6])) {
				power[0]=Double.parseDouble(dummy[0]);
				defense[0]=Double.parseDouble(dummy[1]);
				speed[0]=Double.parseDouble(dummy[2]);
				stamina[0]=Double.parseDouble(dummy[3]);
				Player[0]=P1Id;
				characaterNum[0]=dummy[8];
				power[1]=Double.parseDouble(dummy[4]);
				defense[1]=Double.parseDouble(dummy[5]);
				speed[1]=Double.parseDouble(dummy[6]);
				stamina[1]=Double.parseDouble(dummy[7]);
				characaterNum[1]=dummy[9];
				Player[1]=P2Id;
			}//��ø���� ���� ĳ���͸� �켱����ĳ������ ������ ������
			
			else if(Double.parseDouble(dummy[2])<Double.parseDouble(dummy[6])) {
				power[0]=Double.parseDouble(dummy[4]);
				defense[0]=Double.parseDouble(dummy[5]);
				speed[0]=Double.parseDouble(dummy[6]);
				stamina[0]=Double.parseDouble(dummy[7]);
				characaterNum[0]=dummy[9];
				Player[0]=P2Id;
				power[1]=Double.parseDouble(dummy[0]);
				defense[1]=Double.parseDouble(dummy[1]);
				speed[1]=Double.parseDouble(dummy[2]);
				stamina[1]=Double.parseDouble(dummy[3]);
				characaterNum[1]=dummy[8];
				Player[1]=P1Id;
			}//��ø���� ���� ĳ���͸� �켱����ĳ������ ������ ������
			
			while(true) {
				double damage=Math.round(power[0]-Math.round(power[0]*defense[1]*0.03));
				//�������� ���
				if(stamina[0]<=0) {
					stamina[0]=0;
					break;
					//ĳ���͵��� HP�� �ϳ��� 0�� �Ǿ��� �� ����
				}
				stamina[1]=stamina[1]-damage;
				P1dataOutStream.writeUTF("$instruct:attack/"+Player[1]+"/"+damage+"/"+stamina[1]+"/"+characaterNum[1]+"/"+characaterNum[0]);
				P2dataOutStream.writeUTF("$instruct:attack/"+Player[1]+"/"+damage+"/"+stamina[1]+"/"+characaterNum[1]+"/"+characaterNum[0]);
				sleep(2000);
				//������ ������ Ŭ���̾�Ʈ�� ����
				if(stamina[1]<=0) {
					stamina[1]=0;
					break;
				} //ĳ���͵��� HP�� �ϳ��� 0�� �Ǿ��� �� ����
				damage=Math.round(power[1]-Math.round(power[1]*defense[0]*0.03));
				//�������� ���
				stamina[0]=stamina[0]-damage;
				P1dataOutStream.writeUTF("$instruct:attack/"+Player[0]+"/"+damage+"/"+stamina[0]+"/"+characaterNum[0]+"/"+characaterNum[1]);
				P2dataOutStream.writeUTF("$instruct:attack/"+Player[0]+"/"+damage+"/"+stamina[0]+"/"+characaterNum[0]+"/"+characaterNum[1]);
				sleep(2000);
				//������ ������ Ŭ���̾�Ʈ�� ����
			}
			if(stamina[0]<stamina[1]) {
				P1dataOutStream.writeUTF("$instruct:win/"+Player[1]);
				P2dataOutStream.writeUTF("$instruct:win/"+Player[1]);
			}//�¸��� ������ ����
			else if(stamina[0]>stamina[1]) {
				P1dataOutStream.writeUTF("$instruct:win/"+Player[0]);
				P2dataOutStream.writeUTF("$instruct:win/"+Player[0]);	
				Server.room.get(roomNum).player1Ready=false;
				Server.room.get(roomNum).player2Ready=false;
			}//�¸��� ������ ����
		}catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
}
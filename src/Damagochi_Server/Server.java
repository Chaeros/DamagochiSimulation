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
				
				this.clientId = dataInStream.readUTF(); // 클라이언트에서 보내는 문자열을 기다렸다가 받아서 msg에 저장
				System.out.println(clientId+"받음");
				dataOutStream.writeUTF("Welcome");
				
				for(int i=0;i<room.size();i++) // 새로운 클라이언트 접속 시, 기존에 존재하던 방 정보 해당 클라이언트에게 전송
				{
					dataOutStream.writeUTF("$instruct:ExistRoom"+"/"+room.get(i).roomName);
				}
				
				dataOutStream.writeUTF("$instruct:EndExistRoom");
				
				while(true) {
					String msg = dataInStream.readUTF(); // 클라이언트에서 보내는 문자열을 기다렸다가 받아서 msg에 저장
					System.out.println("Server> " + this.socket.toString() + " : " + msg);
					String line[]=msg.split("\\/"); //  '/'로 문자열을 구분짓는다.
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
							ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[2]+"'"); 
					
							// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
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
							System.err.println("Select 오류: " + ex.getMessage());
						}
						
						for(int i=0;i<clients.size();i++)
						{
							System.out.println(clients.size());
							outStream = clients.get(i).socket.getOutputStream();
							dataOutStream = new DataOutputStream(outStream);
							dataOutStream.writeUTF("$instruct:NewRoom"+"/"+line[1]);
						}
						
						//방을 생성한 클라이언트에게만 보내는 명령어로 자신이 생성한 방으로 입장하도록 돕기위해 보냄
						outStream = socket.getOutputStream();
						dataOutStream = new DataOutputStream(outStream);
						dataOutStream.writeUTF("$instruct:NewRoomCreator");
					}
					else if(line[0].equals("$instruct:LeaveRoom")) // pvp대전방 나가기 버튼 요청 받았을 때
					{
						for(int i=0;i<room.size();i++)
						{
							if(line[1].equals(room.get(i).roomName))  //해당 방의 접속 인원이 0명이 되면 방 제거하고 각 클라이언트에게 내용 보냄
							{
								room.get(i).playerCount--;
								room.get(i).player1Ready=false;
								room.get(i).player2Ready=false;
								if(room.get(i).playerCount==0) // 클라이언트가 나가고 방에 남아있는 인원이 0명이 된 경우
								{
									for(int j=0;j<clients.size();j++)
									{
										outStream = clients.get(j).socket.getOutputStream();
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:RoomRemove"+"/"+room.get(i).roomName);
									}
									room.remove(i); // 방 삭제
									roomCount--;
								}
								else // 클라이언트가 나가고 방에 남아있는 인원이 1명이 된 경우
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
						outStream = socket.getOutputStream(); //요청한 해당 클라이언트에게만 전송
						dataOutStream = new DataOutputStream(outStream);
						dataOutStream.writeUTF("$instruct:LeaveRoom");
					}
					else if(line[0].equals("$instruct:Logout")) // 사용자가 pvp를 끝낸경우
					{
						for(int i=0;i<clients.size();i++)
						{
							if(clients.get(i).clientId.equals(line[1]))
							{
								System.out.println(clients.get(i).clientId+"유저가 서버에서 나갔습니다.");
								clients.remove(i);
								return;
							}
						}
					}
					else if(line[0].equals("$instruct:EnterRoom")) // 사용자가 기존에 생성된 방에 입장시
					{
						int roomNum=0;
						for(int i=0;i<room.size();i++)
						{
							if(line[1].equals(room.get(i).roomName))
							{
								roomNum=i;
								if(room.get(i).playerCount==2) // 방 접속인원이 꽉찼을 때
								{
									outStream = socket.getOutputStream(); //요청한 해당 클라이언트에게만 전송
									dataOutStream = new DataOutputStream(outStream);
									dataOutStream.writeUTF("$instruct:FullMember");
								}
								else // 방에 인원이 모두 차지않아 아직 접속 가능할 때
								{
									if(room.get(i).player1==null)  // player1 자리가 비어있을 때
									{
										room.get(i).player1=this.socket;
										room.get(i).player1Id=line[2];
										room.get(i).playerCount++;
										
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
											ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[2]+"'"); 
									
											// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
											while (result.next()) { 								
												room.get(i).player1Attack=result.getDouble("power");
												room.get(i).player1Defense=result.getDouble("defense");
												room.get(i).player1Speed=result.getDouble("speed");
												room.get(i).player1Stamina=result.getDouble("stamina");
											}
											stmt.close();
											con.close();	
	
										} catch(SQLException ex) {
											System.err.println("Select 오류: " + ex.getMessage());
										}
									}
									else  // player2 자리가 비어있을 때
									{
										room.get(i).player2=this.socket;
										room.get(i).player2Id=line[2];
										room.get(i).playerCount++;
										
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
											ResultSet result = stmt.executeQuery("SELECT * FROM Characters WHERE id ='"+line[2]+"'"); 
									
											// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
											while (result.next()) { 								
												room.get(i).player2Attack=result.getDouble("power");
												room.get(i).player2Defense=result.getDouble("defense");
												room.get(i).player2Speed=result.getDouble("speed");
												room.get(i).player2Stamina=result.getDouble("stamina");
											}
											stmt.close();
											con.close();	
	
										} catch(SQLException ex) {
											System.err.println("Select 오류: " + ex.getMessage());
										}
									}
									room.get(i).player1Ready=false;
									room.get(i).player2Ready=false;
									
									outStream = room.get(i).player1.getOutputStream(); //요청한 해당 클라이언트에게만 전송
									dataOutStream = new DataOutputStream(outStream);
									dataOutStream.writeUTF("$instruct:Player2ID/"+room.get(i).player2Id+"/"+room.get(i).roomName);
									//dataOutStream.writeUTF("$instruct:Ready");
									
									outStream = room.get(i).player2.getOutputStream(); //요청한 해당 클라이언트에게만 전송
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
					else if(line[0].equals("$instruct:Chat")) // 채팅 시스템
					{
						for(int i=0;i<room.size();i++)
						{
							if(room.get(i).roomName.equals(line[2]))
							{
								if(room.get(i).player1!=null)
								{
									if(room.get(i).player1Id.equals(line[1]))
									{
										outStream = room.get(i).player1.getOutputStream(); //요청한 해당 클라이언트에게만 전송
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										if(room.get(i).player2!=null)
										{
											outStream = room.get(i).player2.getOutputStream(); //요청한 해당 클라이언트에게만 전송
											dataOutStream = new DataOutputStream(outStream);
											dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										}
									}
								}
								
								if(room.get(i).player2!=null)
								{
									if(room.get(i).player2Id.equals(line[1]))
									{
										outStream = room.get(i).player2.getOutputStream(); //요청한 해당 클라이언트에게만 전송
										dataOutStream = new DataOutputStream(outStream);
										dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										if(room.get(i).player1!=null)
										{
											outStream = room.get(i).player1.getOutputStream(); //요청한 해당 클라이언트에게만 전송
											dataOutStream = new DataOutputStream(outStream);
											dataOutStream.writeUTF("$instruct:Chat/"+clientId+": "+chatMessage);
										}
									}
								}
							}
						}
					}
					//추가
					
					else if(line[0].equals("$instruct:ready")) 
					{//플레이어가 준비완료를 눌렀을 때 실행
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
								//플레이어1과 플레이어2가 동시에 준비완료를 누르면 실행
								outStream = room.get(i).player1.getOutputStream();
								dataOutStream = new DataOutputStream(outStream);
								dataOutStream.writeUTF("$instruct:start/"+room.get(i).player1Id+"/"+room.get(i).player2Id);
								
								outStream = room.get(i).player2.getOutputStream();
								dataOutStream = new DataOutputStream(outStream);
								dataOutStream.writeUTF("$instruct:start/"+room.get(i).player1Id+"/"+room.get(i).player2Id);
								//PvpWar객체에 플레이어들의 아이디를 전송
								play = new PlayBettle(room.get(i).player1,room.get(i).player2,room.get(i).player1Id,room.get(i).player2Id,i);
								play.start();
								//직접적으로 싸우는 쓰레드 실행
							}
						}
					}
					else if(line[0].equals("$instruct:unready"))
					{//플레이어가 준비완료를 취소하면 실행
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
	    } // run 메소드
	} //클라이언트 스레드 클래스
} // Server 클래스

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
			//DB에서 부터 플레이어들의 캐릭터의 정보를 불러옴

			P1dataOutStream.writeUTF("$instruct:setHP/"+dummy[3]+"/"+dummy[7]);
			P2dataOutStream.writeUTF("$instruct:setHP/"+dummy[3]+"/"+dummy[7]);
			//캐릭터가 처음 가지고 있는 HP를 전송
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
			}//민첩도가 높은 캐릭터를 우선으로캐릭터의 스텟을 저장함
			
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
			}//민첩도가 높은 캐릭터를 우선으로캐릭터의 스텟을 저장함
			
			while(true) {
				double damage=Math.round(power[0]-Math.round(power[0]*defense[1]*0.03));
				//데미지를 계산
				if(stamina[0]<=0) {
					stamina[0]=0;
					break;
					//캐릭터들의 HP중 하나가 0이 되었을 때 종료
				}
				stamina[1]=stamina[1]-damage;
				P1dataOutStream.writeUTF("$instruct:attack/"+Player[1]+"/"+damage+"/"+stamina[1]+"/"+characaterNum[1]+"/"+characaterNum[0]);
				P2dataOutStream.writeUTF("$instruct:attack/"+Player[1]+"/"+damage+"/"+stamina[1]+"/"+characaterNum[1]+"/"+characaterNum[0]);
				sleep(2000);
				//공격한 정보를 클라이언트에 전송
				if(stamina[1]<=0) {
					stamina[1]=0;
					break;
				} //캐릭터들의 HP중 하나가 0이 되었을 때 종료
				damage=Math.round(power[1]-Math.round(power[1]*defense[0]*0.03));
				//데미지를 계산
				stamina[0]=stamina[0]-damage;
				P1dataOutStream.writeUTF("$instruct:attack/"+Player[0]+"/"+damage+"/"+stamina[0]+"/"+characaterNum[0]+"/"+characaterNum[1]);
				P2dataOutStream.writeUTF("$instruct:attack/"+Player[0]+"/"+damage+"/"+stamina[0]+"/"+characaterNum[0]+"/"+characaterNum[1]);
				sleep(2000);
				//공격한 정보를 클라이언트에 전송
			}
			if(stamina[0]<stamina[1]) {
				P1dataOutStream.writeUTF("$instruct:win/"+Player[1]);
				P2dataOutStream.writeUTF("$instruct:win/"+Player[1]);
			}//승리한 정보를 전송
			else if(stamina[0]>stamina[1]) {
				P1dataOutStream.writeUTF("$instruct:win/"+Player[0]);
				P2dataOutStream.writeUTF("$instruct:win/"+Player[0]);	
				Server.room.get(roomNum).player1Ready=false;
				Server.room.get(roomNum).player2Ready=false;
			}//승리한 정보를 전송
		}catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
}
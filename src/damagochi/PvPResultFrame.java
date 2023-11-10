package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;

import Damagochi_Server.*;
import damagochi.Button;

public class PvPResultFrame extends JFrame{
	JLabel explainLa = new JLabel();
	ImageIcon image;
	JLabel imageLa = new JLabel();
	JLabel rewardLa = new JLabel();
	String id;  // 사용자 id
	String result; //승패여부
	
	String characterNum;
	PlayImage play;
	
	PvPResultFrame(String _id,String _result)
	{
		this.id=_id;
		this.result=_result;
		setLayout(new BorderLayout());
		add(explainLa, BorderLayout.NORTH);
		add(imageLa, BorderLayout.CENTER);
		add(rewardLa, BorderLayout.SOUTH);
		
		image=null;
		imageLa.setIcon(image);
		
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
			ResultSet result = stmt.executeQuery("SELECT * FROM characters WHERE id='"+id+"'");
			// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
			while (result.next()) { 
				characterNum=result.getString("characterNum");
			}
			stmt.close();
			con.close();	

		} catch(SQLException ex) {
			System.err.println("Select 오류: " + ex.getMessage());
		}
		
		setSize(270,350); 
		setVisible(true); 
		
		if(_result.equals("win"))
		{
			explainLa.setText("\n\n"+"                                <승리!!!>");
			rewardLa.setText("                      보상내용: 경험치+30");
		}
		else
		{
			explainLa.setText("\n\n"+"                                <패배...>");
			rewardLa.setText("                      보상내용: 경험치+12");
		}
		
		play = new PlayImage();
		play.start();
		
	}
	
	class PlayImage extends Thread{
		
		@Override
		public void run() {
			
				while(true) 
				{
					if(result.equals("win"))
					{
						image=new ImageIcon("D:\\DamaghchiSimulationImages\\"+characterNum+"\\normal1.PNG");
						imageLa.setIcon(image);
						try {
							Thread.sleep(500);
							image=new ImageIcon("D:\\DamaghchiSimulationImages\\"+characterNum+"\\win.PNG");
							imageLa.setIcon(image);
							Thread.sleep(500);
						}
						catch(Exception e)
						{
							e.getStackTrace();
						}
					}
					else
					{
						image=new ImageIcon("D:\\DamaghchiSimulationImages\\"+characterNum+"\\normal1.PNG");
						imageLa.setIcon(image);
						try {
							Thread.sleep(500);
							image=new ImageIcon("D:\\DamaghchiSimulationImages\\"+characterNum+"\\defeat.PNG");
							imageLa.setIcon(image);
							Thread.sleep(500);
						}
						catch(Exception e)
						{
							e.getStackTrace();
						}
					}
				}
					
		}
		
	}

}

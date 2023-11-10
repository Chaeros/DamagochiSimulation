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
	String id;  // ����� id
	String result; //���п���
	
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
			ResultSet result = stmt.executeQuery("SELECT * FROM characters WHERE id='"+id+"'");
			// result ��ü�� ����� ���� ����κ��� ���� ���� ����
			while (result.next()) { 
				characterNum=result.getString("characterNum");
			}
			stmt.close();
			con.close();	

		} catch(SQLException ex) {
			System.err.println("Select ����: " + ex.getMessage());
		}
		
		setSize(270,350); 
		setVisible(true); 
		
		if(_result.equals("win"))
		{
			explainLa.setText("\n\n"+"                                <�¸�!!!>");
			rewardLa.setText("                      ���󳻿�: ����ġ+30");
		}
		else
		{
			explainLa.setText("\n\n"+"                                <�й�...>");
			rewardLa.setText("                      ���󳻿�: ����ġ+12");
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

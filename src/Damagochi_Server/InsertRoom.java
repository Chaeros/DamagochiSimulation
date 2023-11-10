package Damagochi_Server;

import java.sql.*;


public class InsertRoom {
	String result;
	InsertRoom(String input){
		String URL = "jdbc:mysql://localhost/tamagotchi?serverTimezone=Asia/Seoul";
		Connection con = null;
		Statement stmt = null;
		SelectRoom r=new SelectRoom();
		String []dummyfile=r.result.split("/");
		if((dummyfile.length/3)>=4) {
			result="full";
			return;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		try {
			con = DriverManager.getConnection(URL, "root", "1q2w3e");
			String [] dummy=input.split("/");
			String sql="insert into room values ('"+dummy[1]+"','"+dummy[2]+"','"+dummy[3]+"');";
			//货肺款 措扁角 积己
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
}

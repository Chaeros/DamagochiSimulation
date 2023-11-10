package Damagochi_Server;

import java.sql.*;


public class SelectRoom {
	String result;
	SelectRoom(){
		String URL = "jdbc:mysql://localhost/tamagotchi?serverTimezone=Asia/Seoul";
		Connection con = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
		try {
			con = DriverManager.getConnection(URL, "root", "1q2w3e");
			String sql="SELECT * FROM room;";
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			result="";
			while (rs.next()) { 
				result+="Room/";
				result += rs.getString("RName") + "/";
				result += rs.getString("RPass") + "/";
				result +=  rs.getString("RID") + "/";
			}
			//대기실을 검색함
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
}

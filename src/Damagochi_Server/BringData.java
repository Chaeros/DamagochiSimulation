package Damagochi_Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BringData {
	String result;
	String P1;
	String P2;
	BringData(String _P1,String _P2){
		this.P1=_P1;
		this.P2=_P2;
		
		String URL="jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";
		Connection con =null;
		Statement stmt =null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
		try {
			con=DriverManager.getConnection(URL, "root", "6272");
			
			String sql="SELECT P1.power, P1.defense, P1.speed, P1.stamina,P2.power, P2.defense, P2.speed, P2.stamina, P1.characterNum, P2.characterNum  FROM Characters P1, Characters P2 WhERE P1.Id='"+P1+"'and P2.Id='"+P2+"';";
			stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			result="";
			while(rs.next()) {
				result+=rs.getString("P1.power")+"/";
				result+=rs.getString("P1.defense")+"/";
				result+=rs.getString("P1.speed")+"/";
				result+=rs.getString("P1.stamina")+"/";
				result+=rs.getString("P2.power")+"/";
				result+=rs.getString("P2.defense")+"/";
				result+=rs.getString("P2.speed")+"/";
				result+=rs.getString("P2.stamina")+"/";
				result+=rs.getString("P1.characterNum")+"/";
				result+=rs.getString("P2.characterNum")+"/";
			}
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
}

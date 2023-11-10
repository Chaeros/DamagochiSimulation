package damagochi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SetID {
	String result;
	String P1;
	String P2;
	SetID(String _P1,String _P2){
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
			
			String sql="SELECT P1.level, P1.name, P1.characterNum,P2.level, P2.name, P2.characterNum FROM Characters P1, Characters P2 WHERE P1.Id = '"+P1+"' and P2.Id ='"+P2+"';";
			stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			result="";
			while(rs.next()) {
				result+=rs.getString("P1.level")+"/";
				result+=rs.getString("P1.name")+"/";
				result+=rs.getString("P1.characterNum")+"/";
				result+=rs.getString("P2.level")+"/";
				result+=rs.getString("P2.name")+"/";
				result+=rs.getString("P2.characterNum")+"/";
			}
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
}

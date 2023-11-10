package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class CreateCharacter extends JFrame{   // 접속한 클라이언트가 처음으로 로그인했을 때(생성한 캐릭터가 없을 때) 띄우는 프레임
	JLabel explainLa = new JLabel("생성할 캐릭터의 이름을 지정해주세요.");
	JTextField determineNameTf = new JTextField();
	JButton queryBt = new JButton("ID중복확인");
	JLabel resultLa = new JLabel();
	JButton createBtn = new JButton("생성하기");
	String id;
	
	CreateCharacter(String id)
	{
		this.id=id;
		setTitle("캐릭터 생성"); 
		getContentPane();
		setLayout(null); // 컨테이너에 배치관리자를 사용하지 않음. (컴포넌트 위치/크기 절대적으로 지정.)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // 프레임의 X 클릭으로 창을 닫았을 때 프로그램을 종료 시키는 메소드
		
		explainLa.setBounds(10,5,220,22);
		determineNameTf.setBounds(10,30,220,22);
		resultLa.setBounds(235,5,100,22);
		queryBt.setBounds(235,30,100,22);
		createBtn.setBounds(100,55,100,22);
		//resultBt
		
		add(explainLa);
		add(determineNameTf);
		add(resultLa);
		add(queryBt);
		add(createBtn);
		
		queryBt.addActionListener(new MyActionListener());
		createBtn.addActionListener(new MyActionListener());
		determineNameTf.addActionListener(new MyEnterActionListener());
		
		setSize(360,123); 
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	class MyActionListener implements ActionListener{	// 버튼 이벤트 발생시 작동하는 액션리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();  // e가 누른 버튼의 객체를 받아주고 참조변수 b에 대입한다.
			
			if(b==queryBt)  // 입력한 캐릭터 이름 기존에 존재하는지 확인을 위한 이벤트
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
					ResultSet result = stmt.executeQuery("SELECT * FROM characters"); 
					String tempNAME="";
			
					// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
					while (result.next()) { 
						tempNAME=result.getString("name");
						if(tempNAME.equals(determineNameTf.getText()))
						{
							explainLa.setText("    사용불가");
							return;
						}
					}
					explainLa.setText("    사용가능");
					stmt.close();
					con.close();	

				} catch(SQLException ex) {
					System.err.println("Select 오류: " + ex.getMessage());
				}
			}
			else if(b==createBtn)
			{
				Connection con = null;  // DBMS 로그인 정보 입력
				Statement stmt = null;  // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
				String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost 아이피에서 chattingservice 스키마에 접근, 대한민국 시간대(물음표필수)
				String user = "root";   //DB ID
				String passwd = "admin"; //DB PW

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
					ResultSet result = stmt.executeQuery("SELECT * FROM characters"); 
					String tempNAME="";
			
					// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
					while (result.next()) { 
						tempNAME=result.getString("name");
						if(tempNAME.equals(determineNameTf.getText()))
						{
							JOptionPane.showMessageDialog(null, "이미 존재하는 캐릭터명입니다.", "Message", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					System.out.println("INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvaion,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)");  // 삽입문 확인용
					
					String sql="INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvation,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)";
					
					//새로운 클라이언트 정보 삽입
					PreparedStatement pre = con.prepareStatement(sql);
					pre.executeUpdate(); // db로 전송
					
					System.out.println("Characters Information Inserting Successfully!");
					
					// login테이블의 캐릭터존재여부 값 변경
					PreparedStatement pre0 = con.prepareStatement("UPDATE login SET logConfirm='exist' WHERE id='"+id+"'");
					pre0.execute();
					System.out.println("캐릭터 exist로 변경");
					
 
							
					setVisible(false);
					JOptionPane.showMessageDialog(null, "정상적으로 캐릭터가 생성되었습니다.", "Message", JOptionPane.INFORMATION_MESSAGE); 
					pre.close();
					stmt.close();
					con.close();	
					pre0.close();

				} catch(SQLException ex) {
					System.err.println("Select 오류: " + ex.getMessage());
				}
			}
			
		}
	}
	
	class MyEnterActionListener implements ActionListener{  // 텍스트필드 엔터입력시 액션객체 받는 액션리스너
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JTextField b=(JTextField)e.getSource();
			
			if(b==determineNameTf)
			{
				Connection con = null;  // DBMS 로그인 정보 입력
				Statement stmt = null;  // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
				String url = "jdbc:mysql://localhost/damagochi?serverTimezone=Asia/Seoul";  //localhost 아이피에서 chattingservice 스키마에 접근, 대한민국 시간대(물음표필수)
				String user = "root";   //DB ID
				String passwd = "admin"; //DB PW

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
					ResultSet result = stmt.executeQuery("SELECT * FROM characters"); 
					String tempNAME="";
			
					// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
					while (result.next()) { 
						tempNAME=result.getString("name");
						if(tempNAME.equals(determineNameTf.getText()))
						{
							JOptionPane.showMessageDialog(null, "이미 존재하는 캐릭터명입니다.", "Message", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					System.out.println("INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvaion,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)");  // 삽입문 확인용
					
					String sql="INSERT INTO Characters(name,id,characterNum,power,defense,speed,level,love,"
							+ "starvation,stamina,experience,sumExperience) values('"
							+ determineNameTf.getText() +"','"+ id +"', '1',1,0,0,1,10,10,10,0,100)";
					
					//새로운 클라이언트 정보 삽입
					PreparedStatement pre = con.prepareStatement(sql);
					pre.executeUpdate(); // db로 전송
					
					System.out.println("Characters Information Inserting Successfully!");
					
					// login테이블의 캐릭터존재여부 값 변경
					PreparedStatement pre0 = con.prepareStatement("UPDATE login SET logConfirm='exist' WHERE id='"+id+"'");
					pre0.execute();
					System.out.println("캐릭터 exist로 변경");
					
 
							
					setVisible(false);
					JOptionPane.showMessageDialog(null, "정상적으로 캐릭터가 생성되었습니다.", "Message", JOptionPane.INFORMATION_MESSAGE);
					Button button = new Button(id);
					pre.close();
					stmt.close();
					con.close();	
					pre0.close();

				} catch(SQLException ex) {
					System.err.println("Select 오류: " + ex.getMessage());
				}
			}
			
		}
		
	}

}

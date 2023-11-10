package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class SubscribeFrame extends JFrame{
	Container logincp;
	JLabel idLabel = new JLabel(" ID   입력");
	JLabel pwLabel = new JLabel(" PW 입력");
	JLabel pwConfirmLael = new JLabel(" PW 확인");
	JTextField idTextField = new JTextField();
	JTextField pwTextField = new JTextField();
	JTextField nameTextField = new JTextField();
	JPasswordField pwConfirmTextField = new JPasswordField();
	JButton subscribeBtn = new JButton("회원등록");
	JButton cancelBtn = new JButton("취소");
	
	boolean loginConfirm =false;
	
	
	SubscribeFrame() {   //로그인 창 생성
		setTitle("회원가입"); 
		logincp = getContentPane();
		logincp.setLayout(null); // 컨테이너에 배치관리자를 사용하지 않음. (컴포넌트 위치/크기 절대적으로 지정.)
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // 프레임의 X 클릭으로 창을 닫았을 때 프로그램을 종료 시키는 메소드

		//각각 컴포넌트의 절대적 위치 지정
		idLabel.setBounds(10,5,80,22);
		pwLabel.setBounds(10,30,80,22);
		pwConfirmLael.setBounds(10,55,80,22);
		
		idTextField.setBounds(70,5,200,22);
		pwTextField.setBounds(70,30,200,22);
		pwConfirmTextField.setBounds(70,55,200,22);
		
		subscribeBtn.setBounds(10,87,130,25);
		cancelBtn.setBounds(140,87,130,25);
		
		//컨테이너 cp에 컨포넌트 부착
		logincp.add(idLabel);
		logincp.add(pwLabel);
		logincp.add(pwConfirmLael);
		logincp.add(idTextField);
		logincp.add(pwTextField);
		logincp.add(pwConfirmTextField);
		logincp.add(subscribeBtn);
		logincp.add(cancelBtn);
		
		subscribeBtn.addActionListener(new MyActionListener());
		cancelBtn.addActionListener(new MyActionListener());
		idTextField.addActionListener(new MyEnterActionListener());
		pwTextField.addActionListener(new MyEnterActionListener());
		pwConfirmTextField.addActionListener(new MyEnterActionListener());
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(300,160); 
		setVisible(false); 
	}
	
	class MyEnterActionListener implements ActionListener{  // 텍스트필드 엔터입력시 액션객체 받는 액션리스너
		@Override
		public void actionPerformed(ActionEvent e)  // 텍스트에 포커스가 잡혀있을 때 Enter를 입력하면 이벤트 발생
		{
			if(idTextField.getText().equals("")) // id텍스트 필드 미입력 시
			{
				JOptionPane.showMessageDialog(null, "ID를 입력하지 않으셨습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
			}
			else if(pwTextField.getText().equals("")) // pw텍스트 필드 미입력 시
			{
				JOptionPane.showMessageDialog(null, "PW를 입력하지 않으셨습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
			}
			else if(pwConfirmTextField.getText().equals("")) //pw 확인 텍스트필드 미입력 시
			{
				JOptionPane.showMessageDialog(null, "PW점검을 입력하지 않으셨습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
			}
			else // 모든 텍스트 필드 입력 시, db로 입력정보 전송함
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
	
					
					ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
					System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
					String tempID="";
					String tempPW="";
			
					// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
					while (result.next()) { 
						tempID=result.getString("id");
						tempPW=result.getString("pw");
						System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
						if(idTextField.getText().equals(result.getString("id"))) // 입력한 id가 기존에 존재하는 id일 때
						{
							JOptionPane.showMessageDialog(null, "기존에 존재하는 ID입니다.", "Message", JOptionPane.ERROR_MESSAGE); 
							stmt.close();
							con.close();
							return;
						}
					}
					
					if(!pwTextField.getText().equals(pwConfirmTextField.getText()))  // pw와 pw확인에 입력 한 값이 일치하지 않을 때
					{
						JOptionPane.showMessageDialog(null, "입력한 두 PW가 일치하지 않습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
						stmt.close();
						con.close();
						return;
					}
					
					System.out.println("INSERT INTO Login(id,pw,logConfirm) values('"
							+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");  // 삽입문 확인용
					
					
					//새로운 클라이언트 정보 삽입
					PreparedStatement pre = con.prepareStatement("INSERT INTO Login(id,pw,logConfirm) values('"
							+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");
			
					pre.executeUpdate(); // db로 전송
					System.out.println("Subscriber Information Inserting Successfully!");
					JOptionPane.showMessageDialog(null, "정상적으로 회원가입되었습니다.", "Message", JOptionPane.INFORMATION_MESSAGE); 
					pre.close();
					stmt.close();
					con.close();	
					setVisible(false);
	
				} catch(SQLException ex) {
					System.err.println("Select 오류: " + ex.getMessage());
				}
			}
		}
		
	}
	
	class MyActionListener implements ActionListener{	// 버튼 이벤트 발생시 작동하는 액션리스너
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			JButton b = (JButton)e.getSource();  // e가 누른 버튼의 객체를 받아주고 참조변수 b에 대입한다.
			
			if(b==subscribeBtn)
			{
				if(idTextField.getText().equals(""))  // id텍스트 필드 미입력 시
				{
					JOptionPane.showMessageDialog(null, "ID를 입력하지 않으셨습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
				}
				else if(pwTextField.getText().equals("")) //pw텍스트 필드 미입력시
				{
					JOptionPane.showMessageDialog(null, "PW를 입력하지 않으셨습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
				}
				else if(pwConfirmTextField.getText().equals("")) // pw확인 텍스트 필드 미입력 시
				{
					JOptionPane.showMessageDialog(null, "PW점검을 입력하지 않으셨습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
				}
				else  // 모든 텍스트 필드 입력 시, db로 입력정보 전송함
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
		
						
						ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
						System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
						String tempID="";
						String tempPW="";
				
						// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
						while (result.next()) { 
							tempID=result.getString("id");
							tempPW=result.getString("pw");
							System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
							if(idTextField.getText().equals(result.getString("id"))) // 입력한 id가 기존에 존재하는 id일 때
							{
								JOptionPane.showMessageDialog(null, "기존에 존재하는 ID입니다.", "Message", JOptionPane.ERROR_MESSAGE); 
								stmt.close();
								con.close();
								return;
							}
						}
						
						if(!pwTextField.getText().equals(pwConfirmTextField.getText()))  // pw와 pw확인에 입력 한 값이 일치하지 않을 때
						{
							JOptionPane.showMessageDialog(null, "입력한 두 PW가 일치하지 않습니다.", "Message", JOptionPane.ERROR_MESSAGE); 
							stmt.close();
							con.close();
							return;
						}
						
						System.out.println("INSERT INTO Login(id,pw,logConfirm) values('"
								+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");  // 삽입문 확인용
						
						
						//새로운 클라이언트 정보 삽입
						PreparedStatement pre = con.prepareStatement("INSERT INTO Login(id,pw,logConfirm) values('"
								+ idTextField.getText() +"','"+ pwTextField.getText()+"', 'unexist')");
				
						pre.executeUpdate(); // db로 전송
						System.out.println("Subscriber Information Inserting Successfully!");
						JOptionPane.showMessageDialog(null, "정상적으로 회원가입되었습니다.", "Message", JOptionPane.INFORMATION_MESSAGE); 
						pre.close();
						stmt.close();
						con.close();	
						setVisible(false);
	
					} catch(SQLException ex) {
						System.err.println("Select 오류: " + ex.getMessage());
					}
				}
			}
			
		}
			
	}
}

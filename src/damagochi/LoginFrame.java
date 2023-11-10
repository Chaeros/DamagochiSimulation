package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;


public class LoginFrame extends JFrame{
	Container logincp;
	JLabel idLabel = new JLabel("      ID");
	JLabel pwLabel = new JLabel("      PW");
	JPanel panel;
	JLabel lblNewLabel;
	JTextField idTextField = new JTextField();
	JPasswordField pwTextField = new JPasswordField();
	JButton loginBtn = new JButton("로그인");
	JButton subscribeBtn = new JButton("회원가입");
	public SubscribeFrame SubscribeFrame;
	SubscribeFrame sub = new SubscribeFrame();
	public static String IDtemp;//ID 저장
	
	boolean loginConfirm =false;
	
	 Timer timer1 = new Timer(30, new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	        
	            if(panel.getHeight() != 135){
	            	panel.setBounds(0,0,LoginFrame.this.getSize().width,panel.getHeight() + 5);
	                if(panel.getHeight() == 135){
	                    timer1.stop();
	                }
	            }
	        }
	    });
	 
	 Timer timer2 = new Timer(30, new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	        
	            if(panel.getHeight() != 0){
	            	panel.setBounds(0,0,LoginFrame.this.getSize().width,panel.getHeight() - 5);
	                if(panel.getHeight() == 0){
	                    timer2.stop();
	                }
	            }
	        }
	    });
	 private final JTextField textField = new JTextField();
	
	LoginFrame() {
		textField.setText("DAMAGOCHI\nSIMULATION");
		textField.setFont(new Font("굴림", Font.BOLD, 30));
		textField.setBackground(UIManager.getColor("PasswordField.inactiveForeground"));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(117, 27, 676, 171);
		textField.setColumns(10);
		getContentPane().setBackground(UIManager.getColor("Button.darkShadow"));   //로그인 창 생성
		/*String dirStr = "D:\\DamaghchiSimulationImages";
		String dirStr1 = "D:\\DamaghchiSimulationImages\\1";
		String dirStr2 = "D:\\DamaghchiSimulationImages\\2";
		String dirStr3 = "D:\\DamaghchiSimulationImages\\3";
		//경로를 문자열로 받을 수도 있다
	    File newFile2 = new File(dirStr);
	    File newFile21 = new File(dirStr1);
	    File newFile22 = new File(dirStr2);
	    File newFile23 = new File(dirStr3);
	    if(newFile2.mkdir()){   //만드려는 디렉토리가 기존에 존재하지 않을경우
	      System.out.println("파일 설치 완료.");
	    }else{
	      System.out.println("이미 파일설치가 되어있습니다.");
	    }
	    
	    if(newFile21.mkdir()){
			Connection con = null;  // DBMS 로그인 정보 입력
			Statement stmt = null;  // 내가 DBMS에 어떤일을 시킬 것인지 정리해 놓는 객체(검색, 삽입, 삭제 등)
			String url = "jdbc:mysql://localhost/Damagochi?serverTimezone=Asia/Seoul";  //localhost 아이피에서 chattingservice 스키마에 접근, 대한민국 시간대(물음표필수)
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
	            
				//이미지 불러오기
				Statement stmt2 = con.createStatement();
		        ResultSet rs = stmt2.executeQuery("SELECT * FROM images WHERE characterNum='1'");
		        
		        try {
		           if(rs.next()){
		               InputStream is = rs.getBinaryStream("normal1");   //이미지 250*250
		               InputStream is2 = rs.getBinaryStream("attack1");
		               InputStream is3 = rs.getBinaryStream("attack2");
		               InputStream is4 = rs.getBinaryStream("defence1");
		               String sql ="D:\\DamaghchiSimulationImages\\1/normal1.PNG";
		               String sql2 ="D:\\DamaghchiSimulationImages\\1/attack1.PNG";
		               String sql3 ="D:\\DamaghchiSimulationImages\\1/attack2.PNG";
		               String sql4 ="D:\\DamaghchiSimulationImages\\1/defence1.PNG";
		               FileOutputStream fos = new FileOutputStream(sql);
		               FileOutputStream fos2 = new FileOutputStream(sql2);
		               FileOutputStream fos3 = new FileOutputStream(sql3);
		               FileOutputStream fos4 = new FileOutputStream(sql4);
		               byte[] buff = new byte[8192];
		               byte[] buff2 = new byte[8192];
		               byte[] buff3 = new byte[8192];
		               byte[] buff4 = new byte[8192];
		               int len;
		               int len2;
		               int len3;
		               int len4;
		               while( (len = is.read(buff)) > 0){
		                   fos.write(buff, 0, len);
		               }
		               while( (len2 = is2.read(buff2)) > 0){
		                   fos2.write(buff2, 0, len2);
		               }
		               while( (len3 = is3.read(buff3)) > 0){
		                   fos3.write(buff3, 0, len3);
		               }
		               while( (len4 = is4.read(buff4)) > 0){
		                   fos4.write(buff4, 0, len4);
		               }
		               System.out.println("이미지 불러오기 성공");
		               fos.close();
		               is.close();
		               rs.close();
		               
		               fos2.close();
		               is2.close();
		               
		               fos3.close();
		               is3.close();
		               
		               fos4.close();
		               is4.close();
		           }
		        }
		        catch(Exception e2)
		        {
		        	System.err.println("Select 오류: " + e2.getMessage());
		        	System.out.println("이미지 불러오기 실패");
		        }
				stmt.close();
				con.close();	
				
				stmt.close();
				con.close();	

			} catch(SQLException ex) {
				System.err.println("Select 오류: " + ex.getMessage());
			}
	    }
	    if(newFile22.mkdir()){}
	    if(newFile23.mkdir()){}*/
	    
		setTitle("login"); 
		logincp = getContentPane();
		logincp.setLayout(null); // 컨테이너에 배치관리자를 사용하지 않음. (컴포넌트 위치/크기 절대적으로 지정.)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // // 프레임의 X 클릭으로 창을 닫았을 때 프로그램을 종료 시키는 메소드

		//각각 컴포넌트의 절대적 위치 지정
		idLabel.setBounds(54,246,80,22);
		pwLabel.setBounds(54,337,80,22);
		
		idTextField.setBounds(197,246,200,22);
		pwTextField.setBounds(197,337,200,22);
		loginBtn.setFont(new Font("굴림", Font.BOLD, 30));
		loginBtn.setForeground(Color.BLACK);
		
		loginBtn.setBounds(593,245,200,114);
		subscribeBtn.setBounds(593,384,200,25);
		
		//컨테이너 cp에 컨포넌트 부착
		logincp.add(idLabel);
		logincp.add(pwLabel);
		logincp.add(idTextField);
		logincp.add(pwTextField);
		logincp.add(loginBtn);
		logincp.add(subscribeBtn);
		
		panel = new JPanel();
		panel.setBackground(UIManager.getColor("List.selectionBackground"));
		panel.setBounds(0, 0, 0, 0);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel.setBounds(44, 80, 631, 23);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("확인");
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 15));
		btnNewButton.setBounds(764, 48, 80, 80);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	timer2.start();
	            }
	        });
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("비밀번호 보이기");
		chckbxNewCheckBox.setBounds(407, 335, 131, 27);
		getContentPane().add(chckbxNewCheckBox);
		
		getContentPane().add(textField);
		chckbxNewCheckBox.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	 if(chckbxNewCheckBox.isSelected()){
	            		 pwTextField.setEchoChar((char)0);
	                 }else{
	                	 pwTextField.setEchoChar('*');
	                 }
	            }
	        });
		
		loginBtn.addActionListener(new MyActionListener());
		subscribeBtn.addActionListener(new MyActionListener());
		pwTextField.addActionListener(new MyEnterActionListener());
		//setLocationRelativeTo(null);
		Dimension frameSize = this.getSize(); // 프레임 사이즈
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
		this.setLocation((screenSize.width - frameSize.width)/4, (screenSize.height - frameSize.height)/4);
		setResizable(false);
		setSize(911,510); 
		setVisible(true); 
	}
	
	public static void main(String[] args)
	{
		LoginFrame log = new LoginFrame();
	}
	
	class MyEnterActionListener implements ActionListener{  // 텍스트필드 엔터입력시 액션객체 받는 액션리스너
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JTextField b=(JTextField)e.getSource();
			
			if(b==pwTextField)
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
					ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
					System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
				    String tempID="";
					String tempPW="";
			
					// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
					while (result.next()) { 
						tempID=result.getString("id");
						tempPW=result.getString("pw");
						System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
						if(idTextField.getText().equals(result.getString("id")) && pwTextField.getText().equals(result.getString("pw")))
						{
							setVisible(false);
							loginConfirm =true;
							System.out.println("로긴성공");
							if(result.getString("logConfirm").equals("unexist")) // 처음 접속하는 클라이언트일 때(해당 id에 캐릭터가 존재하지 않을 때)
							{
								CreateCharacter createCharacter = new CreateCharacter(idTextField.getText()); //캐릭터 생성 프레임
							}
							else // 처음 접속하는 클라이언트가 아닐 때(해당 id에 캐릭터가 이미 존재할 때)
							{
				                
								JOptionPane.showMessageDialog(null, "OK 계정확인완료.", "ErroMessage", JOptionPane.INFORMATION_MESSAGE);
								IDtemp=tempID;//아이디 저장
								System.out.println("저장된 : "+IDtemp);//저장된 아이디 확인용
								Button button = new Button(tempID);
							}
							break;
						}
					}
					if(!tempID.equals("") && !tempPW.equals(""))
					{
						if(!idTextField.getText().equals(tempID) || !pwTextField.getText().equals(tempPW) )
						{
							System.out.println("로긴실패");
							lblNewLabel.setText("Invalide Username Or Password");
							lblNewLabel.setForeground(Color.RED);
				            timer1.start();
							//JOptionPane.showMessageDialog(null, "존재하지않는 ID와 PW 입니다.", "Message", JOptionPane.ERROR_MESSAGE);//다이얼로그
							
						}
					}
					stmt.close();
					con.close();	

				} catch(SQLException ex) {
					System.err.println("Select 오류: " + ex.getMessage());
				}
			}
			
		}
		
	}
	
	class MyActionListener implements ActionListener{	// 버튼 이벤트 발생시 작동하는 액션리스너
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();  // e가 누른 버튼의 객체를 받아주고 참조변수 b에 대입한다.
			
			if(b==loginBtn)
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
					ResultSet result = stmt.executeQuery("SELECT * FROM login"); 
					System.out.println("id:" + idTextField.getText() + " , " + pwTextField.getText());
					String tempID="";
					String tempPW="";
			
					// result 객체에 저장된 질의 결과로부터 행의 정보 얻음
					while (result.next()) { 
						tempID=result.getString("id");
						tempPW=result.getString("pw");
						System.out.println("id:" + tempID + " and " + "pw: " + tempPW);
						if(idTextField.getText().equals(result.getString("id")) && pwTextField.getText().equals(result.getString("pw")))
						{
							setVisible(false);
							loginConfirm =true;
							System.out.println("로긴성공");
							if(result.getString("logConfirm").equals("unexist")) // 처음 접속하는 클라이언트일 때(해당 id에 캐릭터가 존재하지 않을 때)
							{
								CreateCharacter createCharacter = new CreateCharacter(idTextField.getText()); //캐릭터 생성 프레임
							}
							else // 처음 접속하는 클라이언트가 아닐 때(해당 id에 캐릭터가 이미 존재할 때)
							{
								JOptionPane.showMessageDialog(null, "OK 계정확인완료.", "ErroMessage", JOptionPane.INFORMATION_MESSAGE);
								IDtemp=tempID;//아이디 저장
								System.out.println("저장된 : "+IDtemp);//아이디 저장 확인용
								Button button = new Button(tempID);
							}
							break;
						}
					}
					if(!tempID.equals("") && !tempPW.equals(""))
					{
						if(!idTextField.getText().equals(tempID) || !pwTextField.getText().equals(tempPW) )
						{
							System.out.println("로긴실패");
							//JOptionPane.showMessageDialog(null, "존재하지않는 ID와 PW 입니다.", "Message", JOptionPane.ERROR_MESSAGE);//다이얼로그
							lblNewLabel.setText("존재하지않는 ID와 PW 입니다.");
							lblNewLabel.setForeground(Color.RED);
				            timer1.start();
						}
					}
					stmt.close();
					con.close();	

				} catch(SQLException ex) {
					System.err.println("Select 오류: " + ex.getMessage());
				}
			}
			else if(b==subscribeBtn)
			{
				//SubscribeFrame subscribe = new SubscribeFrame();
				sub.setVisible(true);
			}
			
		}
	}
}
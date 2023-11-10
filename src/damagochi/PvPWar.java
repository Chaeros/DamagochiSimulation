package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;

public class PvPWar extends JFrame{
	Container warContainer = new Container();
	Container chatContainer = new Container();
	
	//플레이어1과 관련된 컴포넌트
	JLabel player1LevelLa = new JLabel();
	JLabel player1NameLa = new JLabel();
	JLabel player1StaminaLa = new JLabel();
	ImageIcon player1ImageIcon;
	JLabel player1ImageLa = new JLabel();
	JLabel player1DamageLa = new JLabel();
	JProgressBar player1StaminaBar = new JProgressBar();
	JPanel player1Stamina = new JPanel();
	
	//플레이어2와 관련된 컴포넌트
	JLabel player2LevelLa = new JLabel();
	JLabel player2NameLa = new JLabel();
	JLabel player2StaminaLa = new JLabel();
	JLabel player2ImageLa = new JLabel();
	ImageIcon player2ImageIcon;
	JLabel player2DamageLa = new JLabel();
	JProgressBar player2StaminaBar = new JProgressBar();
	JPanel player2Stamina = new JPanel();
	
	//채팅과 관련된 컴포넌트
	JLabel chatLa = new JLabel("<채팅창>");
	JTextArea chatTArea = new JTextArea();
	JScrollPane chatAreaScroll = new JScrollPane(chatTArea);
	JTextField chatTf = new JTextField();
	JButton chatTransmitBtn = new JButton("전송");
	
	//그 외 기능의 컴포넌트
	JButton stateBtn = new JButton("준비완료");  //준비완료,준비취소, 항복 기능을 할 버튼(액션 이벤트 발생 시마다 버튼의 문자들을 변경 시켜줄 것이다.)
	JButton leaveRoomBtn = new JButton("방 나가기");
	JLabel stateLa = new JLabel("         대기 중"); //대기 중 및 게임 중을 표기할 레이블
	JLabel leaveRoomLa = new JLabel("        방 나가기");
	
	String roomName;
	String chatLog="";
	
	PvPWar(String roomName) {   //로그인 창 생성
		this.roomName=roomName;
		setTitle("방제목 : "+roomName); 
		setContentPane(new MyPanel());
		getContentPane();
		setLayout(null); // 컨테이너에 배치관리자를 사용하지 않음. (컴포넌트 위치/크기 절대적으로 지정.)
		
		player1ImageIcon=null;
		player1ImageLa.setIcon(player1ImageIcon);
		
		warContainer.setBounds(0,0,570,400);
		player1LevelLa.setBounds(20,10,50,20);
		player1NameLa.setBounds(110,35,50,20);
		player1StaminaLa.setBounds(210,10,50,20);
		player1ImageLa.setBounds(0,50,300,250);
		player1DamageLa.setBounds(30,250,20,20);
		//player1StaminaBar.setBounds(70,10,130,20);
		player1Stamina.setBounds(70,10,0,20);
		player1Stamina.setBackground(new Color(10,255,10));
		
		player2LevelLa.setBounds(310,10,50,20);
		player2NameLa.setBounds(410,35,50,20);
		player2StaminaLa.setBounds(500,10,50,20);
		player2ImageLa.setBounds(300,50,300,250);
		player2DamageLa.setBounds(520,250,20,20);
		//player2StaminaBar.setBounds(360,10,130,20);
		player2Stamina.setBounds(360,10,0,20);
		player2Stamina.setBackground(new Color(10,255,10));
		
		chatContainer.setBounds(570,0,230,400);
		chatLa.setBounds(10,10,100,30);
		chatAreaScroll.setBounds(10,40,200,270);
		chatTf.setBounds(10,320,135,20);
		chatTransmitBtn.setBounds(150,320,60,20);
		
		stateBtn.setBounds(10,320,100,30);
		leaveRoomBtn.setBounds(460,320,100,30);
		stateLa.setBounds(10,320,100,30);
		leaveRoomLa.setBounds(460,320,100,30);
		
		add(warContainer);
		warContainer.add(player1LevelLa);
		warContainer.add(player1NameLa);
		warContainer.add(player1StaminaLa);
		warContainer.add(player1ImageLa);
		warContainer.add(player1DamageLa);
		warContainer.add(player1Stamina);
		//warContainer.add(player1StaminaBar);
		
		warContainer.add(player2LevelLa);
		warContainer.add(player2NameLa);
		warContainer.add(player2StaminaLa);
		warContainer.add(player2ImageLa);
		warContainer.add(player2DamageLa);
		warContainer.add(player2Stamina);
		//warContainer.add(player2StaminaBar);
		
		add(chatContainer);
		chatContainer.add(chatLa);
		chatContainer.add(chatAreaScroll);
		chatContainer.add(chatTf);
		chatContainer.add(chatTransmitBtn);
		
		add(stateBtn);
		add(leaveRoomBtn);
		add(stateLa);
		add(leaveRoomLa);
		
		stateBtn.setVisible(false);
		leaveRoomLa.setVisible(false);
		setSize(800,400);                // 프레임 크기 설정
		setVisible(false); 
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	class MyPanel extends JPanel {  //테두리 및 구분선 생성을 위한 클래스
		ImageIcon icon = new ImageIcon("D:\\DamaghchiSimulationImages\\back\\pvp2.PNG"); 
		Image img = icon.getImage();
		
		public void paintComponent(Graphics g) { 
			super.paintComponent(g); 
			g.drawImage(img, 0,0, this);
			//g.setColor(Color.gray);
			//g.fillRect(0, 0, 570, 310);    //배경색
			g.setColor(Color.BLUE);        //검정색 선택
			g.drawLine(570, 0, 570, 400);  //구분선 생성
			
			g.setColor(Color.BLACK);        //검정색 선택
			g.drawLine(0, 310, 570, 310);  //구분선 생성
			g.drawLine(0, 0, 800, 0);  //구분선 생성
			g.drawRect(10,320,100,30);      //준비완료,취소,항복 버튼 테두리
			g.drawRect(69,9,132,22);  //player1 체력바
			g.drawRect(359,9,132,22); //player2 체력바
			g.drawRect(460,320,100,30); // 방 나가기 테두리
			//g.drawRect(395,15,170,220);     //사진 테두리 박스생성
			
		} 
	}
	
	class MyPanel2 extends JPanel {  //테두리 및 구분선 생성을 위한 클래스
		ImageIcon icon = new ImageIcon("D:\\DamaghchiSimulationImages\\back\\pvp1.PNG"); 
		Image img = icon.getImage();
		
		public void paintComponent(Graphics g) { 
			super.paintComponent(g); 
			g.setColor(Color.gray);
			g.fillRect(0, 0, 570, 310);    //배경색
			g.drawImage(img, 20,20, this);
			
		} 
	}
	
	
}
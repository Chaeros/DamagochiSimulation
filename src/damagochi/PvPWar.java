package damagochi;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;

public class PvPWar extends JFrame{
	Container warContainer = new Container();
	Container chatContainer = new Container();
	
	//�÷��̾�1�� ���õ� ������Ʈ
	JLabel player1LevelLa = new JLabel();
	JLabel player1NameLa = new JLabel();
	JLabel player1StaminaLa = new JLabel();
	ImageIcon player1ImageIcon;
	JLabel player1ImageLa = new JLabel();
	JLabel player1DamageLa = new JLabel();
	JProgressBar player1StaminaBar = new JProgressBar();
	JPanel player1Stamina = new JPanel();
	
	//�÷��̾�2�� ���õ� ������Ʈ
	JLabel player2LevelLa = new JLabel();
	JLabel player2NameLa = new JLabel();
	JLabel player2StaminaLa = new JLabel();
	JLabel player2ImageLa = new JLabel();
	ImageIcon player2ImageIcon;
	JLabel player2DamageLa = new JLabel();
	JProgressBar player2StaminaBar = new JProgressBar();
	JPanel player2Stamina = new JPanel();
	
	//ä�ð� ���õ� ������Ʈ
	JLabel chatLa = new JLabel("<ä��â>");
	JTextArea chatTArea = new JTextArea();
	JScrollPane chatAreaScroll = new JScrollPane(chatTArea);
	JTextField chatTf = new JTextField();
	JButton chatTransmitBtn = new JButton("����");
	
	//�� �� ����� ������Ʈ
	JButton stateBtn = new JButton("�غ�Ϸ�");  //�غ�Ϸ�,�غ����, �׺� ����� �� ��ư(�׼� �̺�Ʈ �߻� �ø��� ��ư�� ���ڵ��� ���� ������ ���̴�.)
	JButton leaveRoomBtn = new JButton("�� ������");
	JLabel stateLa = new JLabel("         ��� ��"); //��� �� �� ���� ���� ǥ���� ���̺�
	JLabel leaveRoomLa = new JLabel("        �� ������");
	
	String roomName;
	String chatLog="";
	
	PvPWar(String roomName) {   //�α��� â ����
		this.roomName=roomName;
		setTitle("������ : "+roomName); 
		setContentPane(new MyPanel());
		getContentPane();
		setLayout(null); // �����̳ʿ� ��ġ�����ڸ� ������� ����. (������Ʈ ��ġ/ũ�� ���������� ����.)
		
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
		setSize(800,400);                // ������ ũ�� ����
		setVisible(false); 
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	class MyPanel extends JPanel {  //�׵θ� �� ���м� ������ ���� Ŭ����
		ImageIcon icon = new ImageIcon("D:\\DamaghchiSimulationImages\\back\\pvp2.PNG"); 
		Image img = icon.getImage();
		
		public void paintComponent(Graphics g) { 
			super.paintComponent(g); 
			g.drawImage(img, 0,0, this);
			//g.setColor(Color.gray);
			//g.fillRect(0, 0, 570, 310);    //����
			g.setColor(Color.BLUE);        //������ ����
			g.drawLine(570, 0, 570, 400);  //���м� ����
			
			g.setColor(Color.BLACK);        //������ ����
			g.drawLine(0, 310, 570, 310);  //���м� ����
			g.drawLine(0, 0, 800, 0);  //���м� ����
			g.drawRect(10,320,100,30);      //�غ�Ϸ�,���,�׺� ��ư �׵θ�
			g.drawRect(69,9,132,22);  //player1 ü�¹�
			g.drawRect(359,9,132,22); //player2 ü�¹�
			g.drawRect(460,320,100,30); // �� ������ �׵θ�
			//g.drawRect(395,15,170,220);     //���� �׵θ� �ڽ�����
			
		} 
	}
	
	class MyPanel2 extends JPanel {  //�׵θ� �� ���м� ������ ���� Ŭ����
		ImageIcon icon = new ImageIcon("D:\\DamaghchiSimulationImages\\back\\pvp1.PNG"); 
		Image img = icon.getImage();
		
		public void paintComponent(Graphics g) { 
			super.paintComponent(g); 
			g.setColor(Color.gray);
			g.fillRect(0, 0, 570, 310);    //����
			g.drawImage(img, 20,20, this);
			
		} 
	}
	
	
}
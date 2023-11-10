# DamagochiSimulation
유저 간 대전 격투가 가능한 다마고치 시뮬레이션 게임(Socket통신을 통한 대전 격투 게임)
<br>

# 개발동기
개발 전, 소켓을 이용해 채팅프로그램을 먼저 만들어 봤었습니다.<br>
직접 서버를 구축하고 클라이언트 간 통신을 할 수 있도록 하는 과정이 참 흥미로웠습니다.<br>
그래서 유저 간 소켓 통신을 수행하는 프로젝트 주제를 고안해보았고,<br>
때마침 포켓몬스터 게임이 유행했어서 결이 비슷한 다마고치 대전 격투 게임을 개발하게 됐습니다.<br>
<br>

# 팀 구성 및 역할
- 박찬호(팀장): PvP 대전 기능 구현
- 박영우: 대기방 기능 구현
- 김난세: 케릭터 능력치 키우기 기능 구현
<br> 

# 개발 환경
- 서버
  - Java - 클라이언트로부터 받은 요청 수행
  - JDBC - Java 코드와 MySQL 연결
  - MySQL - 데이터 저장<br>
- 클라이언트
  - Java Swing - GUI 구현
  - Java - 서버와 통신을 통해 받은 데이터로 게임 수행
<br>
- 서버와 클라이언트 프로그램 간 소켓을 이용해 통신하도록함
<br>

# 기능
- 로그인 및 회원가입 시스템
- 케릭터 능력치 향상 기능
  - 버튼 클릭을 통한 훈련/밥주기/산책 수행
  - 각 기능 수행 시 다른 능력치가 상승함
  - 레벨10 달성 시, 능력치에 따라 다른 형태로 진화 수행
- PvP 대전 대기방 기능
  - 방 만들기 기능
  - 만들어진 방 입장 기능
  - 인원이 가득찬 방에는 입장 불가
- PvP 대전 기능
  - 유저 간 채팅 기능
  - 대전 격투 기능
  - 결투 후 보상 정산 기능

# 시연 이미지
- 로그인 및 회원가입 시스템<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/47c79e58-95ac-4f31-9c9c-512d75bfee80)
<br>

- 캐릭터 능력치 향상 기능
  - 최초 생성시 모습<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/6aa21c0a-412b-4c91-9ced-0bdcf21d4805)
<br>

- 캐릭터 능력치 향상 기능
  - 레벨 10 달성 시, 능력치에 따른 진화<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/da053833-a72e-47c2-b86c-ea26ed9dd7e9)
<br>

- PvP 대전 대기방 기능
  - 초기 대기방 진입시<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/b8eea390-4198-47df-87f4-c5e7279d9c73)
<br>

- PvP 대전 대기방 기능
  - 방 만들기<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/c0935c2a-724e-4818-a81c-c8bb5b35043b)
<br>

- PvP 대전 대기방 기능
  - 방 인원이 가득 찼을 경우<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/d0165749-3e40-40b3-9afb-f8769c7c41a2)
<br>

- PvP 대전 구현
  - 최초 방 생성 시<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/10a1529d-aebd-4462-be83-ac79a9130cff)
<br>

- PvP 대전 구현
  - Player2 입장 시<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/0cd4358b-43d1-455b-a0ba-d34340d12ca4)
<br>

- PvP 대전 구현
  - 채팅 <br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/7d9155e3-0471-4c70-90a5-c50ffc9d2aec)
<br>

- PvP 대전 구현
  - 대전 격투 기능(두 플레이어가 모두 준비 완료시 시작)<br>
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/19bf5d60-8c2b-4e8f-b087-e3ef8e15eb8f)
<br>

- PvP 대전 구현
  - 결과 출력 및 보상<br>  
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/bc6d9cfa-e817-45d6-84da-44c73374479a)
![image](https://github.com/Chaeros/DamagochiSimulation/assets/91451735/629aaee4-2431-45ca-a196-1b01a1ca5047)
<br>


package Puzzle_pkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Login_pkg.LoginData;
import Login_pkg.LoginDB;


public class Puzzle_Swing extends JFrame {
	PuzzleDB Pdb = new PuzzleDB();
	LoginData Ldata = new LoginData();
	LoginDB Ldb = new LoginDB();
	private JButton changebtn; // 빈칸과 바꿀 칸 변경
	private JButton[][] numbtn = new JButton[4][4]; // 15 까지의 버튼
	private int[][] numcount = new int[4][4]; // 15까지의 숫자
	private int row = 0, col = 0;
	private static String timerBuffer;  //경과 시간 문자열이 저장될 버퍼 정의
	private static int oldTime;  //타이머 시작 시각을 기억하고 있는 변수

	public Puzzle_Swing() {
		stopwatch(1); // 시간 측정 시작
		// 배치
		setTitle("슬라이딩 퍼즐");
		setSize(350, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new GridLayout(4, 4));

		int k = 1;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				numbtn[i][j] = new JButton(String.valueOf(k));
				numbtn[i][j].setFont(new Font("굴림체", Font.BOLD, 30));
				c.add(numbtn[i][j]);
				numbtn[i][j].addKeyListener(new MyKeyListener());
				k++;
			}
		}		
		

		getNum();
		display();
		
		setVisible(true);
	}

	// 0~16 난수발생
	public void getNum() {
		int[] num = new int[16];
		int n = 0;
		boolean Check = false;
		for (int i = 0; i < 16; i++) {
			Check = true;
			while (Check) {
				n = (int) (Math.random() * 16);
				Check = false;
				for (int j = 0; j < i; j++) {
					if (n == num[j]) // 같은 수 저장 방지
					{
						Check = true;
						break;
					}
				}
			}
			num[i] = n;
			numcount[i / 4][i % 4] = n;
			if (n == 15) { // 랜덤 칸 생성
				row = i / 4;
				col = i % 4;
			}

		}
	}

	// 디스플레이
	public void display() {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == row && j == col) {
					numbtn[i][j].setText(String.valueOf(""));

					numbtn[i][j].setEnabled(false);
				} else {
					System.out.println("numcount["+i+"]"+"["+j+"] "+numcount[i][j]+" ");
					numbtn[i][j].setText(String.valueOf(numcount[i][j] + 1));
					numbtn[i][j].setEnabled(true);
				}
			}
		}
	}

	// 종료 여부 확인 numbtn 과 k 가 같으면 종료
	public boolean isEnd() {
		
		int k = 1;
		try {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (Integer.parseInt(numbtn[i][j].getText()) != k)
					return false;
				System.out.println("k :"+k);
				k++;
			}
		}
		}catch(NumberFormatException e) { 
			
		}
		
		return true;

	}
	
	private class MyKeyListener extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			
			boolean isEnd = false;
			int keyCode = e.getKeyCode();
			
			switch (keyCode) {
			case KeyEvent.VK_UP: // 방향키 ↑ 클릭 시 동작
				if (row == 0) { // 바깥 나감 방지
					break;
				} else {
					changebtn = numbtn[row - 1][col]; // 변경할 위 버튼
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // 변경할 버튼 숫자 변경
					numbtn[row][col].setEnabled(true);

					row = row - 1; // 위에를 다시 가리킴

					changebtn = numbtn[row][col]; // 빈칸 버튼 지정
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);
					
					isEnd(); // 게임 종료 확인
					if (isEnd) { // isEnd를 true로 변경하면 키보드 입력 시 테스트가 바로 가능 함.
						
						String getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							while(true) {
								getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							}
							JOptionPane.showMessageDialog(null, "등록 완료		ID: "+getID+", 걸린 시간: "+timerBuffer);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}
						else {
							JOptionPane.showMessageDialog(null, "등록 완료		ID: "+getID+", 걸린 시간: "+timerBuffer);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}

					}
					break;
				}

			case KeyEvent.VK_DOWN:  // 방향키 ↓ 클릭 시 동작
				if (row == 3) {  // 바깥 나감 방지
					break;
				} else {
					changebtn = numbtn[row + 1][col];  // 변경할 아래 버튼
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // 변경할 버튼 숫자 변경
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					row = row + 1; // 아래를 다시 가리킴

					changebtn = numbtn[row][col]; // 빈칸 버튼 지정
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					isEnd(); // 게임 종료 확인
					if (isEnd) {
						
						String getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							while(true) {
								getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							}
							JOptionPane.showMessageDialog(null, "등록 완료		ID: "+getID+", 걸린 시간: "+timerBuffer);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}
						else {
							JOptionPane.showMessageDialog(null, "Your message: "+getID);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}

					}

					break;
				}
				
			case KeyEvent.VK_RIGHT:  // 방향키 → 클릭 시 동작
				if (col == 3) {  // 바깥 나감 방지
					break;
				} else {
					changebtn = numbtn[row][col + 1]; // 변경할 오른쪽 버튼
					numbtn[row][col].setText(String.valueOf(changebtn.getText()));  // 변경할 버튼 숫자 변경
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					col = col + 1; // 오른쪽을 다시 가리킴

					changebtn = numbtn[row][col]; // 빈칸 버튼 지정
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					isEnd(); // 게임 종료 확인
					if (isEnd) {
						
						String getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							while(true) {
								getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							}
							JOptionPane.showMessageDialog(null, "등록 완료		ID: "+getID+", 걸린 시간: "+timerBuffer);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}
						else {
							JOptionPane.showMessageDialog(null, "Your message: "+getID);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}

					}
				}
				break;
				
			case KeyEvent.VK_LEFT:  // 방향키 ← 클릭 시 동작
				if (col == 0) {  // 바깥 나감 방지
					break;
				} else {
					changebtn = numbtn[row][col - 1]; // 변경할 왼쪽 버튼
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // 변경할 버튼 숫자 변경
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					col = col - 1; // 왼쪽을 다시 가리킴

					changebtn = numbtn[row][col]; // 빈칸 버튼 지정
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					isEnd(); // 게임 종료 확인
					if (isEnd) {
						
						String getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							while(true) {
								getID = JOptionPane.showInputDialog("아이디를 입력 해주세요.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "아이디가 동일하지 않습니다.");
							}
							JOptionPane.showMessageDialog(null, "등록 완료		ID: "+getID+", 걸린 시간: "+timerBuffer);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}
						else {
							JOptionPane.showMessageDialog(null, "Your message: "+getID);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}

					}
					
					break;
				}
			}
			stopwatch(0); // 시간 측정 종료
		}
		
	}
	
	 public static void stopwatch(int onOff) {
		    if (onOff == 1)  //타이머 on
		      oldTime = (int) System.currentTimeMillis() / 1000;

		    if (onOff == 0) // 타이머 off, 시분초 timerBuffer 에 저장
		      secToHHMMSS(  ((int) System.currentTimeMillis() / 1000) - oldTime  );
		  }

		  // 정수로 된 시간을 초단위(sec)로 입력 받고, 문자열로 시분초를 저장
		  public static void secToHHMMSS(int secs) {
		    int hour, min, sec;

		    sec  = secs % 60;
		    min  = secs / 60 % 60;
		    hour = secs / 3600;

		    timerBuffer = String.format("%02d%02d%02d", hour, min, sec);
		  }
		  	  
}

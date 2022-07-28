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
	private JButton changebtn; // ��ĭ�� �ٲ� ĭ ����
	private JButton[][] numbtn = new JButton[4][4]; // 15 ������ ��ư
	private int[][] numcount = new int[4][4]; // 15������ ����
	private int row = 0, col = 0;
	private static String timerBuffer;  //��� �ð� ���ڿ��� ����� ���� ����
	private static int oldTime;  //Ÿ�̸� ���� �ð��� ����ϰ� �ִ� ����

	public Puzzle_Swing() {
		stopwatch(1); // �ð� ���� ����
		// ��ġ
		setTitle("�����̵� ����");
		setSize(350, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new GridLayout(4, 4));

		int k = 1;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				numbtn[i][j] = new JButton(String.valueOf(k));
				numbtn[i][j].setFont(new Font("����ü", Font.BOLD, 30));
				c.add(numbtn[i][j]);
				numbtn[i][j].addKeyListener(new MyKeyListener());
				k++;
			}
		}		
		

		getNum();
		display();
		
		setVisible(true);
	}

	// 0~16 �����߻�
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
					if (n == num[j]) // ���� �� ���� ����
					{
						Check = true;
						break;
					}
				}
			}
			num[i] = n;
			numcount[i / 4][i % 4] = n;
			if (n == 15) { // ���� ĭ ����
				row = i / 4;
				col = i % 4;
			}

		}
	}

	// ���÷���
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

	// ���� ���� Ȯ�� numbtn �� k �� ������ ����
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
			case KeyEvent.VK_UP: // ����Ű �� Ŭ�� �� ����
				if (row == 0) { // �ٱ� ���� ����
					break;
				} else {
					changebtn = numbtn[row - 1][col]; // ������ �� ��ư
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // ������ ��ư ���� ����
					numbtn[row][col].setEnabled(true);

					row = row - 1; // ������ �ٽ� ����Ŵ

					changebtn = numbtn[row][col]; // ��ĭ ��ư ����
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);
					
					isEnd(); // ���� ���� Ȯ��
					if (isEnd) { // isEnd�� true�� �����ϸ� Ű���� �Է� �� �׽�Ʈ�� �ٷ� ���� ��.
						
						String getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							while(true) {
								getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							}
							JOptionPane.showMessageDialog(null, "��� �Ϸ�		ID: "+getID+", �ɸ� �ð�: "+timerBuffer);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}
						else {
							JOptionPane.showMessageDialog(null, "��� �Ϸ�		ID: "+getID+", �ɸ� �ð�: "+timerBuffer);
							Pdb.InsertPuzzleDB(new PuzzleData(getID, timerBuffer));
							System.exit(0);	
						}

					}
					break;
				}

			case KeyEvent.VK_DOWN:  // ����Ű �� Ŭ�� �� ����
				if (row == 3) {  // �ٱ� ���� ����
					break;
				} else {
					changebtn = numbtn[row + 1][col];  // ������ �Ʒ� ��ư
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // ������ ��ư ���� ����
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					row = row + 1; // �Ʒ��� �ٽ� ����Ŵ

					changebtn = numbtn[row][col]; // ��ĭ ��ư ����
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					isEnd(); // ���� ���� Ȯ��
					if (isEnd) {
						
						String getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							while(true) {
								getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							}
							JOptionPane.showMessageDialog(null, "��� �Ϸ�		ID: "+getID+", �ɸ� �ð�: "+timerBuffer);
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
				
			case KeyEvent.VK_RIGHT:  // ����Ű �� Ŭ�� �� ����
				if (col == 3) {  // �ٱ� ���� ����
					break;
				} else {
					changebtn = numbtn[row][col + 1]; // ������ ������ ��ư
					numbtn[row][col].setText(String.valueOf(changebtn.getText()));  // ������ ��ư ���� ����
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					col = col + 1; // �������� �ٽ� ����Ŵ

					changebtn = numbtn[row][col]; // ��ĭ ��ư ����
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					isEnd(); // ���� ���� Ȯ��
					if (isEnd) {
						
						String getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							while(true) {
								getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							}
							JOptionPane.showMessageDialog(null, "��� �Ϸ�		ID: "+getID+", �ɸ� �ð�: "+timerBuffer);
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
				
			case KeyEvent.VK_LEFT:  // ����Ű �� Ŭ�� �� ����
				if (col == 0) {  // �ٱ� ���� ����
					break;
				} else {
					changebtn = numbtn[row][col - 1]; // ������ ���� ��ư
					numbtn[row][col].setText(String.valueOf(changebtn.getText())); // ������ ��ư ���� ����
					System.out.println("row : " + row + " col : " + col);
					numbtn[row][col].setEnabled(true);

					col = col - 1; // ������ �ٽ� ����Ŵ

					changebtn = numbtn[row][col]; // ��ĭ ��ư ����
					numbtn[row][col].setText("");
					numbtn[row][col].setEnabled(false);

					isEnd(); // ���� ���� Ȯ��
					if (isEnd) {
						
						String getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
						int s = Ldb.LoginOX(new LoginData(getID));
						if(s== -1) {
							JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							while(true) {
								getID = JOptionPane.showInputDialog("���̵� �Է� ���ּ���.");
								s = Ldb.LoginOX(new LoginData(getID));
								if(s == 1) {
									break;
								}
								else JOptionPane.showMessageDialog(null, "���̵� �������� �ʽ��ϴ�.");
							}
							JOptionPane.showMessageDialog(null, "��� �Ϸ�		ID: "+getID+", �ɸ� �ð�: "+timerBuffer);
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
			stopwatch(0); // �ð� ���� ����
		}
		
	}
	
	 public static void stopwatch(int onOff) {
		    if (onOff == 1)  //Ÿ�̸� on
		      oldTime = (int) System.currentTimeMillis() / 1000;

		    if (onOff == 0) // Ÿ�̸� off, �ú��� timerBuffer �� ����
		      secToHHMMSS(  ((int) System.currentTimeMillis() / 1000) - oldTime  );
		  }

		  // ������ �� �ð��� �ʴ���(sec)�� �Է� �ް�, ���ڿ��� �ú��ʸ� ����
		  public static void secToHHMMSS(int secs) {
		    int hour, min, sec;

		    sec  = secs % 60;
		    min  = secs / 60 % 60;
		    hour = secs / 3600;

		    timerBuffer = String.format("%02d%02d%02d", hour, min, sec);
		  }
		  	  
}

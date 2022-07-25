package Socket_pkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

	// Ŭ���̾�Ʈ ������ �޴¼���
	ServerSocket serverSocket;
	// ���� ����� �ϴ� ��Ĺ
	Socket socket;
	BufferedReader br;

	// �������� ���� ����
	// �̹� ������ �б⸦ �ϰ� �ֱ⶧���� �̸� �����ϱ����ؼ� ���ο� �����尡 �ʿ��ϴ�.
	BufferedWriter bw;
	BufferedReader keyboard;// Ű����� �д� ���� �̿��� ���ο� �����尡 �ʿ��ϴ�.

	public server() {
		System.out.println("1. �������� ����-------------------------------------------------------");
		try {
			serverSocket = new ServerSocket(10000);

			System.out.println("2. �������� �����Ϸ� : Ŭ���̾�Ʈ ���� ����� ----------------------------------------------");
			socket = serverSocket.accept();// Ŭ���̾�Ʈ ���� �����....
			System.out
					.println("3. Ŭ���̾�Ʈ ����Ϸ� -buffer ����Ϸ� (�б� ����)-----------------------------------------------------");

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));// �д� ����
			// socket.getInputStream()�� Ŭ���̾�Ʈ ������ ByteSream���� ������ br�� ���۸� �޾Ҵ�.
			// ���۸� ��� �����ʿ�¾��⶧���� �޽����� �޴°��� �ݺ�

			keyboard = new BufferedReader(new InputStreamReader(System.in)); // Ű���� ����
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));// ���¹���
			// ������ ����
			// �� ���� ������
			WriteThread wt = new WriteThread();
			Thread t1 = new Thread(wt);
			t1.start();

			// ���ν�����
			// ���б� ������
			while (true) {

				String msg = br.readLine();
				System.out.println("4. Ŭ���̾�Ʈ�� ���� ���� �޽��� : " + msg);
			}

		} catch (Exception e) {
			System.out.println("�������� ���� �߻��� : " + e.getMessage());
		}
	}

	// ���� Ŭ����
	class WriteThread implements Runnable {

		@Override
		public void run() {
			while (true) {

				try {
					System.out.println("Ű���� �޽��� �Է� �����--------------------------------");
					String keyboardMsg = keyboard.readLine();
					bw.write(keyboardMsg + "\n");// �޽������� \n���� ���� �˷��ش� ��ű�Ģ!
					bw.flush();// ���� ����
				} catch (Exception e) {
					System.out.println("���������ʿ��� Ű���� �Է¹޴� �� ������ �߻��߽��ϴ� : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {

		new server();

	}

}
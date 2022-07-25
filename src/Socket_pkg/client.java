package Socket_pkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class client {

	Socket socket;
	BufferedWriter bw;
	BufferedReader keyboard;

	// �б� ����
	// ���ο� ������ �ʿ�
	BufferedReader br;

	public client() {
		System.out.println("1. Ŭ���̾�Ʈ���� ����-------------------------------------------------------");
		try {
			socket = new Socket("localhost", 10000);// �ش� �ҽ��ڵ� ����� ���������� accept() �޼��� ȣ��
			// ����ǻ�Ͱ� ������ Ŭ���̾�Ʈ�� ���� �����ϴϱ� localhost�� ����
			// ��Ʈ��ȣ�� ������ ���� 10000���� ����ش�.

			System.out.println("2. ����(write)���� �Ϸ�-------------------------------------------------------");
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			// Ŭ���̾�Ʈ�� ����ϱ⶧���� Output������Ѵ�.

			// Ű���� ����
			System.out.println("3.Ű���� ��Ʈ�� + ����(read) ����Ϸ�-------------------------------------------------------");
			keyboard = new BufferedReader(new InputStreamReader(System.in));

			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));// �д� ����

			// ���꽺����(�� �б� ����)
			ReadThread rt = new ReadThread();
			Thread t1 = new Thread(rt);
			t1.start();

			// ���ν�����(�۾��� ����)
			// ���۸� ��� ���� �ʿ�� ���⶧���� �޽����� ���ºκи� �ݺ�
			while (true) {
				System.out.println("4. Ű���� �޽��� �Է� �����-------------------------------------------------------");
				String keyboardMsg = keyboard.readLine();
				// ����� ��Ģ!!
				// �޼��� ���� �˷�����Ѵ�. \n

				bw.write(keyboardMsg + "\n");
				// ������ ���� ������ �������Ѵ�.
				bw.flush();

			}

		} catch (Exception e) {
			System.out.println("Ŭ���̾�Ʈ ���� ���� �߻��� : " + e.getMessage());
			e.printStackTrace();
		}
	}

	class ReadThread implements Runnable {

		@Override
		public void run() {
			// �̹����� ���� �о���Ѵ�.
			while (true) {
				try {
					String msg = br.readLine();
					System.out.println("������ ���� ���� �޽��� : " + msg);
				} catch (IOException e) {
					System.out.println("Ŭ���̾�Ʈ �����ʿ��� �������� �޽����� �Է¹޴��� ������ �߻��߽��ϴ�." + e.getMessage());
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) {

		new client();
	}

}

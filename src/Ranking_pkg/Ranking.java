package Ranking_pkg;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import Puzzle_pkg.PuzzleData;
import Puzzle_pkg.PuzzleDB;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Ranking {

	public JFrame frame;
	private JTable table;
	
	String colNames[] = {"Rank","ID","Time"}; // ���̺� ��� �̸� ����
	private DefaultTableModel model = new DefaultTableModel(colNames, 0);

	public Ranking() { // ������
		initialize();
		select();
		KeyF5();
	}
	
	PuzzleDB db = new PuzzleDB();
	
	public void select() { // ��ŷ ���� ���
			Vector<PuzzleData> Ar = new Vector<PuzzleData>();
			Ar = db.PuzzleDBlist();
		
			for(int i=0; i< Ar.size();i++)
			{
			model.addRow(new Object[]{Ar.get(i).GetID(),Ar.get(i).GetTime(),Ar.get(i).GetRank()});
			}	
			
	}
	
	public void KeyF5(){ // F5 Ŭ�� �� ���ΰ�ħ
		frame.getContentPane().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				if(keyCode==KeyEvent.VK_F5) 
				{
					model.setRowCount(0);
					select();
					
				}
			}
		});
		frame.getContentPane().setFocusable(true);
		frame.getContentPane().requestFocus();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 458, 382);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 442, 343);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("���ʷչ���", Font.PLAIN, 18));
		scrollPane.setBounds(0, 0, 442, 343);
		panel.add(scrollPane);
		
		table = new JTable(model){
			public boolean isCellEditable(int row, int column) { // Ŭ�� ��Ȱ��ȭ
		        return false;
			}
		};

		table.setBackground(Color.WHITE);
		table.setFont(new Font("���ʷչ���", Font.PLAIN, 16));
		
		//���̺� ��� ����
		DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
		cell.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel centerModel = table.getColumnModel();
		for(int i=0;i < centerModel.getColumnCount(); i++) centerModel.getColumn(i).setCellRenderer(cell);
		
		//���̺� �÷��� �̵��� ����
				table.getTableHeader().setReorderingAllowed(false);      
				table.getColumnModel().getColumn(0).setPreferredWidth(20);
				table.getColumnModel().getColumn(0).setResizable(false);
				table.getColumnModel().getColumn(1).setPreferredWidth(162);
				
		scrollPane.setViewportView(table);

	}
}

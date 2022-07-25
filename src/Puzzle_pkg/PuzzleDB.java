package Puzzle_pkg;

import java.util.*;
import java.sql.*;

public class PuzzleDB{
	Connection conn = null;
	ResultSet rs = null;
	Statement st = null;
	PreparedStatement ps = null;
	
	public PuzzleDB() {
		try {
			final String url = "jdbc:mariadb://localhost:3306/puzzledb";
			final String id = "user";
			final String pw = "1234";
			
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pw);
			
		}catch(ClassNotFoundException cnfe) {
			System.out.println("DB 드라이버 로딩 실패:"+ cnfe.toString());
		}catch(SQLException sqle){
			System.out.println("DB 접속실패"+ sqle.toString());
		}catch(Exception e){
			System.out.println("Unkown error");
			e.printStackTrace();
		}
	}
	
	public void DBClose() {
		try {
			if(rs != null) rs.close();
			if(st != null) st.close();
			if(ps != null) ps.close();
		} catch (Exception e) {
			System.out.println(e + " => DBClose fail");
		}
	}
	
	public void InsertPuzzleDB(PuzzleData puzzledata) {
		try {
			String sql = "insert into puzzle values(?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, puzzledata.GetID());
			ps.setString(2, puzzledata.GetTime());
			ps.setString(3, null);
			ps.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DBClose();
		}
	}
	

	
	/*
	 * String Updata = "update puzzle set ID = ?, Rank = ?,  where Time = ?;"; ps =
	 * conn.prepareStatement(Updata);
	 */
	
	public Vector<PuzzleData> PuzzleDBlist()
	{
		Vector<PuzzleData> Ar = new Vector<PuzzleData>();
		 
		 try{
			 st = conn.createStatement();
			 
			 String sql = "Select DENSE_RANK() OVER (ORDER BY Time ASC ) as RANK, ID, Time from puzzle";
			 rs = st.executeQuery(sql);
			 while (rs.next()) {
				 Ar.add(new PuzzleData(rs.getString(1), rs.getString(2), rs.getString(3)));
			 }
		 }catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBClose();
			}
		 return Ar;
		 
	}
	
	

	public void UpdatePuzzleDB(PuzzleData puzzledata)
	{
		try {
			String Updata = "update puzzle set Rank = ?, where Time = ?;";
			ps = conn.prepareStatement(Updata);
			ps.setString(1, puzzledata.GetRank());
			ps.setString(2, puzzledata.GetTime());
			ps.executeUpdate();
			}catch(SQLException e){	
				e.printStackTrace();
			} finally {
				DBClose();
			}
	}

	public void Delete(String ID)
	{
		String Delete = "delete from puzzle where ID = ?;";
		try {
			ps = conn.prepareStatement(Delete);
			ps.setString(1, ID);
			ps.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
				DBClose();
		}	
	}
}
	



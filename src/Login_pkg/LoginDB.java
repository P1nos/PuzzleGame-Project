package Login_pkg;

import java.sql.*;

public class LoginDB {
	private Connection conn = null;
	private ResultSet rs = null;
	private Statement st = null;
	private PreparedStatement ps = null;
	int count = 0;
	
	
	public LoginDB() {
		
		final String url = "jdbc:mariadb://localhost:3306/puzzledb";
		final String id = "user";
		final String pw = "1234";
		try{
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

	public void DBClose(){
		try{
			if(rs != null) rs.close();
			if(st != null) st.close();
			if(ps != null) ps.close();
		}catch(Exception e) { System.out.println(e + "=> DBClose 실패");}
	}
	
	public void InsertLogin(LoginData logindata){
		String sql = "insert into login values(?, ?, ?)";
		
		try{
			ps = conn.prepareStatement(sql);
			st = conn.createStatement();
			rs = st.executeQuery("Select * From login order by position*1");
			while(rs.next())
			{
				int Number1 = rs.getInt("position");
				if(Number1 == count) count++;
				}
			ps.setInt(1, count);
			ps.setString(2, logindata.GetID());
			ps.setString(3, logindata.GetPassword());
			ps.executeUpdate();
			count++;
		}catch(SQLException e) 
		{
			e.printStackTrace();
		}finally 
		{
			DBClose();
		}
	}
	
	public void Delete(String ID){
		String sql = "Delete from login where ID =?";
		
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, ID);
			ps.executeUpdate();
		}catch(SQLException e)
		{	
			e.printStackTrace();
		} finally 
		{
			DBClose();
		}
	}
	
	public int LoginTry(LoginData logindata){
		String sql = "select * from login where ID = ? and Password = ?";
		
		try{
		ps = conn.prepareStatement(sql);
		ps.setString(1, logindata.GetID());
		ps.setString(2, logindata.GetPassword());
		rs = ps.executeQuery();
		
		if(rs.next())
		{
			return 1;		
		}	
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	public int LoginOX(LoginData logindata){
		String sql = "select * from login where ID = ?";
		try{
		ps = conn.prepareStatement(sql);
		ps.setString(1, logindata.GetID());
		rs = ps.executeQuery();
		if(rs.next()) 
		{
			return 1;
		}
		
		}catch(Exception e)
		{ 
			e.printStackTrace();
		}
		return -1;
	}

}


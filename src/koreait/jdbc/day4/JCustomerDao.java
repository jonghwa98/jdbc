package koreait.jdbc.day4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import koreait.jdbc.day2.OracleUtility;
/*
1. 회원 로그인 - 간단히 회원아이디를 입력해서 존재하면 로그인 성공
*/
public class JCustomerDao {
	
	// 1. 회원 로그인 - 간단히 회원아이디를 입력해서 존재하면 로그인 성공
	public JCustomer selectOne(String custom_id) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		// PK조회 : 결과 행 0 또는 1개
		String sql = "select * from j_custom where custom_id = ?";
		
		PreparedStatement ps = connection.prepareStatement(sql);
		// Statement : SQL, Prepared : SQL 이 미리 컴파일되어 준비된. 
		// PreparedStatement 는 Statement 인터페이스와 비교할 수 있습니다.
		// Statement 인터페이스 : SQL 실행에 필요한 데이터를 동시에 포함시켜서 컴파일을 합니다. (? 안에 값 넣음)
		
		ps.setString(1, custom_id);
		// 준비된 SQL 에 파라미터 전달하여
		
		ResultSet rs = ps.executeQuery();
		// select 쿼리 실행
		
		JCustomer temp = null;
		if(rs.next()) {
			temp = new JCustomer(
					rs.getString(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4), 
					rs.getDate(5));
		}
		
		ps.close();
		connection.close();
		return temp;
	}
	
	// -----------------------------------------------------------------------------
	
	public int insert(JCustomer jcustomer) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "insert into j_custom values(?, ?, ?, ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, jcustomer.getCustom_id());
		ps.setString(2, jcustomer.getName());
		ps.setString(3, jcustomer.getEmail());
		ps.setInt(4, jcustomer.getAge());
		ps.setDate(5, jcustomer.getReg_date());
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
	
	public int update(JCustomer jcustomer) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "update j_custom set name = ?, email = ?, age = ?, reg_date = ? where custom_id = ? ";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, jcustomer.getName());
		ps.setString(2, jcustomer.getEmail());
		ps.setInt(3, jcustomer.getAge());
		ps.setDate(4, jcustomer.getReg_date());
		ps.setString(5, jcustomer.getCustom_id());
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
	
	public int delete(String custom_id) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "delete table j_custom where custom_id = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, custom_id);
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
	
	public List<JCustomer> selectAll() throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "select * from j_custom";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		List<JCustomer> results = new ArrayList<>();
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			JCustomer jc = new JCustomer(
					rs.getString(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4), 
					rs.getDate(5));
			results.add(jc);
		}
		
		ps.close();
		connection.close();
		return results;
	}
}

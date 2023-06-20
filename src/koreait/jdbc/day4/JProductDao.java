package koreait.jdbc.day4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import koreait.jdbc.day2.OracleUtility;
/*
2. 상품 목록 보기
3. 상품명으로 검색하기 (그 외에 가격대 별 검색)
*/
public class JProductDao {
	
	// 2. 상품 목록 보기
	public List<JProduct> selectAll() throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "select * from j_product";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		List<JProduct> results = new ArrayList<>();
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			JProduct jd = new JProduct(
					rs.getString(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4));
			results.add(jd);
		}
		
		ps.close();
		connection.close();
		return results;
	}
	
	// 3. 상품명으로 검색하기 (유사검색 -> **`검색어가 포함된 상품명`**을 목록 조회하기)
	public List<JProduct> selectByPname(String pname) throws SQLException {
		// pname은 사용자가 입력한 검색어
		Connection connection = OracleUtility.getConnection();
		
		String sql = "select * from j_product "
				+ "where pname like '%' || ? || '%' order by price desc";
		// like는 유사 비교. % 기호 사용
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, pname);
		ResultSet rs = ps.executeQuery();
		
		List<JProduct> results = new ArrayList<>();
		while(rs.next()) {
			results.add(new JProduct(
					rs.getString(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4)));
		}
		
		ps.close();
		connection.close();
		return results;
	}
	
	// -----------------------------------------------------------------------------
	
	public JProduct selectOne(String pname) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "select * from j_product where pname = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, pname);
		ResultSet rs = ps.executeQuery();
		
		JProduct result = null;
		if(rs.next()) {
			String pcode = rs.getString(1);
			String category = rs.getString(2);
			int price = rs.getInt(4);
			result = new JProduct(pcode, category, pname, price);
		}
		
		ps.close();
		connection.close();
		return result;
	}
	
	public int insert(JProduct jproduct) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "insert into j_product values(?, ?, ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, jproduct.getPcode());
		ps.setString(2, jproduct.getCategory());
		ps.setString(3, jproduct.getPname());
		ps.setInt(4, jproduct.getPrice());
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
	
	public int update(JProduct jproduct) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "update j_product set category = ?, pname = ?, price = ? where pcode = ? ";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, jproduct.getCategory());
		ps.setString(2, jproduct.getPname());
		ps.setInt(3, jproduct.getPrice());
		ps.setString(4, jproduct.getPcode());
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
	
	public int delete(String pcode) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "delete table j_product where pcode = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, pcode);
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
}

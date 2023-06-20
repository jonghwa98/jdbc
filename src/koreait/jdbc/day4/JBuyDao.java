package koreait.jdbc.day4;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import koreait.jdbc.day2.OracleUtility;
/*
5. 상품 구매(결제)하기 - 장바구니의 데이터를 구매 테이블에 입력하기 (여러 개 insert)
*/
public class JBuyDao {		// 구매와 관련된 CRUD 실행 SQL. DAO : JCustomerDao, JProductDao
	// 메소드 이름은 insert, update, delete, select, selectByname 등등으로 이름을 작성하세요.
	
	// 트랜잭션을 처리하는 예시 : auto commit 을 해제하고 직접 commit을 합니다.*
	// try catch 를 직접하세요. throws 아닙니다.
	public int insert(List<JBuy> carts) {
		// 5. 상품 구매(결제)하기 - 장바구니의 데이터를 구매 테이블에 입력하기 (여러 개 insert)
		Connection connection = OracleUtility.getConnection();
		
		String sql = "insert into j_buy values(jbuy_seq.nextval, ?, ?, ?, sysdate)";
		
		int count = 0;
		PreparedStatement ps = null;
		try {
			connection.setAutoCommit(false);		// auto commit 설정 - false
			ps = connection.prepareStatement(sql);
			for(JBuy b : carts) {
				ps.setString(1, b.getCustom_id());
				ps.setString(2, b.getPcode());
				ps.setInt(3, b.getQuantity());
				count += ps.executeUpdate();
			}
			connection.commit();			// 커밋
		} catch (SQLException e) {
			System.out.println("장바구니 상품 구매하기 예외 : " + e.getMessage());
			System.out.println("장바구니 상품 구매를 취소합니다.");
			try {
				connection.rollback();		// 롤백
			} catch(SQLException e1) {
			}
		}
		return count;
	}
	
	// -----------------------------------------------------------------------------

	public int update(JBuy jbuy) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "update j_buy set custom_id = ?, pcode = ?, quantity = ?, buy_date = ? where buy_seq = ? ";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, jbuy.getCustom_id());
		ps.setString(2, jbuy.getPcode());
		ps.setInt(3, jbuy.getQuantity());
		ps.setDate(4, jbuy.getBuy_date());
		ps.setInt(5, jbuy.getBuy_seq());
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
	
	public int delete(String buy_seq) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "delete table j_buy where buy_seq = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, buy_seq);
		int result = ps.executeUpdate();
		
		ps.close();
		connection.close();
		return result;
	}
	
	public List<JBuy> selectAll() throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "select * from j_buy";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		List<JBuy> results = new ArrayList<>();
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			JBuy jb = new JBuy(
					rs.getInt(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4), 
					rs.getDate(5));
			results.add(jb);
		}
		
		ps.close();
		connection.close();
		return results;
	}
	
	public JBuy selectOne(String pcode) throws SQLException {
		Connection connection = OracleUtility.getConnection();
		
		String sql = "select * from j_buy where pcode = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, pcode);
		ResultSet rs = ps.executeQuery();
		
		JBuy result = null;
		if(rs.next()) {
			int buy_seq = rs.getInt(1);
			String custom_id = rs.getString(2);
			int quantity = rs.getInt(4);
			Date buy_date = rs.getDate(5);
			result = new JBuy(buy_seq, custom_id, pcode, quantity, buy_date);
		}
		
		ps.close();
		connection.close();
		return result;
	}

	public int insert(JBuy buy) {
		
		return 1;
	}

	public JBuy selectOne(int buy_seq) throws SQLException {
		// sql 실행을 구현을 하고 테스트 케이스 확인하기
		Connection conn = OracleUtility.getConnection();
		
		String sql = "select * from j_buy where buy_seq = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, buy_seq);
		ResultSet rs = ps.executeQuery();
		JBuy result = null;
		if(rs.next()) {
			result = new JBuy(
					rs.getInt(1),
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4), 
					rs.getDate(5));
		}

		ps.close();
		conn.close();
		return result;
	}
	
}

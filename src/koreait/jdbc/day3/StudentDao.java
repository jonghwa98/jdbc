package koreait.jdbc.day3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import koreait.jdbc.day2.OracleUtility;

// DAO : Data Access(접근-읽기와 쓰기) Object
//		 SQL 실행 메소드를 모아 놓은 클래스. 

/* StudentDao의 내용을 요약.
 * insert, update 는 sql 파라미터에 전달한 데이터의 타입을 dto
 * delete는 										원시형 또는 String
 * delete sql의 조건절 컬럼이 여러 개일 때는 dto가 될 수 있습니다. map 도 종종 사용합니다.
 * 
 * insert, update, delete 는 정수 리턴값으로 반영된 행의 개수를 전달.
 * 
 * selectOne : sql 파라미터에 전달할 데이터를 메소드 인자로 함.
 * selectAll : 파라미터 없으며, 여러 개의 행을 저장할 객체는 List 타입.
 */
public class StudentDao {
	// 나중에 db를 `쉽게 코딩`하기 위한 `프레임워크`를 사용하면 Exception 처리 안해도 됩니다.
	public int insert(StudentDto student) throws SQLException {

		Connection connection = OracleUtility.getConnection();

		String sql = "insert into tbl_student values(?, ?, ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sql);

		ps.setString(1, student.getStuno());
		ps.setString(2, student.getName());
		ps.setInt(3, student.getAge());
		ps.setString(4, student.getAddress());
		int result = ps.executeUpdate();

		ps.close();
		connection.close();
		return result;
	}

	public int update(StudentDto student) throws SQLException {

		Connection connection = OracleUtility.getConnection();

		String sql = "update tbl_student set age = ?, address = ? where stuno = ?";
		PreparedStatement ps = connection.prepareStatement(sql);

		ps.setInt(1, student.getAge());
		ps.setString(2, student.getAddress());
		ps.setString(3, student.getStuno());
		int result = ps.executeUpdate();

		ps.close();
		connection.close();
		return result;
	}

	// delete 메소드는 여러분이 만들어보세요. - 매개변수는 ?
	public int delete(String stuno) throws SQLException {

		Connection connection = OracleUtility.getConnection();

		String sql = "delete tbl_student where stuno = ?";
		PreparedStatement ps = connection.prepareStatement(sql);

		ps.setString(1, stuno);		// 메소드 인자(매개변수)값을 sql 쿼리에 전달
		int result = ps.executeUpdate();

		ps.close();
		connection.close();
		return result;
	}
	
	// select 모두 조회 - 조회결과 값들을 List 객체로 리턴합니다.
	// 메소드에서 조회된 결과만을 리턴. 출력 증 기타기능들은 이 메소드를 사용하는 프로그램에서 구현합니다.
	public List<StudentDto> selectAll() throws SQLException {
		
		Connection connection = OracleUtility.getConnection();
		String sql = "select * from tbl_student";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		List<StudentDto> results = new ArrayList<>();
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			
			StudentDto sd = new StudentDto();
			sd.setStuno(rs.getString(1));
			sd.setName(rs.getString(2));
			sd.setAge(rs.getInt(3));
			sd.setAddress(rs.getString(4));
			results.add(sd);
			
//			StudentDto sd = new StudentDto(rs.getString(1), 
//											rs.getString(2), 
//											rs.getInt(3), 
//											rs.getString(4));
//			results.add(sd);
		}
		return results;
	}
	
	// select * from tbl_student where stuno = '2023002'; 실행을 위한
	//	 ㄴ selectOne 메소드 정의
	public StudentDto selectOne(String stuno) throws SQLException{
		
		Connection connection = OracleUtility.getConnection();
		
		String sql = "select * from tbl_student where stuno = ?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, stuno);
		ResultSet rs = ps.executeQuery();
		
		StudentDto result = null;
		if(rs.next()) {
//			String stuno2 = rs.getString(1);	// == ps.setString(1, stuno);
			String name = rs.getString(2);
			int age = rs.getInt(3);
			String address = rs.getString(4);
			result = new StudentDto(stuno, name, age, address);
			
			// 코드 줄이면 아래와 같이 씁니다.
//			return new StudentDto(stuno, rs.getString(2), rs.getInt(3), rs.getString(4));
		}
		
		ps.close();
		connection.close();
		return result;
	}
	
}
















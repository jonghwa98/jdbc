package koreait.jdbc.day1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

// 학생 성적처리 프로그햄 중에 새로운 학생을 등록(입력) 하는 기능을 만들어 봅시다.
// (테이블에 insert 실행)
public class InsertDMLTest {
	public static void main(String[] args) {
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "iclass";
		String password = "0419";
		
		try (
				Connection conn = DriverManager.getConnection(url, user, password);
			){
			
			System.out.println("연결 상태 = " + conn);
			if(conn != null)
				System.out.println("오라클 데이터베이스 연결 성공!! ");
			
			// db연결 완료 후에 sql 실행하기.
			
			// insert SQL 작성 : 제약조건(기본키 stuno) 위반 되지 않는 값으로 입력하기
			String sql = "INSERT INTO TBL_STUDENT VALUES('2023002', '김땡이', 16, '경기도')";
			
			// PreparedStatement `객체를 생성하면서 실행할 sql을 설정`합니다.
			// PreparedStatement 객체는 Connection 객체 메소드로 만듭니다. 
			// Connetion 구현객체는 dbms 종류에 따라 생성되고 PreparedStatement 객체도 그에 따라
			// 구현 객체가 결정됩니다.
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// oracle.jdbc.driver.OraclePreparedStatementWrapper 클래스로 객체가 생성
			System.out.println("pstmt 객체의 구현 클래스 : " + pstmt.getClass().getName());
			
			// 'prepareStatement() 메소드는 객체를 생성'해서 리턴합니다.
			pstmt.execute();		// PreparedStatement 객체로 excute 하면 'SQL이 실행'됩니다.
			pstmt.close();
			
		} catch(Exception e) {
			System.out.println("오류메시지 = " + e.getMessage());
//			e.printStackTrace();	// Exception 발생의 모든 원인을 cascade 형식으로 출력.
		} 
		
		// conn.close()를 명시적으로 실행할 필요가 없습니다. 자동 close 
	}
}
/*
 * Statement 인터페이스의 SQL 쿼리 처리와 관련된 방법을 정의합니다.
 * 객체는 SQL 쿼리문을 데이터베이스에 전송합니다. Connection 객체를 통해서 만듭니다.
 * 
 * PreparedStatement는 Statement의 자식 인터페이스.
 * 특징은 sql을 먼저 컴파일하고 sql 실행에 필요한 값은 실행할 때 매개변수로 전달하는 방식입니다.
 */
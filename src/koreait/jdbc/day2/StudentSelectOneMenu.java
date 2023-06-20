package koreait.jdbc.day2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentSelectOneMenu {
	public static void main(String[] args) {
		
		Connection conn = OracleUtility.getConnection();
		System.out.println("::::::::: 학생을 학번으로 조회하는 메뉴 :::::::::");
		SelectOneStudent(conn);
		
		OracleUtility.close(conn);
	}

	private static void SelectOneStudent(Connection conn) {
		Scanner sc = new Scanner(System.in);
		String stuno = "2023002";
		String sql = "SELECT * FROM TBL_STUDENT WHERE STUNO = ?";
		try(PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, stuno);
			// sql 실행하고 select 는 조회 데이터를 결과로 받아 자바 변수에 저장해야 합니다.
			//						-> ResultSet 인터페이스 타입 객체로 저장합니다.
			ResultSet rs = ps.executeQuery();	// select 실행하기
			// ResultSet 객체 rs로 참조가 됩니다. rs는 읽어올 행의 커서를 갖고 있습니다.
			// 맨 처음에는 그 커서를 rs.next() 메소드로 첫번째 행에 위치를 시켜야 합니다.
			System.out.println("rs 객체의 구현 클래스는 " + rs.getClass().getName());
			// 		ㄴ oracle.jdbc.driver.OracleResultSetImpl 클래스 객체로 만들어집니다.
			
			// rs.next() 데이터를 가져올 커서(위치)를 다음 행으로 이동합니다.
			// 조회 결과 유무를 알려면 `제일 먼저 실행해야 할 메소드 - 조회 결과 첫번째 행으로 이동`입니다.
			System.out.println("조회 결과가 있을까요? " + rs.next());
			// 조회된 rs에서 특정 컬럼값을 가져오기 할 때, 컬럼의 데이터 타입을 확인하고
			// getXXXX 메소드 정하기. getXXXX 메소드의 인자는 컬럼의 인덱스 또는 컬럼 이름입니다.
			System.out.println("조회 결과 첫번째 컬럼의 값 : " + rs.getString(1));
			System.out.println("조회 결과 두번째 컬럼의 값 : " + rs.getString(2));
			System.out.println("조회 결과 세번째 컬럼의 값 : " + rs.getInt(3));
			System.out.println("조회 결과 네번째 컬럼의 값 : " + rs.getString(4));
			
			System.out.println("다음 조회 결과 행이 또 있을까요? " + rs.next());
		} catch (SQLException e) {
			System.out.println("데이터 조회에 문제가 생겼습니다. 상세내용 - " + e.getMessage());
			// 결과 집합을 모두 소모했음 -> 조회 결과가 없는데 rs.getXXXX 메소드 실행할 때의 예외 메시지.
		}
		sc.close();
	}
}

package koreait.jdbc.day2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentDeleteManu {
	public static void main(String[] args) {

		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "iclass";
		String password = "0419";
		System.out.println("::::::::::::::::::::학생 삭제 메뉴입니다.::::::::::::::::::::");
		System.out.println("지정된 학번으로 학생 정보를 삭제할 수 있습니다.");

		try (Connection conn = DriverManager.getConnection(url, user, password);) {

			deleteStudent(conn);
		} catch (Exception e) {
			System.out.println("오류 메시지 = " + e);
		}
	}
//	@SuppressWarnings("resource")	// 리소스와 관련 경고는 표시하지 않게 해주세요.
	private static void deleteStudent(Connection connection) throws Exception {
		Scanner sc = new Scanner(System.in);

		String stuno;
		String sql = "DELETE TBL_STUDENT WHERE STUNO = ?";

		System.out.println("학생번호 0000 입력은 삭제 취소입니다.");
		System.out.print("학번을 입력하세요 >>> ");
		stuno = sc.nextLine();

		if (stuno.equals("0000")) {
			System.out.println("학생 정보 삭제를 취소합니다.");
			sc.close();
			return; // 리턴에 값이 없을 때는 단순하게 메소드 종료로 실행됩니다.
		}

		try (PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setString(1, stuno);
			int count = ps.executeUpdate();
			System.out.println("학생 정보 삭제 " + count + " 건이 완료되었습니다.");
		} catch (SQLException e) {
			System.out.println("데이터 삭제에 문제가 생겼습니다. " + e.getMessage());
		}
		sc.close();
	}
}

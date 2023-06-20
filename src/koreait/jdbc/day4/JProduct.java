package koreait.jdbc.day4;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class JProduct {
	private String pcode;
	private String category;
	private String pname;
	private int price;
	
	public JProduct() {
		
	}
}

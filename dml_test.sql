-- CRUD : 데이터의 생성, 읽기, 갱신, 삭제를 가리키는 약자.
-- 프로그램이 가져야 할 사용자 인터페이스(메뉴) 기본 기능.

-- 단순 조회 (read)
SELECT * FROM TBL_STUDENT;

-- insert 테스트 (create)
INSERT INTO TBL_STUDENT VALUES('2023001', '김땡땡', 16, '경기도');

-- update 테스트 
UPDATE TBL_STUDENT 
SET AGE = 17, ADDRESS = '종로구'
WHERE STUNO = '2023006';

-- delete 삭제 테스트
DELETE TBL_STUDENT WHERE STUNO = '2023005';

-- 
SELECT * FROM TBL_STUDENT WHERE STUNO = '2023002';

SELECT * FROM TBL_Score;

select count(*) from tbl_student;

-- 여기서부터는 다른 테이블로 연습합니다.
/*
1. 회원 로그인 - 간단히 회원아이디를 입력해서 존재하면 로그인 성공
2. 상품 목록 보기
3. 상품명으로 검색하기 (그 외에 가격대 별 검색)
4. 상품 장바구니 담기 - 장바구니 테이블이 없으므로 구매를 원하는 상품을 List 에 담기
5. 상품 구매(결제)하기 - 장바구니의 데이터를 구매 테이블에 입력하기 (여러 개 insert)
6. 나의 구매 내역 보기. 총 구매 금액을 출력해주기
*/

select * from tbl_custom;
select * from tbl_product;
select * from tbl_product where pname like '%' || '동원' || '%';
select * from tbl_buy;		-- 구매 정보 테이블
select * from tbl_buy where customid = 'mina012';

-- 기존에 연습했던 테이블을 변경하지 않도록 새롭게 복사해서 jdbc 구현합니다.
create table j_custom
as
select * from tbl_custom;		

create table j_product
as
select * from tbl_product;

create table j_buy
as
select * from tbl_buy;

select * from j_custom;
select * from j_product;
select * from j_buy;

-- pk, fk는 필요하면 제약조건을 추가합니다.
-- custom_id, pcode, buy_seq 컬럼으로 pk 설정하기
-- j_buy 테이블에는 외래키도 2개가 있습니다.(j_buy 외래키 설정 제외하고 하겠습니다.)

alter table j_custom add constraint custom_pk primary key (custom_id);
alter table j_product add constraint pcode_pk primary key (pcode);
alter table j_buy add constraint buy_seq_pk primary key (buy_seq);

-- 추가 데이터 입력
insert into j_product values ('ZZZ01', 'B1','오뚜기바몬드카레', 2400);
insert into j_product values ('APP11', 'A1','얼음골사과 1박스', 32500);
insert into j_product values ('APP99', 'A1','씻은사과 10개', 25000);

-- j_buy 테이블에 사용할 시쿼스
-- 적절한 시작값으로 다시 생성하기
create sequence jbuy_seq start with 1008;
select jbuy_seq.nextval from dual;

-- 테이블, 시퀀스 삭제
delete j_buy where pcode = 'ZZZ01';
drop sequence jbuy_seq;		

-- delete from j_buy where buy_seq = 1029;
-- rollback 테스트
select * from j_buy;
alter table j_buy add constraint q_check check (quantity between 1 and 30);

-- check 제약조건 오류
insert into j_buy values (jbuy_seq.nextval, 'twice', 'APP99', 33, sysdate);	

-- 6. 마이페이지 구매내역
-- 2개 테이블 join 하여 행단위로 합계(수량 * 가격) 수식까지 조회하기
select buy_date, customid, jp.pcode, pname, quantity, price, (jb.quantity * jp.price) total
from j_product jp
join j_buy jb
on jp.pcode = jb.pcode
order by buy_date desc;

-- 자주 사용될 join 결과는 view 로 만들기 view 는 create or replace 로 생성 후에 수정까지 가능.
-- view 는 물리적인 테이블이 아닙니다. 물리적 테이블을 이용해서 만들어진 가상의 테이블(논리적 테이블)
create or replace view mypage_buy
as
select buy_date, customid, jp.pcode, pname, quantity, price, (jb.quantity * jp.price) total
from j_product jp
join j_buy jb
on jp.pcode = jb.pcode
order by buy_date desc; 

select * from mypage_buy where customid = 'mina012';

select sum(total) from mypage_buy where customid = 'mina012';

-- 외부평가 샘플문제
select * from member_tbl_02;
select * from money_tbl_02;

create table j_member
as
select * from member_tbl_02;

create table j_money
as
select * from money_tbl_02;

select * from j_member;
select * from j_money;

-- custno, salenol 컬럼으로 pk 설정하기
alter table j_member add constraint custno_pk primary key (custno);
alter table j_money add constraint custno2_pk primary key (custno, salenol);

-- j_member 테이블에 사용할 시쿼스
create sequence jmember_seq start with 1007;
select jmember_seq.nextval from dual;

-- 테이블, 시퀀스 삭제
delete j_member where CUSTNO = '100007';
drop sequence jmember_seq;	

-- 회원등록
insert into j_member values(jbuy_seq.nextval, ?, ?, ?, to_date(?, 'YYYY-MM-DD'), ?, ?);

-- 회원목록조회
-- REPLACE(REPLACE(REPLACE(GRADE, 'A', 'VIP'), 'B', '일반'), 'C', '직원') GRADE, 
SELECT 
	CUSTNO, 
	CUSTNAME, 
	PHONE,
	ADDRESS, 
	JOINDATE, 
	DECODE(GRADE, 'A', 'VIP', 'B', '일반', 'C', '직원') GRADE, 
	CITY
FROM j_member;


-- join 결과는 view 로 만들기
CREATE OR REPLACE VIEW sales_inquiry AS
SELECT
    jmb.CUSTNO,
    jmb.CUSTNAME,
    DECODE(jmb.GRADE, 'A', 'VIP', 'B', '일반', 'C', '직원') AS GRADE,
    SUM(jmn.AMOUNT * jmn.PCOST) AS total
FROM
    j_member jmb
JOIN
    j_money jmn 
ON 
	jmb.CUSTNO = jmn.CUSTNO
GROUP BY
    jmb.CUSTNO, jmb.CUSTNAME, jmb.GRADE
ORDER BY
    total DESC;
-- 또는
CREATE OR REPLACE VIEW sales_inquiry AS
select 
	met.custno, custname,
	decode(met.grade, 'A', 'VIP', 'B', '일반', 'C', '직원') as grade,
	asum
from 
	j_member met 
join
	(select custno, sum(price) asum 
	from j_money mot 
	group by custno
	order by asum desc) sale
on 
	met.custno = sale.custno 
ORDER BY 
	asum desc;

select * from sales_inquiry;
select * from sales_inquiry where CUSTNO = '100001';
select sum(total) from sales_inquiry where CUSTNO = '100001';

select custno, sum(price) asum from jmony group by 


-- step 1 회원별 매출합계
select custno, sum(price) from money_tbl_02 group by custno;

-- step 2 정렬 기준 확인하기
select custno, sum(price) from money_tbl_02 group by custno order by sum(price) desc;

-- step 3 custno 컬럼으로 조인하여 고객 정보 전체 가져오기
select * from member_tbl_02 met,
   (select custno, sum(price) asum from money_tbl_02 mot 
   group by custno
   order by asum desc) sale
where met.custno = sale.custno;
-- 또는
select * from member_tbl_02 met join
   (select custno, sum(price) asum from money_tbl_02 mot 
   group by custno
   order by asum desc) sale
on met.custno = sale.custno;

-- step 4 필요한 컬럼만 가져오기
select met.custno, custname,
   decode(met.grade, 'A', 'VIP', 'B', '일반', 'C', '직원') as grade,
   asum
   from member_tbl_02 met join
   (select custno, sum(price) asum from money_tbl_02 mot 
   group by custno
   order by asum desc) sale
   on met.custno = sale.custno ORDER BY asum desc;
-- 또는
select met.custno, custname,
   decode(met.grade, 'A', 'VIP', 'B', '일반', 'C', '직원') as grade,
   sale.asum
   from member_tbl_02 met,
   (select custno, sum(price) asum from money_tbl_02 mot 
   group by custno
   order by asum desc) sale
   where met.custno = sale.custno 
   ORDER BY total desc;

++ decode(grade, 'A', 'VIP', 'B', '일반', 'C', '직원');

-- 외부조인 : 매출이 없는 회원도 포함한다.
select met.custno, custname,
   decode(met.grade, 'A', 'VIP', 'B', '일반', 'C', '직원') as grade,
   nvl(sale.asum,0) total
from member_tbl_02 met 
LEFT OUTER join
   (select custno, sum(price) asum from money_tbl_02 mot 
   group by custno
   order by asum desc) sale
on met.custno = sale.custno 
ORDER BY total DESC ,custno;


-- 6월 19일 로그인 구현하기 위한 패스워드 컬럼 추가를 합니다.
-- 패스워드 컬럼은 해시값 64문자를 저장합니다.

alter table j_custom add password char(64);

-- twice 만 패스워드 값 저장하기
update j_custom 
set password = '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4'
where CUSTOM_ID = 'twice';


select * from j_custom;










package spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MemberDao {
	private JdbcTemplate jdbcTemplate;
	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	private static long nextId = 0;
	
	private Map<String, Member> map = new HashMap<>();
	
	public void insert(final Member member){
		//이 member인스턴스는 id없고 나머지변수만만 set되어있는 상태
		KeyHolder keyHolder = new GeneratedKeyHolder();	//자동생성키
		
		jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException{
				//파라미터로 전달받은 Connection을 이용해서 PrepareStatement생성
				PreparedStatement pstmt = 
						con.prepareStatement("insert into MEMBER (ID, EMAIL, PASSWORD, NAME, REGDATE)"
											+" values (MEMBER_SEQ.nextval, ?, ?, ?, ?)"
											, new String[] {"ID"});	//자동 생성되는 키에 대한 컬럼 목록을 지정할 때 사용된다.//"ID"부분의 키값으로 쓰인 컬럼값 저장됨
											//기존 pstmt쿼리문 ( + new String[] {"ID"} :keyHolder에 저장할 부분 )  
				//인텍스 파라미터값 생성
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4, new Timestamp(member.getRegisterDate().getTime()));
								
				//생성한 preparedStatement객체 반환
				return pstmt;
				}
			}
			, keyHolder);
		//update함수가 keyHolder에 new String[] {"ID"} => keyHolder에 결과 행중 ID열의 이름(ID)를 key(SEQ.nextVal)로 값을 value로 저장  
		
		Number keyValue = keyHolder.getKey(); //value를 가져옴
		member.setId(keyValue.longValue());//member에 id까지 set해줌
		
	}
	
	public void update(Member member){
		jdbcTemplate.update("update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?",
							member.getName(), member.getPassword(), member.getEmail());
	}
	
	
	//모든 멤버 정보를 List에 담음
	public Collection<Member> selectAll(){
		
		List<Member> results = jdbcTemplate.query("select * from MEMBER",
//				new MemberRowMapper()
				new RowMapper<Member>(){
					public Member mapRow(ResultSet rs, int rowNum)throws SQLException{
						Member member = new Member(
											rs.getString("EMAIL"),
											rs.getString("PASSWORD"),
											rs.getString("PASSWORD"),
											rs.getTimestamp("REGDATE"));
						member.setId(rs.getLong("ID"));
						return member;
					}
				}
		);
		
		return results;
	}
	

	public Member selectByEmail(String email){
		
		List<Member> result = jdbcTemplate.query("select * from MEMBER where EMAIL = ?",
				new MemberRowMapper()
//				new RowMapper<Member>(){
//					public Member mapRow(ResultSet rs, int rowNum)throws SQLException{
//						Member member = new Member(
//											rs.getString("EMAIL"),
//											rs.getString("PASSWORD"),
//											rs.getString("PASSWORD"),
//											rs.getTimestamp("REGDATE"));
//						member.setId(rs.getLong("ID"));
//						return member;
//					}
//				}		
				,email);
		//JdbcTemplate 클래스의 query메서드 
		//String으로 쿼리 문 받아 실행하고 
		//결과 ResultSet객체를 => RowMapper형태의 자바객체로 변환하여 저장한 다음 List로 반환한다.
		//List<T> query(String sql, RowMapper<t> rowMapper (,Object... args))	
		//오브젝트 인자 갯수 제한없이 넣을수있음 
		//쿼리문 + object 조합해서 완성된 쿼리문으로 동작해서 받아온 resultset객체를 mapRow메서드에 인자로 넣고 member를 반환
		//그 멤버들을 모아서 최종적으로 List<Member>반환함
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	
	
	//총 몇개의 Member가 있는지 구해오는 메서드
	public int count() {
		Integer count = jdbcTemplate.queryForObject("select count(*) from MEMBER", Integer.class);
		
		return count;
	}
	//queryForObject : 결과가 하나의 행만 가진 경우를 위한 메서드임
	// 주의! 결과행이 1행이 아니면 query메서드를 사용해야함
	
	//queryForObject(쿼리문, 컬럼을 읽어올 때의 타입 지정) //얻어오는 컬럼도 하나인경우
	//얻어오는 컬럼이 두개이상인 경우에는 RowMapper객체의 mapRow메서드 사용해서 collection에 저장해서 뽑아오면됨

	
}

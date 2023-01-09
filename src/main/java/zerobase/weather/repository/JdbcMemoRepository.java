package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired  // properties 에서 가져옴
    public JdbcMemoRepository(DataSource dataSource) {
        // datasource -> properties 파일에서 설정한 DB 관련 정보들이 담김
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Memo save(Memo memo) {
        String sql = "insert into memo values(?,?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    public List<Memo> findAll() {
        String sql = "select * from memo";
        return jdbcTemplate.query(sql, memoRowMapper());
        // 조희 -> query 함수, ResultSet으로 반환받은 값을 memoRowMapper() 함수로 Memo클래스로 변환해서 반환
    }

    public Optional<Memo> findById(int id) {
        String sql = "select * from memo where id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }

    private RowMapper<Memo> memoRowMapper() {
        // JDBC를 통해 데이터를 가져오면 `ResultSet` 형식으로 가져옴
        // {id = 1, text = 'this is memo~'}
        // rs는 ResultSet의 줄임말
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );
    }

}

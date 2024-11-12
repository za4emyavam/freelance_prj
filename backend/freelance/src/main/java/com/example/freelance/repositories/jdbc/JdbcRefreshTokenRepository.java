package com.example.freelance.repositories.jdbc;

import com.example.freelance.entities.RefreshToken;
import com.example.freelance.repositories.RefreshTokenRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcRefreshTokenRepository implements RefreshTokenRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcRefreshTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        List<RefreshToken> rToken = jdbcTemplate.query(
                "select * from refresh_token where token=?",
                this::mapToRefreshToken, token);
        return token.isEmpty() ? Optional.empty() : Optional.of(rToken.getFirst());
    }

    @Override
    public void deleteByUsername(String username) {
        jdbcTemplate.update("delete from refresh_token where username=?", username);
    }

    @Override
    public Optional<RefreshToken> findByUsername(String username) {
        List<RefreshToken> token = jdbcTemplate.query(
                "select * from refresh_token where username=?",
                this::mapToRefreshToken, username);
        return token.isEmpty() ? Optional.empty() : Optional.of(token.getFirst());
    }

    @Override
    public void delete(RefreshToken token) {
        jdbcTemplate.update("delete from refresh_token where token_id=?", token.getTokenId());
    }

    @Override
    public RefreshToken save(RefreshToken token) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into refresh_token (token, username, expiry_date) values (?, ?, ?)",
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);
        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Arrays.asList(
                        token.getToken(),
                        token.getUsername(),
                        token.getExpiryDate()
                )
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        Long id = ((Number) keyHolder.getKeys().get("GENERATED_KEYS")).longValue();
        token.setTokenId(id);

        return token;
    }

    @Override
    public RefreshToken update(RefreshToken token) {
        jdbcTemplate.update("update refresh_token set token=?, expiry_date=? where username=?",
                token.getToken(),
                token.getExpiryDate(),
                token.getUsername()
                );

        return token;
    }

    private RefreshToken mapToRefreshToken(ResultSet rs, int rowNum) throws SQLException {
        return new RefreshToken(
                rs.getLong("token_id"),
                rs.getString("token"),
                rs.getString("username"),
                rs.getTimestamp("expiry_date").toLocalDateTime()
        );
    }
}

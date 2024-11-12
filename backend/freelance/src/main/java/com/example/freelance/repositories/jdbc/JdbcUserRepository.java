package com.example.freelance.repositories.jdbc;

import com.example.freelance.entities.User;
import com.example.freelance.repositories.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "select * from \"user\" where email=:email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        return !jdbcTemplate.query(
                        sql,
                        params,
                        this::mapToUser)
                .isEmpty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        String sql = "select * from \"user\" where email=:email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        List<User> user = jdbcTemplate.query(sql, params,
                this::mapToUser);
        return user.isEmpty() ? Optional.empty() : Optional.of(user.getFirst());
    }

    @Override
    public User update(User user) {
        String sql = "update \"user\" set " +
                "name=:name, second_name=:second_name, surname=:surname, birth_date=:birth_date," +
                "gender=:gender, phone_num=:phone_num, about_me=:about_me where email=:email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("name", user.getName());
        params.put("second_name", user.getSecondName());
        params.put("surname", user.getSurname());
        params.put("birth_date", user.getBirthday());
        params.put("gender", user.getGender());
        params.put("phone_num", user.getPhoneNum());
        params.put("about_me", user.getAboutMe());
        jdbcTemplate.update(sql, params);

        return user;
    }

    @Override
    public void deactivateProfileById(Long userId) {
        String sql = "update \"user\" set status=:status where id_user=:id_user";
        Map<String, Object> params = new HashMap<>();
        params.put("status", User.UserStatus.DEACTIVATED.toString());
        params.put("id_user", userId);
        jdbcTemplate.update(
                sql,
                params);
    }

    @Override
    public boolean isUserActive(String email) {
        String sql = "select status from \"user\" where email=:email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        return User.UserStatus.valueOf(
                jdbcTemplate.queryForObject(
                        sql,
                        params,
                        String.class))
                .equals(User.UserStatus.ACTIVE);
    }

    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getLong("id_user"),
                rs.getString("email"),
                rs.getString("pass_hash"),
                rs.getString("name"),
                rs.getString("second_name"),
                rs.getString("surname"),
                rs.getTimestamp("birth_date").toLocalDateTime().toLocalDate(),
                rs.getString("gender"),
                rs.getString("phone_num"),
                rs.getString("about_me"),
                User.UserRole.valueOf(rs.getString("role")),
                rs.getTimestamp("reg_date").toLocalDateTime(),
                User.UserStatus.valueOf(rs.getString("status"))
        );
    }
}

package com.example.freelance.repositories.jdbc;

import com.example.freelance.entities.User;
import com.example.freelance.repositories.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByEmail(String email) {
        return !jdbcTemplate.query(
                        "select * from \"user\" where email=?",
                        this::mapToUser,
                        email)
                .isEmpty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        List<User> user = jdbcTemplate.query("select * from \"user\" where email=?",
                this::mapToUser,
                email);
        return user.isEmpty() ? Optional.empty() : Optional.of(user.getFirst());
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update("update \"user\" u set " +
                        "name=?, second_name=?, surname=?, birth_date=?," +
                        "gender=?, phone_num=?, about_me=? where email=?",
                user.getName(),
                user.getSecondName(),
                user.getSurname(),
                user.getBirthday(),
                user.getGender(),
                user.getPhoneNum(),
                user.getAboutMe(),
                user.getEmail());

        return user;
    }

    @Override
    public void deactivateProfileById(Long userId) {
        jdbcTemplate.update(
                "update \"user\" set status=?::user_status where id_user=?",
                User.UserStatus.DEACTIVATED.toString(),
                userId);
    }

    @Override
    public boolean isUserActive(String email) {
        return User.UserStatus.valueOf(
                jdbcTemplate.queryForObject(
                        "select status from \"user\" where email=?",
                        String.class,
                        email)).equals(User.UserStatus.ACTIVE);
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

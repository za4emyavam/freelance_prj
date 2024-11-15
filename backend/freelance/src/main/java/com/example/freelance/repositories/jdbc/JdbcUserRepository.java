package com.example.freelance.repositories.jdbc;

import com.example.freelance.entities.User;
import com.example.freelance.entities.dto.CreateUserDTO;
import com.example.freelance.repositories.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "select * from \"user\" where email=?";

        return !jdbcTemplate.query(
                        sql,
                        this::mapToUser,
                        email)
                .isEmpty();
    }

    @Override
    @Transactional
    public User save(CreateUserDTO user) {
        String createLoginSql = String.format("CREATE LOGIN [%s] WITH PASSWORD = '%s';",
                user.getEmail(), user.getPassword());

        String createUserSql = String.format("CREATE USER [%s] FOR LOGIN [%s];",
                user.getEmail(), user.getEmail());
        String addToRoleSql = String.format("ALTER ROLE %s ADD MEMBER [%s];",
                user.getRole().toLowerCase(), user.getEmail());



        jdbcTemplate.execute(createLoginSql);
        jdbcTemplate.execute(createUserSql);
        jdbcTemplate.execute(addToRoleSql);

        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into \"user\" (name, email, second_name, surname, birth_date, gender, phone_num, about_me, role) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR, Types.DATE, Types.NVARCHAR, Types.NVARCHAR, Types.NVARCHAR,
                Types.NVARCHAR
        );

        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Arrays.asList(
                        user.getName(),
                        user.getEmail(),
                        user.getSecondName(),
                        user.getSurname(),
                        user.getBirthday(),
                        user.getGender(),
                        user.getPhoneNum(),
                        user.getAboutMe(),
                        User.UserRole.valueOf(user.getRole()).toString()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        Long id = ((Number) keyHolder.getKeys().get("GENERATED_KEYS")).longValue();

        return new User(id, user.getEmail(), user.getPassword(), user.getName(),user.getSecondName(), user.getSurname(), user.getBirthday(),
                user.getGender(), user.getPhoneNum(), user.getAboutMe(), User.UserRole.valueOf(user.getRole()), null, User.UserStatus.ACTIVE);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        String sql = "select * from \"user\" where email=?";
        List<User> user = jdbcTemplate.query(sql,
                this::mapToUser, email);
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
        String sql = "update \"user\" set status=:status where id_user=?";
        jdbcTemplate.update(
                sql,
                User.UserStatus.DEACTIVATED.toString(),
                userId);
    }

    @Override
    public boolean isUserActive(String email) {
        String sql = "select status from \"user\" where email=?";
                return User.UserStatus.valueOf(
                jdbcTemplate.queryForObject(
                        sql,
                        String.class,
                        email))
                .equals(User.UserStatus.ACTIVE);
    }

    @Override
    public String getRole(String username) {
        String sql = String.format("SELECT dp2.name AS RoleName " +
                "FROM sys.database_principals AS dp " +
                "JOIN sys.database_role_members AS drm ON dp.principal_id = drm.member_principal_id " +
                "JOIN sys.database_principals AS dp2 ON dp2.principal_id = drm.role_principal_id " +
                "WHERE dp.name = '%s'", username);
        return jdbcTemplate.queryForObject(
                sql,
                String.class
        );
    }

    @Override
    public int countRegisterUsers(LocalDate from, LocalDate to) {
        return jdbcTemplate.queryForObject(
                "SELECT count(*) FROM \"user\" WHERE reg_date>= ? AND reg_date<= ?",
                Integer.class,
                from,
                to);
    }

    @Override
    public double getAverageRating() {
        return jdbcTemplate.queryForObject(
                "SELECT CAST(AVG(r.avg_rating) as DECIMAL(3,2)) AS avg_rating_all_contractors " +
                        "FROM [user] u " +
                        "CROSS APPLY get_contractor_rate(u.id_user) r " +
                        "WHERE u.role = 'CONTRACTOR'",
                Double.class
        );
    }

    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getLong("id_user"),
                rs.getString("email"),
                null,
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

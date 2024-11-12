package com.example.freelance.repositories.jdbc;

import com.example.freelance.entities.FieldOfActivity;
import com.example.freelance.repositories.FieldOfActivityRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFieldOfActivityRepository implements FieldOfActivityRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFieldOfActivityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FieldOfActivity> readAll() {
        return jdbcTemplate.query("select * from field_of_activity", this::mapToFieldOfActivity);
    }

    @Override
    public List<FieldOfActivity> readAllActive() {
        return jdbcTemplate.query("select * from field_of_activity where is_active=1",
                this::mapToFieldOfActivity);
    }

    @Override
    @Transactional
    public FieldOfActivity save(FieldOfActivity fieldOfActivity) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into field_of_activity (field) values (?)",
                Types.VARCHAR);
        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Collections.singletonList(
                        fieldOfActivity.getField()
                )
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        Long id = Long.parseLong(keyHolder.getKeys().get("id_field").toString());
        fieldOfActivity.setIdField(id);

        Boolean isActive = Boolean.parseBoolean(keyHolder.getKeys().get("is_active").toString());
        fieldOfActivity.setIsActive(isActive);
        return fieldOfActivity;
    }

    @Override
    public FieldOfActivity update(FieldOfActivity fieldOfActivity) {
        jdbcTemplate.update(
                "update field_of_activity f set field=?, is_active=? where id_field=?",
                fieldOfActivity.getField(),
                fieldOfActivity.getIsActive(),
                fieldOfActivity.getIdField()
        );
        return fieldOfActivity;
    }

    @Override
    public FieldOfActivity delete(FieldOfActivity fieldOfActivity) {
        return null;
    }

    @Override
    public Optional<FieldOfActivity> retrieveById(Long fieldId) {
        List<FieldOfActivity> field = jdbcTemplate.query(
                "select * from field_of_activity where id_field=?",
                this::mapToFieldOfActivity,
                fieldId);

        return field.isEmpty() ? Optional.empty() : Optional.of(field.getFirst());
    }

    @Override
    public Optional<FieldOfActivity> readByName(String name) {
        List<FieldOfActivity> fields = jdbcTemplate.query(
                "select * from field_of_activity where field=?",
                this::mapToFieldOfActivity,
                name);
        return fields.isEmpty() ? Optional.empty() : Optional.of(fields.getFirst());
    }

    private FieldOfActivity mapToFieldOfActivity(ResultSet rs, int rowNum) throws SQLException {
        return new FieldOfActivity(
                rs.getLong("id_field"),
                rs.getString("field"),
                rs.getBoolean("is_active")
        );
    }
}

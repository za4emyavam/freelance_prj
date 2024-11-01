package com.example.freelance.repositories.jdbc;

import com.example.freelance.entities.dto.ContractorDTO;
import com.example.freelance.entities.dto.ContractorProfileDTO;
import com.example.freelance.repositories.ContractorRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcContractorRepository implements ContractorRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcContractorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ContractorProfileDTO> findContractorById(Long id) {
        List<ContractorProfileDTO> list = jdbcTemplate.query(
                "select id_user, email, name, second_name, surname, about_me from \"user\" where id_user=?",
                this::mapToContractorDTO, id);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        ContractorProfileDTO contractorProfileDTO = list.getFirst();
        jdbcTemplate.query(
                "select * from get_contractor_rate(?::integer)",
                (resultSet, rowNum) -> {
                    contractorProfileDTO.setRating(resultSet.getDouble("avg_rating"));
                    contractorProfileDTO.setTaskCompleted(resultSet.getInt("completed_tasks_count"));
                    return null;
                }, id);

        contractorProfileDTO.setFieldRatingList(jdbcTemplate.query("select * from get_completed_tasks_by_field(?::integer)",
                this::mapToFieldRating, id));

        return Optional.of(contractorProfileDTO);
    }

    @Override
    public Optional<ContractorDTO> findContractorDTOById(Long id) {
        Optional<ContractorProfileDTO> profileDTO = findContractorById(id);
        if (profileDTO.isEmpty())
            return Optional.empty();

        ContractorDTO contractorDTO = jdbcTemplate.query(
                "select birth_date, gender, phone_num from \"user\" where id_user=?",
                (resultSet, rowNum) -> {
                    return new ContractorDTO(profileDTO.get(),
                            resultSet.getString("phone_num"),
                            resultSet.getString("gender"),
                            resultSet.getTimestamp("birth_date").toLocalDateTime().toLocalDate());
                }, id).getFirst();
        return Optional.of(contractorDTO);
    }

    private ContractorProfileDTO mapToContractorDTO(ResultSet rs, int rowNum) throws SQLException {
        return new ContractorProfileDTO(
                rs.getLong("id_user"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("second_name"),
                rs.getString("surname"),
                rs.getString("about_me"),
                0d,
                0,
                null
        );
    }

    private ContractorProfileDTO.FieldRating mapToFieldRating(ResultSet rs, int rowNum) throws SQLException {
        return new ContractorProfileDTO.FieldRating(
                rs.getLong("fieldId"),
                rs.getString("field_name"),
                rs.getInt("completed_tasks_count")
        );
    }

    /*private Long userId;
    private String email;
    private String name;
    private String secondName;
    private String surname;
    private String about;
    private Double rating;
    private Integer taskCompleted;
    private List<ContractorDTO.FieldRating> fieldRatingList;*/
}

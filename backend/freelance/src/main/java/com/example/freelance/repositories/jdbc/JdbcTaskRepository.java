package com.example.freelance.repositories.jdbc;

import com.example.freelance.entities.RemovingReason;
import com.example.freelance.entities.RespondedContractor;
import com.example.freelance.entities.Task;
import com.example.freelance.entities.TaskFile;
import com.example.freelance.entities.dto.ActiveTaskDTO;
import com.example.freelance.entities.dto.ContractorTaskDTO;
import com.example.freelance.entities.dto.CreateTaskDTO;
import com.example.freelance.entities.dto.RespondedContractorDTO;
import com.example.freelance.repositories.TaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class JdbcTaskRepository implements TaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Task> read(Long taskId) {
        List<Task> tasks = jdbcTemplate.query(
                "select * from task where id_task=?",
                this::mapToTask,
                taskId);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.getFirst());
    }

    @Override
    public Task save(CreateTaskDTO task) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into task (id_customer, id_activity_field, name, end_date, cost, description) " +
                        "values (?, ?, ?, ?, ?, ?)",
                Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP, Types.DECIMAL, Types.VARCHAR
        );

        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Arrays.asList(
                        task.getIdCustomer(),
                        task.getIdActivityField(),
                        task.getName(),
                        task.getEndDate(),
                        task.getCost(),
                        task.getDescription()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        Long id = Long.parseLong(keyHolder.getKeys().get("id_task").toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(keyHolder.getKeys().get("creation_date").toString(), formatter);

        Task createdTask = new Task();
        createdTask.setIdTask(id);
        createdTask.setIdCustomer(task.getIdCustomer());
        createdTask.setIdActivityField(task.getIdActivityField());
        createdTask.setName(task.getName());
        createdTask.setCreationDate(date.atStartOfDay());
        createdTask.setEndDate(task.getEndDate());
        createdTask.setCost(task.getCost());
        createdTask.setDescription(task.getDescription());
        createdTask.setStatus(Task.Status.valueOf(keyHolder.getKeys().get("status").toString()));

        for (String link : task.getLinks())
            createdTask.getLinks().add(saveTaskFile(id, link));

        return createdTask;
    }

    @Override
    public Task update(Task task) {
        jdbcTemplate.update(
                "update task set name=?, description=?, end_date=?, cost=?, id_activity_field=? where id_task=?",
                task.getName(),
                task.getDescription(),
                task.getEndDate(),
                task.getCost(),
                task.getIdActivityField(),
                task.getIdTask());
        return task;
    }

    @Override
    public void cancelTaskById(Long taskId) {
        jdbcTemplate.update("update task set status=?::status where id_task=?",
                Task.Status.CANCELLED.toString(),
                taskId);
    }

    @Override
    public List<Task> findAllByCustomerId(Long customerId) {
        return jdbcTemplate.query(
                "select * from task where id_customer=?",
                this::mapToTask,
                customerId);
    }

    @Override
    public List<RespondedContractorDTO> getContractorsByTask(Long taskId) {
        return jdbcTemplate.query("select * from get_contractors_by_task(?::integer)",
                this::mapToRespondedContractorDTO, taskId);
    }

    @Override
    public TaskFile saveTaskFile(Long taskId, String link) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into task_file (id_task, link) " +
                        "values (?, ?)",
                Types.INTEGER, Types.VARCHAR
        );

        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Arrays.asList(
                        taskId,
                        link
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        Long id = Long.parseLong(keyHolder.getKeys().get("id_file").toString());

        return new TaskFile(id, taskId, link);
    }

    @Override
    public List<ActiveTaskDTO> findAllActiveByField(String fieldName) {
        return jdbcTemplate.query(
                "SELECT t.id_task, t.name, u.email, t.end_date, t.creation_date, f.id_field, f.field, t.cost FROM task t, field_of_activity f, \"user\" u " +
                        "WHERE id_activity_field = f.id_field AND f.field=? AND u.id_user=t.id_customer AND t.status='ACTIVE'",
                this::mapToActiveTaskDTO,
                fieldName);
    }

    @Override
    public List<ActiveTaskDTO> findAllActiveByEndDate(LocalDate endDate) {
        return jdbcTemplate.query(
                "SELECT t.id_task, t.name, u.email, t.end_date, t.creation_date, f.id_field, f.field, t.cost FROM task t, field_of_activity f, \"user\" u " +
                        "WHERE id_activity_field = f.id_field AND u.id_user=t.id_customer AND end_date=?::date AND t.status='ACTIVE'",
                this::mapToActiveTaskDTO,
                endDate.toString());
    }

    @Override
    public List<TaskFile> findTaskFileByTask(Long taskId) {
        return jdbcTemplate.query(
                "select * from task_file where id_task=?",
                this::mapToTaskFile,
                taskId
        );
    }

    @Override
    public void acceptContractor(Long taskId, Long contractorId) {
        jdbcTemplate.update(
                "update responded_contractor set contractor_status='APPROVED'::contractor_status where id_task=? AND id_contractor=?",
                taskId,
                contractorId
        );
    }

    @Override
    public Optional<RespondedContractor> getRespondedContractor(Long taskId, Long contractorId) {
        List<RespondedContractor> contractor = jdbcTemplate.query(
                "select * from responded_contractor where id_task=? and id_contractor=?",
                this::mapToRespondedContractor,
                taskId,
                contractorId);
        return contractor.isEmpty() ? Optional.empty() : Optional.of(contractor.getFirst());
    }

    @Override
    public RemovingReason rejectAcceptedContractor(Long respondedContractorId, String reason) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into removing_reason (id_approved_contractor, reason) " +
                        "values (?, ?)",
                Types.INTEGER, Types.VARCHAR
        );

        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Arrays.asList(
                        respondedContractorId,
                        reason
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        Long id = Long.parseLong(keyHolder.getKeys().get("id_reason").toString());
        RemovingReason removedReason = new RemovingReason();
        removedReason.setIdReason(id);
        removedReason.setIdApprovedContractor(respondedContractorId);
        removedReason.setReason(reason);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        removedReason.setRecallDate(
                LocalDateTime.parse(keyHolder.getKeys().get("recall_date").toString().split("\\.")[0], formatter));


        return removedReason;
    }

    @Override
    public void rateTaskByTaskId(Long taskId, Integer rate) {
        jdbcTemplate.update(
                "update task set task_rate=? where id_task=?",
                rate,
                taskId);
    }

    @Override
    public Optional<ContractorTaskDTO> findContractorTaskById(Long taskId) {
        List<ContractorTaskDTO> list = jdbcTemplate.query(
                "select t.id_task, t.name as task_name, t.description, u.name, u.surname, u.second_name, t.end_date, " +
                        "t.creation_date, f.id_field, f.field, t.cost, u.email, u.phone_num, t.status " +
                        "from task t, field_of_activity f, \"user\" u " +
                        "WHERE id_activity_field = f.id_field AND t.id_task=? AND u.id_user=t.id_customer",
                this::mapToContractorTaskDTO,
                taskId
        );

        if (list.isEmpty())
            return Optional.empty();

        list.getFirst().setLinks(findTaskFileByTask(taskId));

        return Optional.of(list.getFirst());
    }

    @Override
    public RespondedContractor agreeToTaskByTaskIdAndContractorId(Long taskId, Long contractId) {
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into responded_contractor (id_task, id_contractor) " +
                        "values (?, ?)",
                Types.INTEGER, Types.INTEGER
        );

        factory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Arrays.asList(
                        taskId,
                        contractId
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);

        Long id = Long.parseLong(keyHolder.getKeys().get("id_resp_contractor").toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        LocalDateTime feedbackDate = LocalDateTime.parse(keyHolder.getKeys().get("feedback_date")
                .toString().split("\\.")[0], formatter);
        RespondedContractor.ContractorStatus status = RespondedContractor.ContractorStatus.valueOf(
                keyHolder.getKeys().get("contractor_status").toString()
        );

        return new RespondedContractor(id, taskId, contractId, status, feedbackDate);

    }

    @Override
    public List<ContractorTaskDTO> findAllContractorTasksByStatus(Long userId, Task.Status status) {
        List<ContractorTaskDTO> list = jdbcTemplate.query(
                "select t.id_task, t.name as task_name, t.description, u.name, u.surname, u.second_name, t.end_date," +
                        " t.creation_date, f.id_field, f.field, t.cost, u.email, u.phone_num, t.status " +
                        "from task t, field_of_activity f, \"user\" u, responded_contractor rc " +
                        "where id_activity_field = f.id_field  AND u.id_user=t.id_customer AND rc.id_contractor=(?) " +
                        "AND rc.id_task=t.id_task AND t.status=?::status " +
                        "AND rc.contractor_status='APPROVED'::contractor_status",
                this::mapToContractorTaskDTO,
                userId,
                status.toString()
        );
        for (ContractorTaskDTO contractorTaskDTO : list)
            contractorTaskDTO.setLinks(findTaskFileByTask(contractorTaskDTO.getTaskId()));

        return list;
    }

    private Task mapToTask(ResultSet rs, int rowNum) throws SQLException {
        Long taskId = rs.getLong("id_task");
        List<TaskFile> taskFiles = findTaskFileByTask(taskId);

        Timestamp tsStartDate = rs.getTimestamp("start_date");
        LocalDateTime startDate = tsStartDate == null ? null : tsStartDate.toLocalDateTime();

        Timestamp tsFinishedDate = rs.getTimestamp("finished_date");
        LocalDateTime finishedDate = tsFinishedDate == null ? null : tsFinishedDate.toLocalDateTime();

        return new Task(
                taskId,
                rs.getLong("id_customer"),
                rs.getLong("id_activity_field"),
                rs.getString("name"),
                rs.getTimestamp("creation_date").toLocalDateTime(),
                startDate,
                finishedDate,
                rs.getTimestamp("end_date").toLocalDateTime(),
                rs.getDouble("cost"),
                rs.getString("description"),
                Task.Status.valueOf(rs.getString("status")),
                rs.getInt("task_rate"),
                taskFiles
        );
    }

    private RespondedContractorDTO mapToRespondedContractorDTO(ResultSet rs, int rowNum) throws SQLException {
        return new RespondedContractorDTO(
                rs.getLong("contractor_id"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("second_name"),
                rs.getString("surname"),
                rs.getDouble("avg_rating"),
                rs.getInt("completed_tasks_count"),
                RespondedContractorDTO.ContractorStatus.valueOf(rs.getString("contractor_status"))
        );
    }

    private TaskFile mapToTaskFile(ResultSet rs, int rowNum) throws SQLException {
        return new TaskFile(
                rs.getLong("id_file"),
                rs.getLong("id_task"),
                rs.getString("link")
        );
    }

    private RespondedContractor mapToRespondedContractor(ResultSet rs, int rowNum) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("feedback_date");
        LocalDateTime feedbackDate = timestamp == null ? null : timestamp.toLocalDateTime();

        return new RespondedContractor(
                rs.getLong("id_resp_contractor"),
                rs.getLong("id_task"),
                rs.getLong("id_contractor"),
                RespondedContractor.ContractorStatus.valueOf(rs.getString("contractor_status")),
                feedbackDate
        );
    }

    private ActiveTaskDTO mapToActiveTaskDTO(ResultSet rs, int rowNum) throws SQLException {
        return new ActiveTaskDTO(
                rs.getLong("id_task"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getTimestamp("end_date").toLocalDateTime(),
                rs.getTimestamp("creation_date").toLocalDateTime(),
                rs.getLong("id_field"),
                rs.getString("field"),
                rs.getDouble("cost")
        );
    }

    private ContractorTaskDTO mapToContractorTaskDTO(ResultSet rs, int rowNum) throws SQLException {
        return new ContractorTaskDTO(
                rs.getLong("id_task"),
                rs.getString("task_name"),
                rs.getString("description"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("second_name"),
                rs.getTimestamp("end_date").toLocalDateTime(),
                rs.getTimestamp("creation_date").toLocalDateTime(),
                rs.getLong("id_field"),
                rs.getString("field"),
                rs.getDouble("cost"),
                rs.getString("email"),
                rs.getString("phone_num"),
                null,
                Task.Status.valueOf(rs.getString("status"))

        );
    }
}

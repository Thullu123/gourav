package com.gk.repository;

import com.gk.domain.Hartron_employee;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Cassandra repository for the Hartron_employee entity.
 */
@Repository
public class Hartron_employeeRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Hartron_employee> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public Hartron_employeeRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Hartron_employee.class);
        this.findAllStmt = session.prepare("SELECT * FROM hartron_employee");
        this.truncateStmt = session.prepare("TRUNCATE hartron_employee");
    }

    public List<Hartron_employee> findAll() {
        List<Hartron_employee> hartron_employeesList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Hartron_employee hartron_employee = new Hartron_employee();
                hartron_employee.setId(row.getUUID("id"));
                return hartron_employee;
            }
        ).forEach(hartron_employeesList::add);
        return hartron_employeesList;
    }

    public Hartron_employee findOne(UUID id) {
        return mapper.get(id);
    }

    public Hartron_employee save(Hartron_employee hartron_employee) {
        if (hartron_employee.getId() == null) {
            hartron_employee.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Hartron_employee>> violations = validator.validate(hartron_employee);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(hartron_employee);
        return hartron_employee;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}

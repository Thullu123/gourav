package com.gk.repository;

import com.gk.domain.Gourav;
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
 * Cassandra repository for the Gourav entity.
 */
@Repository
public class GouravRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Gourav> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public GouravRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Gourav.class);
        this.findAllStmt = session.prepare("SELECT * FROM gourav");
        this.truncateStmt = session.prepare("TRUNCATE gourav");
    }

    public List<Gourav> findAll() {
        List<Gourav> gouravsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Gourav gourav = new Gourav();
                gourav.setId(row.getUUID("id"));
                gourav.setKuchbhi(row.getString("kuchbhi"));
                return gourav;
            }
        ).forEach(gouravsList::add);
        return gouravsList;
    }

    public Gourav findOne(UUID id) {
        return mapper.get(id);
    }

    public Gourav save(Gourav gourav) {
        if (gourav.getId() == null) {
            gourav.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Gourav>> violations = validator.validate(gourav);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(gourav);
        return gourav;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}

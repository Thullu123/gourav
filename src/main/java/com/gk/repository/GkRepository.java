package com.gk.repository;

import com.gk.domain.Gk;
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
 * Cassandra repository for the Gk entity.
 */
@Repository
public class GkRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Gk> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public GkRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Gk.class);
        this.findAllStmt = session.prepare("SELECT * FROM gk");
        this.truncateStmt = session.prepare("TRUNCATE gk");
    }

    public List<Gk> findAll() {
        List<Gk> gksList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Gk gk = new Gk();
                gk.setId(row.getUUID("id"));
                gk.setName(row.getString("name"));
                return gk;
            }
        ).forEach(gksList::add);
        return gksList;
    }

    public Gk findOne(UUID id) {
        return mapper.get(id);
    }

    public Gk save(Gk gk) {
        if (gk.getId() == null) {
            gk.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Gk>> violations = validator.validate(gk);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(gk);
        return gk;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}

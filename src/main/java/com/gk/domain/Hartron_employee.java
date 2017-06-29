package com.gk.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Hartron_employee.
 */

@Table(name = "hartron_employee")
public class Hartron_employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hartron_employee hartron_employee = (Hartron_employee) o;
        if (hartron_employee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hartron_employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hartron_employee{" +
            "id=" + getId() +
            "}";
    }
}

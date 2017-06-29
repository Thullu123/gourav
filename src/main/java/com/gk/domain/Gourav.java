package com.gk.domain;

import com.datastax.driver.mapping.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Gourav.
 */

@Table(name = "gourav")
public class Gourav implements Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey
    private UUID id;

    private String kuchbhi;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getKuchbhi() {
        return kuchbhi;
    }

    public Gourav kuchbhi(String kuchbhi) {
        this.kuchbhi = kuchbhi;
        return this;
    }

    public void setKuchbhi(String kuchbhi) {
        this.kuchbhi = kuchbhi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gourav gourav = (Gourav) o;
        if (gourav.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gourav.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Gourav{" +
            "id=" + getId() +
            ", kuchbhi='" + getKuchbhi() + "'" +
            "}";
    }
}

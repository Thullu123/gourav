package com.gk.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Gk entity.
 */
public class GkDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GkDTO gkDTO = (GkDTO) o;
        if(gkDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gkDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GkDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

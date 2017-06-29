package com.gk.service.dto;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Gourav entity.
 */
public class GouravDTO implements Serializable {

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

        GouravDTO gouravDTO = (GouravDTO) o;
        if(gouravDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gouravDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GouravDTO{" +
            "id=" + getId() +
            ", kuchbhi='" + getKuchbhi() + "'" +
            "}";
    }
}

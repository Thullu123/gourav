package com.gk.service.mapper;

import com.gk.domain.*;
import com.gk.service.dto.GouravDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Gourav and its DTO GouravDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GouravMapper extends EntityMapper <GouravDTO, Gourav> {
    
    

}

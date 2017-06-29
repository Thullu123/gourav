package com.gk.service.mapper;

import com.gk.domain.*;
import com.gk.service.dto.GkDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Gk and its DTO GkDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GkMapper extends EntityMapper <GkDTO, Gk> {
    
    

}

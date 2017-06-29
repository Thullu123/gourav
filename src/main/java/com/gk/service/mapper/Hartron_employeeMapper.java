package com.gk.service.mapper;

import com.gk.domain.*;
import com.gk.service.dto.Hartron_employeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Hartron_employee and its DTO Hartron_employeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface Hartron_employeeMapper extends EntityMapper <Hartron_employeeDTO, Hartron_employee> {
    
    

}

package com.gk.service;

import com.gk.service.dto.Hartron_employeeDTO;
import java.util.List;

/**
 * Service Interface for managing Hartron_employee.
 */
public interface Hartron_employeeService {

    /**
     * Save a hartron_employee.
     *
     * @param hartron_employeeDTO the entity to save
     * @return the persisted entity
     */
    Hartron_employeeDTO save(Hartron_employeeDTO hartron_employeeDTO);

    /**
     *  Get all the hartron_employees.
     *
     *  @return the list of entities
     */
    List<Hartron_employeeDTO> findAll();

    /**
     *  Get the "id" hartron_employee.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Hartron_employeeDTO findOne(String id);

    /**
     *  Delete the "id" hartron_employee.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}

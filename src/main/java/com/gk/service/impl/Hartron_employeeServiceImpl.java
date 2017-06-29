package com.gk.service.impl;

import com.gk.service.Hartron_employeeService;
import com.gk.domain.Hartron_employee;
import com.gk.repository.Hartron_employeeRepository;
import com.gk.service.dto.Hartron_employeeDTO;
import com.gk.service.mapper.Hartron_employeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Hartron_employee.
 */
@Service
public class Hartron_employeeServiceImpl implements Hartron_employeeService{

    private final Logger log = LoggerFactory.getLogger(Hartron_employeeServiceImpl.class);

    private final Hartron_employeeRepository hartron_employeeRepository;

    private final Hartron_employeeMapper hartron_employeeMapper;

    public Hartron_employeeServiceImpl(Hartron_employeeRepository hartron_employeeRepository, Hartron_employeeMapper hartron_employeeMapper) {
        this.hartron_employeeRepository = hartron_employeeRepository;
        this.hartron_employeeMapper = hartron_employeeMapper;
    }

    /**
     * Save a hartron_employee.
     *
     * @param hartron_employeeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public Hartron_employeeDTO save(Hartron_employeeDTO hartron_employeeDTO) {
        log.debug("Request to save Hartron_employee : {}", hartron_employeeDTO);
        Hartron_employee hartron_employee = hartron_employeeMapper.toEntity(hartron_employeeDTO);
        hartron_employee = hartron_employeeRepository.save(hartron_employee);
        return hartron_employeeMapper.toDto(hartron_employee);
    }

    /**
     *  Get all the hartron_employees.
     *
     *  @return the list of entities
     */
    @Override
    public List<Hartron_employeeDTO> findAll() {
        log.debug("Request to get all Hartron_employees");
        return hartron_employeeRepository.findAll().stream()
            .map(hartron_employeeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one hartron_employee by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public Hartron_employeeDTO findOne(String id) {
        log.debug("Request to get Hartron_employee : {}", id);
        Hartron_employee hartron_employee = hartron_employeeRepository.findOne(UUID.fromString(id));
        return hartron_employeeMapper.toDto(hartron_employee);
    }

    /**
     *  Delete the  hartron_employee by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Hartron_employee : {}", id);
        hartron_employeeRepository.delete(UUID.fromString(id));
    }
}

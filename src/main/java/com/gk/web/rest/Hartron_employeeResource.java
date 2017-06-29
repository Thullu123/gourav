package com.gk.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gk.service.Hartron_employeeService;
import com.gk.web.rest.util.HeaderUtil;
import com.gk.service.dto.Hartron_employeeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Hartron_employee.
 */
@RestController
@RequestMapping("/api")
public class Hartron_employeeResource {

    private final Logger log = LoggerFactory.getLogger(Hartron_employeeResource.class);

    private static final String ENTITY_NAME = "hartron_employee";

    private final Hartron_employeeService hartron_employeeService;

    public Hartron_employeeResource(Hartron_employeeService hartron_employeeService) {
        this.hartron_employeeService = hartron_employeeService;
    }

    /**
     * POST  /hartron-employees : Create a new hartron_employee.
     *
     * @param hartron_employeeDTO the hartron_employeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hartron_employeeDTO, or with status 400 (Bad Request) if the hartron_employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hartron-employees")
    @Timed
    public ResponseEntity<Hartron_employeeDTO> createHartron_employee(@RequestBody Hartron_employeeDTO hartron_employeeDTO) throws URISyntaxException {
        log.debug("REST request to save Hartron_employee : {}", hartron_employeeDTO);
        if (hartron_employeeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hartron_employee cannot already have an ID")).body(null);
        }
        Hartron_employeeDTO result = hartron_employeeService.save(hartron_employeeDTO);
        return ResponseEntity.created(new URI("/api/hartron-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hartron-employees : Updates an existing hartron_employee.
     *
     * @param hartron_employeeDTO the hartron_employeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hartron_employeeDTO,
     * or with status 400 (Bad Request) if the hartron_employeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the hartron_employeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hartron-employees")
    @Timed
    public ResponseEntity<Hartron_employeeDTO> updateHartron_employee(@RequestBody Hartron_employeeDTO hartron_employeeDTO) throws URISyntaxException {
        log.debug("REST request to update Hartron_employee : {}", hartron_employeeDTO);
        if (hartron_employeeDTO.getId() == null) {
            return createHartron_employee(hartron_employeeDTO);
        }
        Hartron_employeeDTO result = hartron_employeeService.save(hartron_employeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hartron_employeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hartron-employees : get all the hartron_employees.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hartron_employees in body
     */
    @GetMapping("/hartron-employees")
    @Timed
    public List<Hartron_employeeDTO> getAllHartron_employees() {
        log.debug("REST request to get all Hartron_employees");
        return hartron_employeeService.findAll();
    }

    /**
     * GET  /hartron-employees/:id : get the "id" hartron_employee.
     *
     * @param id the id of the hartron_employeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hartron_employeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/hartron-employees/{id}")
    @Timed
    public ResponseEntity<Hartron_employeeDTO> getHartron_employee(@PathVariable String id) {
        log.debug("REST request to get Hartron_employee : {}", id);
        Hartron_employeeDTO hartron_employeeDTO = hartron_employeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hartron_employeeDTO));
    }

    /**
     * DELETE  /hartron-employees/:id : delete the "id" hartron_employee.
     *
     * @param id the id of the hartron_employeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hartron-employees/{id}")
    @Timed
    public ResponseEntity<Void> deleteHartron_employee(@PathVariable String id) {
        log.debug("REST request to delete Hartron_employee : {}", id);
        hartron_employeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}

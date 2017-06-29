package com.gk.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gk.service.GouravService;
import com.gk.web.rest.util.HeaderUtil;
import com.gk.service.dto.GouravDTO;
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
 * REST controller for managing Gourav.
 */
@RestController
@RequestMapping("/api")
public class GouravResource {

    private final Logger log = LoggerFactory.getLogger(GouravResource.class);

    private static final String ENTITY_NAME = "gourav";

    private final GouravService gouravService;

    public GouravResource(GouravService gouravService) {
        this.gouravService = gouravService;
    }

    /**
     * POST  /gouravs : Create a new gourav.
     *
     * @param gouravDTO the gouravDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gouravDTO, or with status 400 (Bad Request) if the gourav has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gouravs")
    @Timed
    public ResponseEntity<GouravDTO> createGourav(@RequestBody GouravDTO gouravDTO) throws URISyntaxException {
        log.debug("REST request to save Gourav : {}", gouravDTO);
        if (gouravDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gourav cannot already have an ID")).body(null);
        }
        GouravDTO result = gouravService.save(gouravDTO);
        return ResponseEntity.created(new URI("/api/gouravs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gouravs : Updates an existing gourav.
     *
     * @param gouravDTO the gouravDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gouravDTO,
     * or with status 400 (Bad Request) if the gouravDTO is not valid,
     * or with status 500 (Internal Server Error) if the gouravDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gouravs")
    @Timed
    public ResponseEntity<GouravDTO> updateGourav(@RequestBody GouravDTO gouravDTO) throws URISyntaxException {
        log.debug("REST request to update Gourav : {}", gouravDTO);
        if (gouravDTO.getId() == null) {
            return createGourav(gouravDTO);
        }
        GouravDTO result = gouravService.save(gouravDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gouravDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gouravs : get all the gouravs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gouravs in body
     */
    @GetMapping("/gouravs")
    @Timed
    public List<GouravDTO> getAllGouravs() {
        log.debug("REST request to get all Gouravs");
        return gouravService.findAll();
    }

    /**
     * GET  /gouravs/:id : get the "id" gourav.
     *
     * @param id the id of the gouravDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gouravDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gouravs/{id}")
    @Timed
    public ResponseEntity<GouravDTO> getGourav(@PathVariable String id) {
        log.debug("REST request to get Gourav : {}", id);
        GouravDTO gouravDTO = gouravService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gouravDTO));
    }

    /**
     * DELETE  /gouravs/:id : delete the "id" gourav.
     *
     * @param id the id of the gouravDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gouravs/{id}")
    @Timed
    public ResponseEntity<Void> deleteGourav(@PathVariable String id) {
        log.debug("REST request to delete Gourav : {}", id);
        gouravService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}

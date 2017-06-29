package com.gk.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gk.service.GkService;
import com.gk.web.rest.util.HeaderUtil;
import com.gk.service.dto.GkDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Gk.
 */
@RestController
@RequestMapping("/api")
public class GkResource {

    private final Logger log = LoggerFactory.getLogger(GkResource.class);

    private static final String ENTITY_NAME = "gk";

    private final GkService gkService;

    public GkResource(GkService gkService) {
        this.gkService = gkService;
    }

    /**
     * POST  /gks : Create a new gk.
     *
     * @param gkDTO the gkDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gkDTO, or with status 400 (Bad Request) if the gk has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gks")
    @Timed
    public ResponseEntity<GkDTO> createGk(@Valid @RequestBody GkDTO gkDTO) throws URISyntaxException {
        log.debug("REST request to save Gk : {}", gkDTO);
        if (gkDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gk cannot already have an ID")).body(null);
        }
        GkDTO result = gkService.save(gkDTO);
        return ResponseEntity.created(new URI("/api/gks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gks : Updates an existing gk.
     *
     * @param gkDTO the gkDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gkDTO,
     * or with status 400 (Bad Request) if the gkDTO is not valid,
     * or with status 500 (Internal Server Error) if the gkDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gks")
    @Timed
    public ResponseEntity<GkDTO> updateGk(@Valid @RequestBody GkDTO gkDTO) throws URISyntaxException {
        log.debug("REST request to update Gk : {}", gkDTO);
        if (gkDTO.getId() == null) {
            return createGk(gkDTO);
        }
        GkDTO result = gkService.save(gkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gkDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gks : get all the gks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gks in body
     */
    @GetMapping("/gks")
    @Timed
    public List<GkDTO> getAllGks() {
        log.debug("REST request to get all Gks");
        return gkService.findAll();
    }

    /**
     * GET  /gks/:id : get the "id" gk.
     *
     * @param id the id of the gkDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gkDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gks/{id}")
    @Timed
    public ResponseEntity<GkDTO> getGk(@PathVariable String id) {
        log.debug("REST request to get Gk : {}", id);
        GkDTO gkDTO = gkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gkDTO));
    }

    /**
     * DELETE  /gks/:id : delete the "id" gk.
     *
     * @param id the id of the gkDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gks/{id}")
    @Timed
    public ResponseEntity<Void> deleteGk(@PathVariable String id) {
        log.debug("REST request to delete Gk : {}", id);
        gkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}

package com.gk.service;

import com.gk.service.dto.GkDTO;
import java.util.List;

/**
 * Service Interface for managing Gk.
 */
public interface GkService {

    /**
     * Save a gk.
     *
     * @param gkDTO the entity to save
     * @return the persisted entity
     */
    GkDTO save(GkDTO gkDTO);

    /**
     *  Get all the gks.
     *
     *  @return the list of entities
     */
    List<GkDTO> findAll();

    /**
     *  Get the "id" gk.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GkDTO findOne(String id);

    /**
     *  Delete the "id" gk.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}

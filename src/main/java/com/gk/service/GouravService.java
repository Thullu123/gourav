package com.gk.service;

import com.gk.service.dto.GouravDTO;
import java.util.List;

/**
 * Service Interface for managing Gourav.
 */
public interface GouravService {

    /**
     * Save a gourav.
     *
     * @param gouravDTO the entity to save
     * @return the persisted entity
     */
    GouravDTO save(GouravDTO gouravDTO);

    /**
     *  Get all the gouravs.
     *
     *  @return the list of entities
     */
    List<GouravDTO> findAll();

    /**
     *  Get the "id" gourav.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    GouravDTO findOne(String id);

    /**
     *  Delete the "id" gourav.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}

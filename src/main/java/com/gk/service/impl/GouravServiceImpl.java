package com.gk.service.impl;

import com.gk.service.GouravService;
import com.gk.domain.Gourav;
import com.gk.repository.GouravRepository;
import com.gk.service.dto.GouravDTO;
import com.gk.service.mapper.GouravMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Gourav.
 */
@Service
public class GouravServiceImpl implements GouravService{

    private final Logger log = LoggerFactory.getLogger(GouravServiceImpl.class);

    private final GouravRepository gouravRepository;

    private final GouravMapper gouravMapper;

    public GouravServiceImpl(GouravRepository gouravRepository, GouravMapper gouravMapper) {
        this.gouravRepository = gouravRepository;
        this.gouravMapper = gouravMapper;
    }

    /**
     * Save a gourav.
     *
     * @param gouravDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GouravDTO save(GouravDTO gouravDTO) {
        log.debug("Request to save Gourav : {}", gouravDTO);
        Gourav gourav = gouravMapper.toEntity(gouravDTO);
        gourav = gouravRepository.save(gourav);
        return gouravMapper.toDto(gourav);
    }

    /**
     *  Get all the gouravs.
     *
     *  @return the list of entities
     */
    @Override
    public List<GouravDTO> findAll() {
        log.debug("Request to get all Gouravs");
        return gouravRepository.findAll().stream()
            .map(gouravMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one gourav by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public GouravDTO findOne(String id) {
        log.debug("Request to get Gourav : {}", id);
        Gourav gourav = gouravRepository.findOne(UUID.fromString(id));
        return gouravMapper.toDto(gourav);
    }

    /**
     *  Delete the  gourav by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Gourav : {}", id);
        gouravRepository.delete(UUID.fromString(id));
    }
}

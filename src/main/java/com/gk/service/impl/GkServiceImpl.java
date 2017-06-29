package com.gk.service.impl;

import com.gk.service.GkService;
import com.gk.domain.Gk;
import com.gk.repository.GkRepository;
import com.gk.service.dto.GkDTO;
import com.gk.service.mapper.GkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Gk.
 */
@Service
public class GkServiceImpl implements GkService{

    private final Logger log = LoggerFactory.getLogger(GkServiceImpl.class);

    private final GkRepository gkRepository;

    private final GkMapper gkMapper;

    public GkServiceImpl(GkRepository gkRepository, GkMapper gkMapper) {
        this.gkRepository = gkRepository;
        this.gkMapper = gkMapper;
    }

    /**
     * Save a gk.
     *
     * @param gkDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GkDTO save(GkDTO gkDTO) {
        log.debug("Request to save Gk : {}", gkDTO);
        Gk gk = gkMapper.toEntity(gkDTO);
        gk = gkRepository.save(gk);
        return gkMapper.toDto(gk);
    }

    /**
     *  Get all the gks.
     *
     *  @return the list of entities
     */
    @Override
    public List<GkDTO> findAll() {
        log.debug("Request to get all Gks");
        return gkRepository.findAll().stream()
            .map(gkMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one gk by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public GkDTO findOne(String id) {
        log.debug("Request to get Gk : {}", id);
        Gk gk = gkRepository.findOne(UUID.fromString(id));
        return gkMapper.toDto(gk);
    }

    /**
     *  Delete the  gk by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Gk : {}", id);
        gkRepository.delete(UUID.fromString(id));
    }
}

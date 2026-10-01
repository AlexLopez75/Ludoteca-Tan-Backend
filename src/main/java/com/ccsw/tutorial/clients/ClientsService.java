package com.ccsw.tutorial.clients;

import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.clients.model.ClientsDto;

import java.util.List;

public interface ClientsService {
    Clients get(Long id);

    List<Clients> findAll();

    void save(Long id, ClientsDto dto);

    void delete(Long id) throws Exception;
}

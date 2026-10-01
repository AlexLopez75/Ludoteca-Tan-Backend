package com.ccsw.tutorial.clients;

import com.ccsw.tutorial.clients.model.Clients;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientsRepository extends CrudRepository<Clients, Long> {

    boolean existsByName(String name);
}

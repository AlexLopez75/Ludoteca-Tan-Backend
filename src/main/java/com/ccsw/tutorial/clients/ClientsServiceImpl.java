package com.ccsw.tutorial.clients;

import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.clients.model.ClientsDto;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientsServiceImpl implements ClientsService {

    @Autowired
    ClientsRepository clientsRepository;

    @Override
    public Clients get(Long id) {

        return this.clientsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Clients> findAll() {

        return (List<Clients>) this.clientsRepository.findAll();
    }

    @Override
    public void save(Long id, ClientsDto dto) {

        Clients clients;

        if (id == null) {
            clients = new Clients();
        } else {
            clients = this.get(id);
        }

        if (clientsRepository.existsByName(dto.getName())) {
            throw new EntityExistsException();
        }

        clients.setName(dto.getName());

        this.clientsRepository.save(clients);
    }

    @Override
    public void delete(Long id) throws Exception {

        if(this.get(id) == null){
            throw new Exception("Not exists");
        }

        this.clientsRepository.deleteById(id);
    }
}

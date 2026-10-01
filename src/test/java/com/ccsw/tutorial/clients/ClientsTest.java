package com.ccsw.tutorial.clients;

import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.clients.model.ClientsDto;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientsTest {
    @Mock
    private ClientsRepository clientsRepository;

    @InjectMocks
    private ClientsServiceImpl clientsService;

    @Test
    public void findAllShouldReturnAllCategories() {

        List<Clients> list = new ArrayList<>();
        list.add(mock(Clients.class));

        when(clientsRepository.findAll()).thenReturn(list);

        List<Clients> clients = clientsService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    public static final String Clients_NAME = "CAT1";

    @Test
    public void saveNotExistsClientsIdShouldInsert() {

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setName(Clients_NAME);

        ArgumentCaptor<Clients> clients = ArgumentCaptor.forClass(Clients.class);

        clientsService.save(null, clientsDto);

        verify(clientsRepository).save(clients.capture());

        assertEquals(Clients_NAME, clients.getValue().getName());
    }

    @Test
    public void saveExistsClientsIdWithSameNameShouldNotInsert() {

        ClientsDto dto = new ClientsDto();
        dto.setName(Clients_NAME);

        when(clientsRepository.existsByName(Clients_NAME)).thenReturn(true);

        assertThrows(EntityExistsException.class,
                () -> clientsService.save(null, dto));

        verify(clientsRepository, never()).save(any());
    }

    public static final Long EXISTS_Clients_ID = 1L;

    @Test
    public void saveExistsClientsIdShouldUpdate() {

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setName(Clients_NAME);

        Clients clients = mock(Clients.class);
        when(clientsRepository.findById(EXISTS_Clients_ID)).thenReturn(Optional.of(clients));

        clientsService.save(EXISTS_Clients_ID, clientsDto);

        verify(clientsRepository).save(clients);
    }



    @Test
    public void deleteExistsClientsIdShouldDelete() throws Exception {

        Clients clients = mock(Clients.class);
        when(clientsRepository.findById(EXISTS_Clients_ID)).thenReturn(Optional.of(clients));

        clientsService.delete(EXISTS_Clients_ID);

        verify(clientsRepository).deleteById(EXISTS_Clients_ID);
    }

    public static final Long NOT_EXISTS_Clients_ID = 0L;

    @Test
    public void getExistsClientsIdShouldReturnClients() {

        Clients clients = mock(Clients.class);
        when(clients.getId()).thenReturn(EXISTS_Clients_ID);
        when(clientsRepository.findById(EXISTS_Clients_ID)).thenReturn(Optional.of(clients));

        Clients ClientsResponse = clientsService.get(EXISTS_Clients_ID);

        assertNotNull(ClientsResponse);
        assertEquals(EXISTS_Clients_ID, clients.getId());
    }

    @Test
    public void getNotExistsClientsIdShouldReturnNull() {

        when(clientsRepository.findById(NOT_EXISTS_Clients_ID)).thenReturn(Optional.empty());

        Clients clients = clientsService.get(NOT_EXISTS_Clients_ID);

        assertNull(clients);
    }

}
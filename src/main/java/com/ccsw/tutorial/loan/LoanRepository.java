package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {

    /**
     * Método para recuperar un listado paginado de {@link Loan}
     *
     * @param pageable pageable
     * @return {@link Page} de {@link Loan}
     */
    Page<Loan> findAll(Pageable pageable);

    /**
     * Método para buscar un Game por su Id de {@link Loan}
     *
     * @param gameId Long
     */
    List<Loan> findByGameId(Long gameId);

    /**
     * Método para buscar un Clients por su Id de {@link Loan}
     *
     * @param clientId Long
     */
    List<Loan> findByClientId(Long clientId);
}

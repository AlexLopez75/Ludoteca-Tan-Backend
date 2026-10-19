package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsRepository;
import com.ccsw.tutorial.common.exceptions.BusinessException;
import com.ccsw.tutorial.game.GameRepository;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private GameRepository gameRepository;

    private BusinessException businessException;

    @Override
    public Loan get(Long id) {

        return this.loanRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {

        Specification<Loan> specification = Specification.allOf();

        if (dto.getGameId() != null) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.equal(root.get("game").get("id"), dto.getGameId())
            );
        }

        if (dto.getClientId() != null) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.equal(root.get("client").get("id"), dto.getClientId())
            );
        }

        if (dto.getSearchDate() != null) {

            specification = specification.and(
                    (root, query, cb) ->
                            cb.and(
                                    cb.lessThanOrEqualTo(
                                            root.get("startDate"),
                                            dto.getSearchDate()
                                    ),
                                    cb.greaterThanOrEqualTo(
                                            root.get("endDate"),
                                            dto.getSearchDate()
                                    )
                            )
            );
        }

        return loanRepository.findAll(specification,dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, LoanDto data) {

        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.get(id);
        }

        long days = ChronoUnit.DAYS.between(data.getStartDate(), data.getEndDate());
        LocalDate day = data.getStartDate();
        int maxDays = 14;

        if (data.getEndDate().isBefore(data.getStartDate())) {
            throw new BusinessException(
                    "END_BEFORE_START",
                    "End date cannot be before start date");
        }

        if (days > maxDays) {
            throw new BusinessException(
                    "TOO_MANY_DAYS",
                    "Loan cannot exceed 14 days");
        }

        List<Loan> gameLoans = loanRepository.findByGameId(data.getGameId());
        for (Loan existing : gameLoans) {

            if (id != null && existing.getId().equals(id)) {
                continue;
            }

            boolean overlap = !data.getEndDate().isBefore(existing.getStartDate()) && !data.getStartDate().isAfter(existing.getEndDate());

            if (overlap) {
                throw new BusinessException(
                        "ALREADY_LOANED",
                        "Game already loaned");
            }
        }

        List<Loan> clientLoans = loanRepository.findByClientId(data.getClientId());
        while (!day.isAfter(data.getEndDate())) {

            int activeLoans = 0;

            for (Loan existing : clientLoans) {

                if (id != null && existing.getId().equals(id)) {
                    continue;
                }

                boolean active = !day.isBefore(existing.getStartDate()) && !day.isAfter(existing.getEndDate());

                if (active) {
                    activeLoans++;
                }
            }

            if (activeLoans >= 2) {
                throw new BusinessException(
                        "MORE_THAN_TWO_LOANS",
                        "Client cannot have more than 2 simultaneous loans");
            }

            day = day.plusDays(1);
        }

        loan.setClient(clientsRepository.findById(data.getClientId()).orElseThrow());
        loan.setGame(gameRepository.findById(data.getGameId()).orElseThrow());

        loan.setStartDate(data.getStartDate());
        loan.setEndDate(data.getEndDate());

        this.loanRepository.save(loan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.loanRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loan> findAll() {

        return (List<Loan>) this.loanRepository.findAll();
    }
}
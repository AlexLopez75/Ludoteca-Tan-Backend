package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsRepository;
import com.ccsw.tutorial.game.GameRepository;
import com.ccsw.tutorial.loan.LoanRepository;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Override
    public Loan get(Long id) {

        return this.loanRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {

        return this.loanRepository.findAll(dto.getPageable().getPageable());
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

        if (data.getEndDate().isBefore(data.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        if (days > 14) {
            throw new RuntimeException("Loan cannot exceed 14 days");
        }

        List<Loan> gameLoans = loanRepository.findByGameId(data.getGameId());
        for (Loan existing : gameLoans) {

            if (id != null && existing.getId().equals(id)) {
                continue;
            }

            boolean overlap = !data.getEndDate().isBefore(existing.getStartDate()) && !data.getStartDate().isAfter(existing.getEndDate());

            if (overlap) {
                throw new RuntimeException("Game already loaned");
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
                throw new RuntimeException("Client cannot have more than 2 simultaneous loans");
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
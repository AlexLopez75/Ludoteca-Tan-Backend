package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.LoanService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar un listado paginado de {@link Loan}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link LoanDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a page of loans")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findPage(@RequestBody LoanSearchDto dto) {

        Page<Loan> page = this.loanService.findPage(dto);

        return new PageImpl<>(
                page.getContent().stream().map(e -> {

                    LoanDto loanDto = new LoanDto();

                    loanDto.setId(e.getId());

                    loanDto.setClientId(e.getClient().getId());
                    loanDto.setClientName(e.getClient().getName());

                    loanDto.setGameId(e.getGame().getId());
                    loanDto.setGameName(e.getGame().getTitle());

                    loanDto.setStartDate(e.getStartDate());
                    loanDto.setEndDate(e.getEndDate());

                    return loanDto;

                }).collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements()
        );
    }

    /**
     * Método para crear o actualizar un {@link Loan}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a loan")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) {

        this.loanService.save(id, dto);
    }

    /**
     * Método para crear o actualizar un {@link Loan}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.loanService.delete(id);
    }

    /**
     * Recupera un listado de autores {@link Loan}
     *
     * @return {@link List} de {@link LoanDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of loans")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<LoanDto> findAll() {

        List<Loan> Loans = this.loanService.findAll();

        return Loans.stream().map(e -> {
            LoanDto dto = new LoanDto();

            dto.setId(e.getId());

            dto.setClientId(e.getClient().getId());
            dto.setClientName(e.getClient().getName());

            dto.setGameId(e.getGame().getId());
            dto.setGameName(e.getGame().getTitle());

            dto.setStartDate(e.getStartDate());
            dto.setEndDate(e.getEndDate());

            return dto;}).collect(Collectors.toList());
    }
}
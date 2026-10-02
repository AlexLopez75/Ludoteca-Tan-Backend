package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    public static final Long DELETE_LOAN_ID = 6L;
    public static final Long MODIFY_LOAN_ID = 3L;
    public static final Long NEW_CLIENT_ID = 1L;
    public static final Long NEW_GAME_ID = 1L;

    public static final String NEW_CLIENT_NAME = "Jerry";
    public static final String NEW_GAME_NAME = "On Mars";

    public static final LocalDate NEW_START_DATE = LocalDate.parse("2026-07-01");
    public static final LocalDate NEW_END_DATE = LocalDate.parse("2026-07-10");

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>(){};

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {

        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {

        long newLoanId = TOTAL_LOANS + 1;
        long newLoanSize = TOTAL_LOANS + 1;

        LoanDto dto = new LoanDto();
        dto.setClientId(NEW_CLIENT_ID);
        dto.setGameId(NEW_GAME_ID);
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, (int) newLoanSize));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoanSize, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(newLoanId)).findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(NEW_CLIENT_ID, loan.getClientId());
        assertEquals(NEW_GAME_ID, loan.getGameId());

        assertEquals(NEW_CLIENT_NAME, loan.getClientName());
        assertEquals(NEW_GAME_NAME, loan.getGameName());

        assertEquals(NEW_START_DATE, loan.getStartDate());
        assertEquals(NEW_END_DATE, loan.getEndDate());
    }

    @Test
    public void modifyWithExistIdShouldModifyLoan() {

        LoanDto dto = new LoanDto();
        dto.setClientId(NEW_CLIENT_ID);
        dto.setGameId(NEW_GAME_ID);
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());

        LoanDto loan = response.getBody().getContent().stream().filter(item -> item.getId().equals(MODIFY_LOAN_ID)).findFirst().orElse(null);
        assertNotNull(loan);
        assertEquals(NEW_CLIENT_ID, loan.getClientId());
        assertEquals(NEW_GAME_ID, loan.getGameId());

        assertEquals(NEW_CLIENT_NAME, loan.getClientName());
        assertEquals(NEW_GAME_NAME, loan.getGameName());

        assertEquals(NEW_START_DATE, loan.getStartDate());
        assertEquals(NEW_END_DATE, loan.getEndDate());
    }

    @Test
    public void modifyWithNotExistIdShouldThrowException() {

        long loanId = TOTAL_LOANS + 1;

        LoanDto dto = new LoanDto();
        dto.setClientId(NEW_CLIENT_ID);
        dto.setGameId(NEW_GAME_ID);
        dto.setStartDate(NEW_START_DATE);
        dto.setEndDate(NEW_END_DATE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + loanId, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteCategory() {

        long newLoanSize = TOTAL_LOANS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newLoanSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteLoanId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteLoanId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    ParameterizedTypeReference<List<LoanDto>> responseTypeList = new ParameterizedTypeReference<List<LoanDto>>(){};

    @Test
    public void findAllShouldReturnAllLoan() {

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseTypeList);

        assertNotNull(response);
        assertEquals(TOTAL_LOANS, response.getBody().size());
    }

    @Test
    public void saveWithEndDateBeforeStartDateShouldThrowException() {

        LoanDto dto = new LoanDto();
        dto.setClientId(1L);
        dto.setGameId(1L);
        dto.setStartDate(LocalDate.parse("2026-02-20"));
        dto.setEndDate(LocalDate.parse("2026-02-10"));

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void saveWithMoreThan14DaysShouldThrowException() {

        LoanDto dto = new LoanDto();
        dto.setClientId(1L);
        dto.setGameId(1L);
        dto.setStartDate(LocalDate.parse("2026-02-01"));
        dto.setEndDate(LocalDate.parse("2026-02-16"));

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void saveWithGameAlreadyLoanedShouldThrowException() {

        LoanDto dto = new LoanDto();
        dto.setClientId(2L);
        dto.setGameId(2L);

        dto.setStartDate(LocalDate.parse("2026-01-10"));
        dto.setEndDate(LocalDate.parse("2026-01-12"));

        ResponseEntity<?> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void saveWithMoreThanTwoLoansForClientShouldThrowException() {

        LoanDto dto1 = new LoanDto();
        dto1.setClientId(2L);
        dto1.setGameId(1L);
        dto1.setStartDate(LocalDate.parse("2026-07-01"));
        dto1.setEndDate(LocalDate.parse("2026-07-10"));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto1), Void.class);

        LoanDto dto2 = new LoanDto();
        dto2.setClientId(2L);
        dto2.setGameId(3L);
        dto2.setStartDate(LocalDate.parse("2026-07-01"));
        dto2.setEndDate(LocalDate.parse("2026-07-10"));

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto2), Void.class);

        LoanDto dto3 = new LoanDto();
        dto3.setClientId(2L);
        dto3.setGameId(5L);
        dto3.setStartDate(LocalDate.parse("2026-07-05"));
        dto3.setEndDate(LocalDate.parse("2026-07-08"));

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto3), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

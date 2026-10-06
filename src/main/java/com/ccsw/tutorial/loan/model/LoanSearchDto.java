package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

import java.time.LocalDate;

public class LoanSearchDto {

    private Long gameId;
    private Long clientId;
    private LocalDate searchDate;

    private PageableRequest pageable;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) { this.gameId = gameId; }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) { this.clientId = clientId; }

    public LocalDate getSearchDate () {
        return searchDate;
    }

    public void setSearchDate(LocalDate searchDate) { this.searchDate = searchDate; }

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }
}

package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.game.model.Game;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

public class LoanDto {

    private Long id;

    private Long clientId;
    private String clientName;

    private Long gameId;
    private String gameName;

    private LocalDate startDate;

    private LocalDate endDate;

    /**
     * @return id
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * @return clientId
     */
    public Long getClientId() {

        return this.clientId;
    }

    /**
     * @param clientId new value of {@link #getClientId()}.
     */
    public void setClientId(Long clientId) {

        this.clientId = clientId;
    }

    /**
     * @return clientName
     */
    public String getClientName() {

        return this.clientName;
    }

    /**
     * @param clientName new value of {@link #getClientName()}.
     */
    public void setClientName(String clientName) {

        this.clientName = clientName;
    }

    /**
     * @return gameId
     */
    public Long getGameId() {

        return this.gameId;
    }

    /**
     * @param gameId new value of {@link #getGameId()}.
     */
    public void setGameId(Long gameId) {

        this.gameId = gameId;
    }

    /**
     * @return gameName
     */
    public String getGameName() {

        return this.gameName;
    }

    /**
     * @param gameName new value of {@link #getGameName()}.
     */
    public void setGameName(String gameName) {

        this.gameName = gameName;
    }

    /**
     * @return start_date
     */
    public LocalDate getStartDate() {

        return this.startDate;
    }

    /**
     * @param startDate new value of {@link #getStartDate()}.
     */
    public void setStartDate(LocalDate startDate) {

        this.startDate = startDate;
    }

    /**
     * @return endDate
     */
    public LocalDate getEndDate() {

        return this.endDate;
    }

    /**
     * @param endDate new value of {@link #getEndDate()}.
     */
    public void setEndDate(LocalDate endDate) {

        this.endDate = endDate;
    }

}

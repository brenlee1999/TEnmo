package com.techelevator.tenmo.model;

import com.techelevator.tenmo.services.UserService;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;

@NotNull
public class Transfer {
    private int transferId;
    UserService userService = new UserService();

    public Transfer(){

    }
    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal transferAmount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.transferAmount = transferAmount;
    }

    private int transferTypeId;
    private int transferStatusId;
    private int accountFromId;
    private int accountToId;
    private BigDecimal transferAmount;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String toString(){
        String userDetails = null;
        //userDetails = getTransferId() + "             From: " + userService.getUserById(getAccountFromId()) + "             To:" + userService.getUserById(accountToId) + "\n";
        return userDetails;
    }
}

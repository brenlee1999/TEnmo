package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> getPastTransfersFromUserID();

    void createTransfer(Transfer transfer);
    void update(Transfer transfer);
    int getMaxID();
}

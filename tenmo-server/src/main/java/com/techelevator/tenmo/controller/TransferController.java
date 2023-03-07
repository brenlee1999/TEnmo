package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfer")
//@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;


    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @PostMapping
    public void createTransfer(@RequestBody Transfer transfer) {
        transferDao.createTransfer(transfer);
    }

    @PutMapping
    public void update(@RequestBody Transfer transfer) {
        transferDao.update(transfer);
    }

    @GetMapping
    public List<Transfer> getPastTransfersFromUserID() {
        return transferDao.getPastTransfersFromUserID();
    }

    public int getMaxID(){
        return transferDao.getMaxID();
    }
}

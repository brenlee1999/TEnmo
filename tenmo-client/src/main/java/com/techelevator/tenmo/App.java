package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private final UserService userService = new UserService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            userService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
            accountService.setAuthToken(currentUser.getToken());
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        System.out.println("Account Balance: " + accountService.getAccountBalance(currentUser.getUser().getId()));

    }

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        List<Transfer> transfers = new ArrayList<>();
        transfers = transferService.getListOfTransfers(currentUser.getUser().getId());
        System.out.println("-------------------------------------------\n" +
                        "Transfers\n"+
                "ID                     From/To                 Amount\n" +
                        "-------------------------------------------"
        );
        for (int i = 0; i < transfers.size(); i++){
            System.out.println(transfers.get(i).getTransferId() + "                 From: " + transfers.get(i).getAccountFromId() + " To: "+ transfers.get(i).getAccountToId() + "                    $" + transfers.get(i).getTransferAmount());
        }
	}
    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        //Optional
        System.out.println("not implemented");
    }

	private void sendBucks() {
		// TODO Auto-generated method stub

        // used when we list out our snacks
        System.out.println("\n" + "╔.★. .═════════════════════════════╗");
        System.out.println("       List Of All Our Users");
        System.out.println("╚═════════════════════════════. .★.╝"+ "\n");
        int recipientId = 0;
        System.out.println(userService.getListofUsers().toString());
        System.out.print("Please enter recipient: ");
        Scanner input = new Scanner(System.in);
        recipientId = input.nextInt();
        while (recipientId != currentUser.getUser().getId() && recipientId != 0 && userService.getListofUsers().contains(recipientId)) {
            if (recipientId == currentUser.getUser().getId())
                System.out.println("You can't send money to yourself. Please try again.");
            else if (recipientId == 0 || !userService.getListofUsers().contains(recipientId))
                System.out.println("Please enter a valid userID and try again.");
            else
                System.out.println("UserID received.");
        }
        transferService.transferMoney(currentUser.getUser().getId(), recipientId);
	}
    private void requestBucks() {
        // TODO Auto-generated method stub
        //Optional
        System.out.println("not implemented");
    }

}

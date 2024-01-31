package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    public UserService userService = new UserService(API_BASE_URL, currentUser);
    public TransferService transferService = new TransferService();
    private final AccountService accountService = new AccountService(API_BASE_URL, currentUser);
    private final TransferStatusService transferStatusService = new TransferStatusService(API_BASE_URL, currentUser);
    private final TransferTypeService transferTypeService = new TransferTypeService(API_BASE_URL, currentUser);
    //added for code readability
//    private final int CURRENT_USER_ID = userService.currentUser.getUser().getId();
//    private final int CURRENT_USER_ACCOUNT_ID = accountService.getAccountByUserID(CURRENT_USER_ID).getAccountID();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
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

        if (currentUser != null) {
            accountService.setAuthToken(currentUser.getToken());
            userService.setAuthToken(currentUser.getToken());
            transferStatusService.setAuthToken(currentUser.getToken());
            transferTypeService.setAuthToken(currentUser.getToken());
        } else {
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
        //Retrieves and displays the user's current balance
        BigDecimal balance = accountService.getBalance(currentUser.getUser().getId());
        System.out.println("Your current account balance is: $" + balance);
		
	}

	private void viewTransferHistory() {
        System.out.println("-------------------------------------------------------------------");
        System.out.printf("                       Transfer History%n");
        System.out.printf("--------------------------------------------------------------------%n");
        System.out.printf("| %-15s | %-15s | %-13s | %-13s %n", "  Transaction", "    Account", "   Account", "   Amount");
        System.out.printf("| %-15s | %-15s | %-13s | %-13s %n", "      ID", "    From/To", "   Username", " Transferred");
        System.out.printf("--------------------------------------------------------------------%n");

        // Variables for conditions in next for-each loop.
        int currentAccountId = accountService.getAccountByUserID(currentUser.getUser().getId()).getAccountID();
        Transfer[] transfers = transferService.getTransferByAccountId(currentAccountId);

        //Iterates through list to print transfer_id, retrieves and prints username, and prints amount.
        for(Transfer transfer: transfers) {
            Account account = accountService.getAccountByAccountId(transfer.getAccountTo());
            Account accountFrom = accountService.getAccountByAccountId(transfer.getAccountFrom());
            User toUser = userService.getUserById(account.getUserID());
            User fromUser = userService.getUserById(accountFrom.getUserID());
            String toUserName = toUser.getUsername();
            String fromUserName = fromUser.getUsername();


            // if account_from = user  SENT TO         if account_to = user    RECEIVED FROM
            if (currentAccountId == transfer.getAccountFrom()) {
//                System.out.printf("| %-10s | %-8s | %4s |%n", "Floating", "double",  "0064"); "Transaction ID", "From/To", "Account", "Amount"
//                System.out.println("   " + transfer.getTransferId() + "       " + "Sent To" + "       " + toUserName + "      " + transfer.getAmount());
                System.out.printf("| %-4s %-10d | %-15s | %1s %-11s | %1s %.2f       %n", "   ", transfer.getTransferId(), "    Sent To", "", toUserName, " $", transfer.getAmount());
            } else {
                System.out.printf("| %-4s %-10d | %-15s | %1s %-11s | %1s %.2f       %n", "   ", transfer.getTransferId(), " Received From", "", fromUserName, " $", transfer.getAmount());
//                System.out.println("   " + transfer.getTransferId() + "     Received From   " + fromUserName + "     " + transfer.getAmount());
            }
        }
        System.out.println("--------------------------------------------------------------------");
        //prompt for transaction id
        int transferId = consoleService.promptForTransferId("Enter transaction id to view additional details (0 to cancel): ", transfers);

        //If user enters 0 instead of a transfer_id, this returns user to the main menu.
        if (transferId == 0){
            System.out.println("Returning to main menu.");
            return;
        }

        Transfer detailedTransaction = transferService.getTransferById(transferId);
        Account account = accountService.getAccountByAccountId(detailedTransaction.getAccountTo());
        Account accountFrom = accountService.getAccountByAccountId(detailedTransaction.getAccountFrom());
        User toUser = userService.getUserById(account.getUserID());
        User fromUser = userService.getUserById(accountFrom.getUserID());
        String toUserName = toUser.getUsername();
        String fromUserName = fromUser.getUsername();

        System.out.printf("-------------------------------------------------------------%n");
        System.out.println("Transfer Details \n");
        System.out.println("Transaction Id: " + detailedTransaction.getTransferId());
        System.out.println("Type: " + transferTypeService.getTransferTypeDesc(detailedTransaction.getTransferTypeId()));
        System.out.println("Status: " + transferStatusService.getTransferStatusDesc(detailedTransaction.getTransferStatusId()));
        System.out.println("From: " + fromUserName);
        System.out.println("To: " + toUserName);
        System.out.println("Amount: $" + detailedTransaction.getAmount());

        System.out.println("-------------------------------------------------");

        //Option to go back to transaction history
        int menuSelection = consoleService.returnToTransferHistoryMenu();

        if (menuSelection == 1){
            viewTransferHistory();
        }

        if (menuSelection == 0){
            return;
        }

        }


	private void viewPendingRequests() {
        //Header print out. Will likely move this to console service.
        System.out.println("-------------------------------------------\n" +
                "Pending Transfers\n" +
                "ID:             To:             Amount:\n" +
                "-------------------------------------------");

        // Variables for conditions in next for-each loop.
        int fromAccountId = accountService.getAccountByUserID(currentUser.getUser().getId()).getAccountID();
        Transfer[] transfers = transferService.getTransferByAccountId(fromAccountId);


        //Method with for-each loop ensures only transfers from the currentUser, that are requests and pending, are returned in list.
        Transfer[] transferRequest = transferService.transferRequests(transfers, fromAccountId);

        //Iterates through list to print transfer_id, retrieves and prints username, and prints amount.
        for(Transfer transfer: transferRequest) {
            Account account = accountService.getAccountByAccountId(transfer.getAccountTo());
            User toUser = userService.getUserById(account.getUserID());
            String toUserName = toUser.getUsername();
            System.out.println(transfer.getTransferId() + "            " + toUserName + "            $ " + transfer.getAmount());
        }

        //Footer, will probably move to ConsoleService
        System.out.println("-------------------------------------------");

        //Prompts user for transfer_id to approve/reject, checks if transfer_id is valid, doesn't allow invalid id's and stops users
        //from approving/rejecting other user's transfers. If transfer_id equals 0, returns user to main menu.
        int transferId = consoleService.promptForTransferId("Please enter transfer ID to approve/reject (0 to cancel): ", transferRequest);

        //If user enters 0 instead of a transfer_id, this returns user to the main menu.
        if (transferId == 0){
            System.out.println("Returning to main menu.");
            return;
        }

        //Retrieves transfer with input transfer_id.
        Transfer targetTransfer = transferService.getTransferById(transferId);

        //Menu print out, returns selection number.
        int menuSelection = consoleService.requestMenu();

        //If user picks 0, it returns them to the list of pending transfers.
        if (menuSelection == 0){
            System.out.println("Returning to pending transfers.");
            viewPendingRequests();
        }

        //If user picks 2, transfer status is changed to rejected.
        if (menuSelection == 2){
            transferService.updateTransfer(targetTransfer, 3);
            System.out.println("Transfer rejected.");
        }


        Account currentUserAccount = accountService.getAccountByUserID(currentUser.getUser().getId());
        if (menuSelection == 1){
            boolean isTransferAllowed;
            if (targetTransfer.getAmount().compareTo(currentUserAccount.getBalance()) > 0){
                System.out.println("Insufficient funds. Please try again.\n");
                viewPendingRequests();
            }

            if(targetTransfer.getAmount().compareTo(currentUserAccount.getBalance()) <= 0) {
                transferService.updateTransfer(targetTransfer, 2);
                accountService.updateBalances(targetTransfer);
                System.out.println("Your transfer was successful!");
                viewCurrentBalance();
            }
        }




    }

    private void sendBucks(){
        consoleService.printSendBucksMenu();

        //Print users
        consoleService.printUsers(userService.listUsers(), currentUser.getUser().getId());

        //prompt for recipient id
        int id = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):");

        //check that given id is valid
        User recipient = new User();
        if(id == 0) {
            System.out.println("Transfer cancelled.");
            mainMenu();
        }else if((userService.getUserById(id) == null)||(id == currentUser.getUser().getId())){
            System.out.println("Invalid entry, please select from list");
            sendBucks();
        }else{
            recipient = userService.getUserById(id);
        }

        //prompt for amount to be transferred
        BigDecimal amount = null;
        amount = consoleService.promptForBigDecimal("Enter amount:");

        //check that amount is valid
        BigDecimal currentUserBalance = accountService.getBalance(currentUser.getUser().getId());
        if((amount.compareTo(BigDecimal.ZERO) <= 0 )){
            System.out.println("Invalid amount, please try again.");
            sendBucks();
        }else if(currentUserBalance.compareTo(amount) <0 ){
            System.out.println("Insufficient funds, please try again.");
            sendBucks();
        }

        //update currentUser and recipient account balances
        Account senderAccount = accountService.getAccountByUserID(currentUser.getUser().getId());
        Account recipientAccount = accountService.getAccountByUserID(recipient.getId());
        accountService.decreaseBalance(senderAccount, amount);
        accountService.updateBalance(senderAccount);
        accountService.increaseBalance(recipientAccount, amount);
        accountService.updateBalance(recipientAccount);
        //testing
//        System.out.println(" New sender balance: " + accountService.getBalance(currentUser.getUser().getId()));
        //This would show the user's current balance -Renny
        //viewCurrentBalance();

        //In banking apps we usually can't see the balance in another user's account due to privacy. -Renny
//        System.out.println(" New recipient balance: " + accountService.getBalance(recipient.getId()));

        //create new transfer object
        Transfer thisTransfer = transferService.createSendBucksTransferObject(senderAccount.getAccountID(), recipientAccount.getAccountID(), amount);
        transferService.createSendBucksTransfer(thisTransfer);
        
    }


	private void requestBucks() {
        // Prints list of users to request from by User ID and username
        consoleService.printRequestUserList(userService.listUsers(), currentUser.getUser().getId());

        // Prompts user to enter the user ID of the user they wish to request from.
        int requestID = consoleService.promptForInt("Please enter User ID of user you wish to request from or enter '0'" +
                " to cancel request: ");

        //Boolean exists to check if user is trying to input their own user ID.
        boolean isItCurrentUser = userService.checkThatRequestIDIsNotCurrentUserID(requestID, currentUser.getUser().getId());

        //If the isItCurrentUser boolean returns true, the user is prompted to enter a different ID until the boolean
        //returns false.
        while (isItCurrentUser){
            System.out.println("Sorry, we cannot process requests made to your own user ID.");
            requestID = consoleService.promptForInt("Please enter User ID of user you wish to request from or enter '0'" +
                    " to cancel request: ");
            isItCurrentUser = userService.checkThatRequestIDIsNotCurrentUserID(requestID, currentUser.getUser().getId());
        }

        //Boolean exists to check to see if the user ID inputted exists in the database.
        boolean userExists = userService.doesUserExistByID(requestID);

        //If userExists boolean returns false, the user is prompted to enter a different ID until it returns true.
        while (!userExists){
            System.out.println("User not found.");
            requestID = consoleService.promptForInt("Please enter User ID of user you wish to request from or enter '0'" +
                    " to cancel request: ");
            userExists = userService.doesUserExistByID(requestID);
        }

        //Prompts user to enter an amount to request. The method promptForRequestAmount checks to see if the user has entered
        //a zero, a null/invalid decimal, or if a negative. If the user inputs an invalid decimal or a negative number,
        //the user is prompted to try again.
        BigDecimal requestAmount = consoleService.promptForRequestAmount("Please enter amount of bucks you wish to " +
                "request or enter '0' to cancel request:");

        //If the user inputs a 0 as a request amount, it will exit the method and return the user to the main menu.
        if (requestAmount.signum() == 0){return;}

        //Gets the accountIDs needed to create TransferRequest by first getting the account and then calling the getAccountID method.
        int fromAccountId = accountService.getAccountByUserID(requestID).getAccountID();
        int toAccountId = accountService.getAccountByUserID(currentUser.getUser().getId()).getAccountID();

        //Makes a transfer object with all relevant info input previously in the method.
        Transfer transferRequest = transferService.createRequestBucksTransferObject(fromAccountId, toAccountId, requestAmount);

        //Sends the transfer object to the server, but does not update the balance, since requestBucks should not update
        //the balance until approved by user requested from in viewPendingTransfers method.
        transferService.requestBucks(transferRequest);
	}

}

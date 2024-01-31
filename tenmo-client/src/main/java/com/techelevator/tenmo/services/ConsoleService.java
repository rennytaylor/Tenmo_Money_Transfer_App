package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.lang.invoke.LambdaMetafactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printSendBucksMenu() {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID              Name");
        System.out.println("-------------------------------------------");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ").toLowerCase();
        String password = promptForString("Password: ").toLowerCase();
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printUsers(User[] users, int id){
        for(User user: users){
            if(user.getId() == id){
                continue;
            }
            System.out.println(user.getId() + "            " + user.getUsername());
        }
        System.out.println("-------------------------------------------");
    }



    //additions for requestBucks -Renny
    public void printRequestUserList(User[] users, int id) {
        System.out.println("-------------------------------------------\n" +
                "Users\n" +
                "ID          Name\n" +
                "-------------------------------------------");
        for(User user: users){
            if(user.getId() == id){
                continue;
            }
            System.out.println(user.getId() + "            " + user.getUsername());
        }
        System.out.println("---------");
    }

    public BigDecimal promptForRequestAmount(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                BigDecimal amount = new BigDecimal(scanner.nextLine());
                if (amount.signum() == 0){
                    return amount;
                }
                while (amount.signum() < 1 && amount.signum() != 0){
                    System.out.println("Requested amount must be greater than 0.");
                    System.out.println(prompt);
                    amount = new BigDecimal(scanner.nextLine());
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }

    }
    //Additions for viewPending -Renny
    public int promptForTransferId(String prompt, Transfer[] transfers) {
        System.out.print(prompt);
        List<Integer> transferIds = new ArrayList<>();
        for (Transfer transfer : transfers){
            transferIds.add(transfer.getTransferId());
        }
        while (true) {
            try {
                int transferId = Integer.parseInt(scanner.nextLine());
                boolean isValid = transferIds.contains(transferId);

                while (!isValid && transferId != 0) {
                    System.out.println("Please enter a valid transfer ID");
                    System.out.println(prompt);
                    transferId = Integer.parseInt(scanner.nextLine());
                    isValid = transferIds.contains(transferId);
                }

                return transferId;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public int requestMenu() {
        String menu = "1: Approve\n" +
                "2: Reject\n" +
                "0: Don't approve or reject\n" +
                "---------\n" +
                "Please choose an option:";
        System.out.print(menu);
        Set<Integer> values = Set.of(0, 1, 2);
        while (true) {
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                while (!values.contains(selection)){
                    System.out.println("Please select a valid selection\n"+
                            menu);
                    selection = Integer.parseInt(scanner.nextLine());
                } return selection;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public int returnToTransferHistoryMenu() {
        String menu = "1: Return to Transfer History\n" +
                "0: Retune to Main Menu\n" +
                "---------\n" +
                "Please choose an option:";
        System.out.print(menu);
        Set<Integer> values = Set.of(0, 1);
        while (true) {
            try {
                int selection = Integer.parseInt(scanner.nextLine());
                while (!values.contains(selection)){
                    System.out.println("Please select a valid selection\n"+
                            menu);
                    selection = Integer.parseInt(scanner.nextLine());
                } return selection;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    // end of Renny's additions -Renny


}

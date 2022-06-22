package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public class TransferService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = url;
    }

    public Transfer[] transfersList() {
        Transfer [] output = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "account/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            output = response.getBody();
            System.out.println(
                    "-------------------------------------------\r\n" +
                    "Transfers\r\n" +
                    "-------------------------------------------\r\n" +
                    "Type 1:Requests / Type 2:Delivered --------\r\n" +
                    "-------------------------------------------\r\n" +
                    "ID      Type    From/To         Amount\r\n" +
                    "-------------------------------------------\r\n");
            String fromOrTo = "";
            String name = "";
            for (Transfer i : output) {
                if (currentUser.getUser().getId() == i.getAccountFrom()) {
                    fromOrTo = "From: ";
                    name = i.getUserFrom();
                } else {
                    fromOrTo = "To: ";
                    name = i.getUserTo();
                }
                System.out.println(i.getTransferId()+ "\t" + i.getTransferTypeId() +"\t\t" + fromOrTo + name + "\t\t$" + i.getAmount());
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Please enter transfer ID to view details (0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (Integer.parseInt(input) != 0) {
                boolean foundTransferId = false;
                for (Transfer i : output) {
                    if (Integer.parseInt(input) == i.getTransferId()) {
                        Transfer temp = restTemplate.exchange(BASE_URL + "transfer/" + i.getTransferId(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
                        foundTransferId = true;
                        System.out.println(
                                "--------------------------------------------\r\n" +
                                "Transfer Details\r\n" +
                                "--------------------------------------------\r\n" +
                                " Id: "+ temp.getTransferId() + "\r\n" +
                                " From: " + temp.getUserFrom() + "\r\n" +
                                " To: " + temp.getUserTo() + "\r\n" +
                                " Amount: $" + temp.getAmount());
                    }
                }
                if (!foundTransferId) {
                    System.out.println("Not a valid transfer ID");
                }
            }
        } catch (Exception e) {
            System.out.println("No previous transfers");
        }
        return output;
    }

    public Transfer[] transfersRequestList() {
        Transfer [] output = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "request/pending/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            output = response.getBody();
            System.out.println(
                    "------------------------------------------------\r\n" +
                    "Transfers\r\n" +
                    "------------------------------------------------\r\n" +
                    "Type1:Requests/Type2:Delivered/Type3:Rejected--- \n" +
                    "------------------------------------------------\r\n" +
                    "ID      Type    From/To              Amount\r\n" +
                    "------------------------------------------------\r\n");
            String fromOrTo = "";
            String name = "";
            for (Transfer i : output) {
                if (currentUser.getUser().getId() == i.getAccountTo()) {
                    fromOrTo = "From: ";
                    name = i.getUserFrom();
                } else {
                    fromOrTo = "To: ";
                    name = i.getUserTo();
                }
                System.out.println(i.getTransferId() + "\t" + i.getTransferTypeId() +"\t\t" + fromOrTo + name + "\t\t\t$" + i.getAmount());
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Please enter transfer ID to approve/reject (0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (Integer.parseInt(input) != 0) {
                boolean foundTransferId = false;
                for (Transfer i : output) {
                    if (i.getAccountTo() != currentUser.getUser().getId()) {
                        if (Integer.parseInt(input) == i.getTransferId()) {
                            System.out.print(
                                    "-------------------------------------------\r\n" +
                                    i.getTransferId() +"\t\t" + fromOrTo + name + "\t\t$" + i.getAmount() + "\r\n" +
                                    "1: Approve\r\n" +
                                    "2: Reject\r\n" +
                                    "0: Don't approve or reject\r\n" +
                                    "--------------------------\r\n" +
                                    "Please choose an option: ");
                            try {
                                int id = 1 + Integer.parseInt(scanner.nextLine());
                                if (id != 1) {
                                    restTemplate.exchange(BASE_URL + "request/update/" + id, HttpMethod.PUT, makeTransferEntity(i), String.class).getBody();
                                    foundTransferId = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Not a valid transfer option");
                            }
                            if (!foundTransferId) {
                                System.out.println("Not a valid transfer ID");
                            }
                        }
                    } else {
                        System.out.println("You can not approve/reject your own request.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("I have your money now");
        }
        return output;
    }

    public void sendBucks() {
        User[] users = null;
        Transfer transfer = new Transfer();
        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(BASE_URL + "listusers/", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println(
                    "-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "-------------------------------------------\r\n" +
                    "ID\t\t\tName\r\n" +
                    "-------------------------------------------");
            for (User i : users) {
                if (i.getId() != currentUser.getUser().getId()) {
                    System.out.println(i.getId() + "\t\t" + i.getUsername());
                }
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Enter ID of user you are sending to (0 to cancel): ");
            transfer.setAccountTo(Integer.parseInt(scanner.nextLine()));
            transfer.setAccountFrom(currentUser.getUser().getId());
            if (transfer.getAccountTo() != 0 && transfer.getAccountTo() != transfer.getAccountFrom()) {
                System.out.print("Enter amount: ");
                if (Double.parseDouble(scanner.nextLine()) == 0) {
                    System.out.println("Zero, the number, is not accepted");
                } else {
                    try {
                        transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                    } catch (NumberFormatException e) {
                        System.out.println("Error when entering amount");
                    }
                    restTemplate.exchange(BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                    System.out.println("Successful transfer");
                }
            }else {
                System.out.println("NoNoNo, can't send money to yourself. Silly goose");
            }
        } catch (Exception e) {
            System.out.println("Lul, no go again");
        }
    }

    public void requestBucks() {
        User[] users = null;
        Transfer transfer = new Transfer();
        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(BASE_URL + "listusers/", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println(
                    "-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "-------------------------------------------\r\n" +
                    "ID\t\t\tName\r\n" +
                    "-------------------------------------------");
            for (User i : users) {
                if (i.getId() != currentUser.getUser().getId()) {
                    System.out.println(i.getId() + "\t\t" + i.getUsername());
                }
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Enter ID of user you are requesting from (0 to cancel): ");
            transfer.setAccountTo(currentUser.getUser().getId());
            transfer.setAccountFrom(Integer.parseInt(scanner.nextLine()));
            if (transfer.getAccountTo() != 0 && transfer.getAccountTo() != transfer.getAccountFrom()) {
                System.out.print("Enter amount: ");
                if (Double.parseDouble(scanner.nextLine()) == 0) {
                    System.out.println("Zero, the number, is not accepted");
                } else {
                try {
                    transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount");
                }
                restTemplate.exchange(BASE_URL + "request", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                System.out.println("Request Delivered");
                }
            } else {
                System.out.println("No can do, soldier.");
            }
        } catch (Exception e) {
            System.out.println("Nope, try again.");
        }
    }

    public User[] getUsers() {
        User[] user = null;
        try {
            user = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            for (User i : user) {
                System.out.println(i);
            }
        } catch (RestClientResponseException e) {
            System.out.println("Error getting users");
        }
        return user;
    }

    private HttpEntity<Transfer> makeTransferEntity( Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
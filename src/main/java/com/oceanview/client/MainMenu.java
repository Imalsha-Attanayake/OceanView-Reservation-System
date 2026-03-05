package com.oceanview.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;

public class MainMenu {

    private static final String BASE_URL = "http://localhost:8080/api";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("     Ocean View Reservation System    ");
        System.out.println("=====================================");

        boolean loggedIn = false;

        while (true) {
            System.out.println("\nMENU");
            System.out.println("1. Login");
            System.out.println("2. View Rooms");
            System.out.println("3. Add Reservation");
            System.out.println("4. View Reservation Details");
            System.out.println("5. Update Reservation");
            System.out.println("6. Delete Reservation");
            System.out.println("7. Help");
            System.out.println("8. Exit");
            System.out.print("Enter option: ");

            String option = sc.nextLine().trim();

            try {
                switch (option) {
                    case "1":
                        loggedIn = login(sc);
                        break;

                    case "2":
                        if (requireLogin(loggedIn)) break;
                        System.out.println(sendRequest("GET", "/rooms", null));
                        break;

                    case "3":
                        if (requireLogin(loggedIn)) break;
                        String reservationJson = buildReservationJson(sc);
                        System.out.println(sendRequest("POST", "/reservations", reservationJson));
                        break;

                    case "4":
                        if (requireLogin(loggedIn)) break;
                        System.out.print("Enter reservation ID: ");
                        String id = sc.nextLine().trim();
                        System.out.println(sendRequest("GET", "/reservations/" + id, null));
                        break;

                    case "5":
                        if (requireLogin(loggedIn)) break;
                        System.out.print("Enter reservation ID to update: ");
                        String updateId = sc.nextLine().trim();
                        String updateJson = buildReservationJson(sc);
                        System.out.println(sendRequest("PUT", "/reservations/" + updateId, updateJson));
                        break;

                    case "6":
                        if (requireLogin(loggedIn)) break;
                        System.out.print("Enter reservation ID to delete: ");
                        String deleteId = sc.nextLine().trim();
                        System.out.println(sendRequest("DELETE", "/reservations/" + deleteId, null));
                        break;

                    case "7":
                        printHelp();
                        break;

                    case "8":
                        System.out.println("Exiting system... Goodbye!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid option. Please enter a number from 1 to 8.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean requireLogin(boolean loggedIn) {
        if (!loggedIn) {
            System.out.println("Access denied. Please login first.");
            return true;
        }
        return false;
    }

    private static boolean login(Scanner sc) throws Exception {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        String json = "{ \"username\":\"" + username + "\", \"password\":\"" + password + "\" }";

        String response = sendRequest("POST", "/login", json);
        System.out.println(response);

        return response.contains("Login successful");
    }

    private static String buildReservationJson(Scanner sc) {
        System.out.print("Guest Name: ");
        String guestName = sc.nextLine().trim();

        System.out.print("Contact Number: ");
        String contactNumber = sc.nextLine().trim();

        System.out.print("Room ID: ");
        String roomId = sc.nextLine().trim();

        System.out.print("Check-in Date (YYYY-MM-DD): ");
        String checkIn = sc.nextLine().trim();

        System.out.print("Check-out Date (YYYY-MM-DD): ");
        String checkOut = sc.nextLine().trim();

        System.out.print("Total Bill: ");
        String totalBill = sc.nextLine().trim();

        return "{"
                + "\"guestName\":\"" + guestName + "\","
                + "\"contactNumber\":\"" + contactNumber + "\","
                + "\"roomId\":" + roomId + ","
                + "\"checkInDate\":\"" + checkIn + "\","
                + "\"checkOutDate\":\"" + checkOut + "\","
                + "\"totalBill\":" + totalBill
                + "}";
    }

    private static String sendRequest(String method, String endpoint, String jsonBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        if (jsonBody != null && (method.equals("POST") || method.equals("PUT"))) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes());
                os.flush();
            }
        }

        int status = conn.getResponseCode();

        BufferedReader br;
        if (status >= 200 && status < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        conn.disconnect();
        return "HTTP " + status + " -> " + response;
    }

    private static void printHelp() {
        System.out.println("\nHELP - How to use the system");
        System.out.println("1) Login with your username/password.");
        System.out.println("2) View Rooms to see available rooms and IDs.");
        System.out.println("3) Add Reservation by entering guest + booking details.");
        System.out.println("4) View Reservation Details using reservation ID.");
        System.out.println("5) Update Reservation by giving reservation ID and new details.");
        System.out.println("6) Delete Reservation to remove a booking record.");
        System.out.println("7) Exit closes the application safely.");
    }
}
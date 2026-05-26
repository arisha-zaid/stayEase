package ui;

import dao.AdminDAO;
import models.Admin;

import java.util.Scanner;

/**
 * MainMenu.java
 *
 * The very first screen the user sees.
 * Handles the login loop.
 * On success, hands off to AdminMenu.
 *
 * The login gives 3 attempts before exiting the program
 * (basic security measure for a college project).
 */
public class MainMenu {

    private Scanner  scanner;
    private AdminDAO adminDAO = new AdminDAO();

    public MainMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        printWelcomeBanner();

        int maxAttempts = 3;
        int attempts    = 0;
        boolean loggedIn = false;

        while (attempts < maxAttempts) {
            System.out.println("\n--- ADMIN LOGIN ---");
            System.out.print(" Username: ");
            String username = scanner.nextLine().trim();

            System.out.print(" Password: ");
            String password = scanner.nextLine().trim();

            // Check credentials against the database
            Admin admin = adminDAO.login(username, password);

            if (admin != null) {
                loggedIn = true;
                System.out.println("\n Login successful! Welcome, " + admin.getUsername() + ".");

                // Hand off to the main dashboard
                AdminMenu adminMenu = new AdminMenu(scanner);
                adminMenu.show();
                break;

            } else {
                attempts++;
                int remaining = maxAttempts - attempts;
                System.out.println("\n[!] Invalid username or password.");

                if (remaining > 0) {
                    System.out.println("    " + remaining + " attempt(s) remaining.");
                }
            }
        }

        if (!loggedIn) {
            System.out.println("\n Too many failed attempts. Exiting system.");
            System.out.println(" Contact your system administrator.\n");
        }
    }

    private void printWelcomeBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║                                              ║");
        System.out.println("  ║     WELCOME TO STAYEASE HOTEL SYSTEM         ║");
        System.out.println("  ║          Hotel Management Console            ║");
        System.out.println("  ║                                              ║");
        System.out.println("  ╚══════════════════════════════════════════════╝");
        System.out.println("         Default login: admin / admin123");
    }
}

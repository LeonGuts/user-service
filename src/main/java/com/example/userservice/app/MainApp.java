package com.example.userservice.app;

import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.User;
import com.example.userservice.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;


public class MainApp {
    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\nUser Service Menu:");
            System.out.println("1. Create User");
            System.out.println("2. View All Users");
            System.out.println("3. View User by ID");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    viewUserById();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        HibernateUtil.shutdown();
        System.out.println("Application exited successfully.");
    }

    private static void createUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline

        User user = new User(name, email, age);
        User savedUser = userDao.save(user);

        if (savedUser != null) {
            System.out.println("User created successfully with ID: " + savedUser.getId());
        } else {
            System.out.println("Failed to create user.");
        }
    }

    private static void viewAllUsers() {
        List<User> users = userDao.findAll();
        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\nList of Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void viewUserById() {
        System.out.print("Enter user ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // consume newline

        User user = userDao.findById(id);
        if (user != null) {
            System.out.println("User details:");
            System.out.println(user);
        } else {
            System.out.println("User not found with ID: " + id);
        }
    }

    private static void updateUser() {
        System.out.print("Enter user ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // consume newline

        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("User not found with ID: " + id);
            return;
        }

        System.out.print("Enter new name (current: " + user.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }

        System.out.print("Enter new email (current: " + user.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Enter new age (current: " + user.getAge() + "): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isEmpty()) {
            user.setAge(Integer.parseInt(ageInput));
        }

        User updatedUser = userDao.update(user);
        if (updatedUser != null) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("Failed to update user.");
        }
    }

    private static void deleteUser() {
        System.out.print("Enter user ID to delete: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // consume newline

        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("User not found with ID: " + id);
            return;
        }

        userDao.delete(user);
        System.out.println("User deleted successfully.");
    }
}
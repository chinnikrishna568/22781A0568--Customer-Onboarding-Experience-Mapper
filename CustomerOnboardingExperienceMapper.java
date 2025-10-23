import com.mongodb.client.*;
import org.bson.Document;
import java.util.Scanner;

public class CustomerOnboardingExperienceMapper {

    // Node of Doubly Linked List
    static class CustomerNode {
        String customerId;
        String name;
        String email;
        String onboardingStage;
        String feedback;
        CustomerNode prev, next;

        CustomerNode(String customerId, String name, String email, String onboardingStage, String feedback) {
            this.customerId = customerId;
            this.name = name;
            this.email = email;
            this.onboardingStage = onboardingStage;
            this.feedback = feedback;
        }
    }

    // Doubly Linked List integrated with MongoDB
    static class CustomerDLL {
        CustomerNode head, tail;
        MongoCollection<Document> collection;

        CustomerDLL(MongoCollection<Document> collection) {
            this.collection = collection;
        }

        // Load data from MongoDB into the DLL
        void loadFromDatabase() {
            FindIterable<Document> docs = collection.find();
            for (Document doc : docs) {
                String id = doc.getString("customerId");
                String name = doc.getString("name");
                String email = doc.getString("email");
                String stage = doc.getString("onboardingStage");
                String feedback = doc.getString("feedback");

                CustomerNode newNode = new CustomerNode(id, name, email, stage, feedback);
                if (head == null) head = tail = newNode;
                else {
                    tail.next = newNode;
                    newNode.prev = tail;
                    tail = newNode;
                }
            }
        }

        // Add new customer
        void addCustomer(String id, String name, String email, String stage, String feedback) {
            CustomerNode newNode = new CustomerNode(id, name, email, stage, feedback);
            if (head == null) head = tail = newNode;
            else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }

            Document doc = new Document("customerId", id)
                    .append("name", name)
                    .append("email", email)
                    .append("onboardingStage", stage)
                    .append("feedback", feedback);
            collection.insertOne(doc);

            System.out.println("✅ Customer added successfully!");
        }

        // Display all customers
        void displayCustomers() {
            if (head == null) {
                System.out.println("No customers found.");
                return;
            }

            CustomerNode current = head;
            System.out.println("\n--- Customer Onboarding List ---");
            while (current != null) {
                System.out.println("ID: " + current.customerId +
                        ", Name: " + current.name +
                        ", Email: " + current.email +
                        ", Stage: " + current.onboardingStage +
                        ", Feedback: " + current.feedback);
                current = current.next;
            }
        }

        // Search by customerId
        CustomerNode searchCustomer(String id) {
            CustomerNode current = head;
            while (current != null) {
                if (current.customerId.equals(id)) return current;
                current = current.next;
            }
            return null;
        }

        // Update customer details
        void updateCustomer(String id, String newStage, String newFeedback) {
            CustomerNode customer = searchCustomer(id);
            if (customer != null) {
                customer.onboardingStage = newStage;
                customer.feedback = newFeedback;

                collection.updateOne(
                        new Document("customerId", id),
                        new Document("$set", new Document("onboardingStage", newStage)
                                .append("feedback", newFeedback))
                );

                System.out.println("✅ Customer updated successfully!");
            } else {
                System.out.println("Customer not found.");
            }
        }

        // Delete customer
        void deleteCustomer(String id) {
            CustomerNode customer = searchCustomer(id);
            if (customer != null) {
                if (customer.prev != null) customer.prev.next = customer.next;
                if (customer.next != null) customer.next.prev = customer.prev;
                if (customer == head) head = customer.next;
                if (customer == tail) tail = customer.prev;

                collection.deleteOne(new Document("customerId", id));
                System.out.println("✅ Customer deleted successfully!");
            } else {
                System.out.println("Customer not found.");
            }
        }
    }

    // Main Menu
    public static void main(String[] args) {
        // Connect to MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("business");
        MongoCollection<Document> collection = database.getCollection("customers");

        CustomerDLL customerList = new CustomerDLL(collection);
        customerList.loadFromDatabase(); // Load existing customers

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Customer Onboarding Experience Mapper ===");
            System.out.println("1. Add Customer");
            System.out.println("2. Display All Customers");
            System.out.println("3. Search Customer");
            System.out.println("4. Update Onboarding Stage");
            System.out.println("5. Delete Customer");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Customer ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Onboarding Stage: ");
                    String stage = sc.nextLine();
                    System.out.print("Enter Feedback: ");
                    String feedback = sc.nextLine();

                    customerList.addCustomer(id, name, email, stage, feedback);
                }

                case 2 -> customerList.displayCustomers();

                case 3 -> {
                    System.out.print("Enter Customer ID to search: ");
                    String id = sc.nextLine();
                    CustomerNode c = customerList.searchCustomer(id);
                    if (c != null)
                        System.out.println("Found: ID: " + c.customerId + ", Name: " + c.name +
                                ", Email: " + c.email + ", Stage: " + c.onboardingStage +
                                ", Feedback: " + c.feedback);
                    else
                        System.out.println("Customer not found.");
                }

                case 4 -> {
                    System.out.print("Enter Customer ID to update: ");
                    String id = sc.nextLine();
                    System.out.print("Enter new Onboarding Stage: ");
                    String stage = sc.nextLine();
                    System.out.print("Enter new Feedback: ");
                    String feedback = sc.nextLine();
                    customerList.updateCustomer(id, stage, feedback);
                }

                case 5 -> {
                    System.out.print("Enter Customer ID to delete: ");
                    String id = sc.nextLine();
                    customerList.deleteCustomer(id);
                }

                case 6 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 6);

        mongoClient.close();
        sc.close();
    }
}
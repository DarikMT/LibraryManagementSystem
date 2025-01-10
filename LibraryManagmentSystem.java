import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

// Book class to store book details
class Book implements Serializable {
    private String title;
    private String author;
    private boolean isIssued;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isIssued() {
        return isIssued;
    }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    @Override
    public String toString() {
        return title + " by " + author + (isIssued ? " (Issued)" : "");
    }
}

// Main Library Management System class
public class LibraryMS {
    private LinkedList<Book> books;
    private Stack<Book> issuedBooksStack;
    private Queue<Book> reservationQueue;
    private final String filePath = "library_data.ser";

    public LibraryMS() {
        books = loadBooks();
        issuedBooksStack = new Stack<>();
        reservationQueue = new LinkedList<>();
        
    }

    // Method to add a new book
    public void addBook(String title, String author) {
        books.add(new Book(title, author));
        saveBooks();
        System.out.println("Book added successfully!");
    }

    // Method to display all books
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }

        System.out.println("Books in the Library:");
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    // Method to issue a book
    public void issueBook(int bookIndex) {
        if (bookIndex < 1 || bookIndex > books.size()) {
            System.out.println("Invalid book index.");
            return;
        }

        Book book = books.get(bookIndex - 1);
        if (book.isIssued()) {
            System.out.println("Book already issued. Adding to reservation queue.");
            reservationQueue.add(book);
        } else {
            book.setIssued(true);
            issuedBooksStack.push(book);
            saveBooks();
            System.out.println("Book issued successfully!");
        }
    }

    // Method to return a book
    public void returnBook(int bookIndex) {
        if (bookIndex < 1 || bookIndex > books.size()) {
            System.out.println("Invalid book index.");
            return;
        }

        Book book = books.get(bookIndex - 1);
        if (!book.isIssued()) {
            System.out.println("Book is not issued.");
        } else {
            book.setIssued(false);
            issuedBooksStack.pop();
            saveBooks();
            System.out.println("Book returned successfully!");
            
            // Check if there are reservations for the returned book
            if (!reservationQueue.isEmpty() && reservationQueue.peek().equals(book)) {
                reservationQueue.poll();
                System.out.println("Book reserved by next in queue.");
            }
        }
    }

    // Method to display issued books (stack operation)
    public void displayIssuedBooks() {
        if (issuedBooksStack.isEmpty()) {
            System.out.println("No books currently issued.");
        } else {
            System.out.println("Recently Issued Books:");
            for (Book book : issuedBooksStack) {
                System.out.println(book);
            }
        }
    }

    // Method to display reservation queue
    public void displayReservations() {
        if (reservationQueue.isEmpty()) {
            System.out.println("No reservations.");
        } else {
            System.out.println("Books in Reservation Queue:");
            for (Book book : reservationQueue) {
                System.out.println(book);
            }
        }
    }

    // Method to save books to a file
    private void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    // Method to load books from a file
    @SuppressWarnings("unchecked")
    private LinkedList<Book> loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (LinkedList<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new LinkedList<>();
        }
    }

    // Main method to run the system
    public static void main(String[] args) {
        LibraryMS library = new LibraryMS();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Display Issued Books");
            System.out.println("6. Display Reservations");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    library.addBook(title, author);
                    break;
                case 2:
                    library.displayBooks();
                    break;
                case 3:
                    System.out.print("Enter book index to issue: ");
                    int issueIndex = scanner.nextInt();
                    library.issueBook(issueIndex);
                    break;
                case 4:
                    System.out.print("Enter book index to return: ");
                    int returnIndex = scanner.nextInt();
                    library.returnBook(returnIndex);
                    break;
                case 5:
                    library.displayIssuedBooks();
                    break;
                case 6:
                    library.displayReservations();
                    break;
                case 7:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
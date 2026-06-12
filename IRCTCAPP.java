import com.sun.security.jgss.GSSUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class IRCTCAPP {

    private final Scanner sc=new Scanner(System.in);
    private final UserService userService=new UserService();
    private final BookingService bookingService=new BookingService();

    public static void main(String[] args) {
        new IRCTCAPP().start();
    }
    public void start(){
        while(true){
            System.out.println("\n--------Welcome to IRCTC APP--------");
            if(!userService.isLoggedIn()){
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your Choice:");
                int choice=sc.nextInt();

                switch(choice){
                    case 1 -> register();
                    case 2 -> login();
                    case 3 -> exitApp();
                    default -> System.out.println("Invalid Choice.");
                }
            }
            else{
                showUserMenu();
            }
        }
    }
    public void register(){
        System.out.print("Enter Username: ");
        String username=sc.next();
        System.out.print("Enter Password: ");
        sc.nextLine();
        String password=sc.next();
        System.out.print("Enter Full Name: ");
        sc.nextLine();
        String fullName= sc.nextLine();
        System.out.print("Enter contact: ");
        String contact=sc.next();

        userService.registerUser(username,password,fullName,contact);

        // Auto Login
        userService.loginUser(username, password);
    }

    public void login(){
        System.out.print("Enter Username: ");
        String username=sc.next();
        System.out.print("Enter Password");
        String password=sc.next();
        userService.loginUser(username,password);
    }
    private void showUserMenu(){
        while(userService.isLoggedIn()){
            System.out.println("\n----- User Menu ------");
            System.out.println("1. Search Trains:");
            System.out.println("2. Book Ticket:");
            System.out.println("3. View My Ticket:");
            System.out.println("4. Cancel Tickets:");
            System.out.println("5. View All Trains:");
            System.out.println("6. Logout");
            System.out.println("Enter your Choice");
            int choice=sc.nextInt();

            switch(choice){
                case 1 -> searchTrain();
                case 2 -> bookTicket();
                case 3 -> viewMyTicket();
                case 4 -> cancelTicket();
                case 5 -> bookingService.listAllTrains();
                case 6 -> userService.logOutUser();
                default -> System.out.println("Invalid Choice.");

            }
        }
    }
    private void searchTrain(){
        System.out.println("Enter source station::");
        String source=sc.next();
        System.out.println("Enter destination station::");
        String destination=sc.next();
        System.out.println("Enter date (yyyy/MM/dd) :");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String dateStr = sc.next();
        LocalDate date = LocalDate.parse(dateStr, formatter);

        List<Train> trains=bookingService.searchTrain(source,destination,date);
        if(trains.isEmpty()){
            System.out.println("No train found between "+source+" and "+destination+" on date "+date);
            return;
        }
        System.out.println("Trains Found:");
        for(Train train: trains){
            System.out.println(train);
        }
        System.out.println("Do you want to book ticket ? (yes/no):");
        String choice=sc.next();
        if(choice.equalsIgnoreCase("yes")){
            System.out.println("Enter train ID to book");
            int trainId=sc.nextInt();
            System.out.println("Enter number of seats to book:");
            int seats=sc.nextInt();

            Ticket ticket=bookingService.bookTicket(userService.getCurrentUser(),trainId,seats);
            if(ticket!=null){
                System.out.println("Booking Successful!");
                System.out.println(ticket);
            }

        }
        else{
            System.out.println("Returning to user menu....");
        }
    }

    private void bookTicket(){
        System.out.println("Enter source station::");
        String source=sc.next();
        System.out.println("Enter destination station::");
        String destination=sc.next();
        System.out.println("Enter date (yyyy/MM/dd) :");
        sc.nextLine(); // consume leftover newline
        String dateStr = sc.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);

        List<Train> trains=bookingService.searchTrain(source,destination,date);
        if(trains.isEmpty()){
            System.out.println("No train available for booking.");
            return;
        }
        System.out.println("Available trains: ");
        for(Train train: trains){
            System.out.println(train);
        }
        System.out.println("Enter train ID to book");
        int trainId=sc.nextInt();
        System.out.println("Enter number of seats to book:");
        int seats=sc.nextInt();

        Ticket ticket=bookingService.bookTicket(userService.getCurrentUser(),trainId,seats);
        if(ticket!=null){
            System.out.println("Booking Successful!");
            System.out.println(ticket);
        }

    }
    private void viewMyTicket(){
        List<Ticket> ticketByUser=bookingService.getTicketByUser(userService.getCurrentUser());
        if(ticketByUser.isEmpty()){
            System.out.println("No Ticket Booked yet");
        }
        else{
            System.out.println("Your tickets:");
            for(Ticket ticket:ticketByUser ){
                System.out.println(ticket);
            }
        }
    }

    private void cancelTicket(){
        System.out.println("Enter Ticket ID to cancel");
        int ticketId=sc.nextInt();
        bookingService.cancelTicket(ticketId,userService.getCurrentUser());

    }

    private void exitApp(){
        System.out.println("Thank you for using IRCTC App.");
        System.exit(0);
    }
}

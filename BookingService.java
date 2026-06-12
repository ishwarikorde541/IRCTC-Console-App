import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookingService {
    private List<Train> trainList = new ArrayList<>();
    private List<Ticket> ticketList=new ArrayList<>();

    public BookingService(){
        trainList.add(new Train(101,"Rajdhani Express","Delhi","Nagpur",100,LocalDate.of(2026, 6, 10)));
        trainList.add(new Train(102,"Shatabdi Express","Delhi","Mumbai",60, LocalDate.of(2026, 5, 14)));
        trainList.add(new Train(103,"Duranto Express","Agra","Delhi",70,LocalDate.of(2026, 2, 11)));
        trainList.add(new Train(104,"Vande Bharat Express","Delhi","Goa",100,LocalDate.of(2026, 8, 13)));
        trainList.add(new Train(105,"Intercity Express","Kolkata","Manali",90,LocalDate.of(2026, 4, 12)));
        trainList.add(new Train(106,"Tejas Express","Delhi","Bangluru",80,LocalDate.of(2026, 3, 16)));

    }
    //add date
    public List<Train> searchTrain(String source, String destination,LocalDate date){
        List<Train> res=new ArrayList<>();
        for(Train train: trainList){
            if(train.getSource().equalsIgnoreCase(source) && train.getDestination().equalsIgnoreCase(destination) && train.getDate().equals(date)){
                res.add(train);
            }
        }
        return res;
    }
    public Ticket bookTicket(User user, int trainId,int seatCount){
        for(Train train: trainList){
            if(train.getTrainId()==trainId){
                if(train.bookSeats(seatCount)){
                    Ticket ticket=new Ticket(user,train,seatCount);
                    ticketList.add(ticket);
                    return ticket;
                }
                else{
                    System.out.println("Not Enough seats avaialable");
                    return null;
                }
            }
        }
        System.out.println("Train Id not found");
        return null;
    }
    public List<Ticket> getTicketByUser(User user){
        List<Ticket> res=new ArrayList<>();
        for(Ticket ticket: ticketList){
            if(ticket.getUser().getUsername().equalsIgnoreCase(user.getUsername())){
                res.add(ticket);
            }
        }
        return res;
    }
    public boolean cancelTicket(int ticketId,User user){
        Iterator<Ticket> iterator=ticketList.listIterator();
        while(iterator.hasNext()){
            Ticket ticket=iterator.next();
            if(ticket.getTicketId()==ticketId &&
            ticket.getUser().getUsername().equalsIgnoreCase(user.getUsername())){
                Train train=ticket.getTrain();
                train.cancelSeats(ticket.getSeatBooked());
                iterator.remove();
                System.out.println("Ticket "+ticketId+" cancelled successfully");
                return true;
            }
        }
        System.out.println("Ticket not found or does not belong to current user");
        return false;
    }

    public void listAllTrains(){
        System.out.println("List of all trains:");
        for(Train train:trainList){
            System.out.println(train);
        }
    }

}

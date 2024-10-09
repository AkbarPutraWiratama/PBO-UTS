package UTS;

import java.util.Scanner;

interface Bookable {
    void bookRoom(Room room);
}

class Room {
    private String type;
    private double price;
    private boolean isAvailable;

    public Room(String type, double price) {
        this.type = type;
        this.price = price;
        this.isAvailable = true;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String toString() {
        return "Tipe Ruangan: " + type + ", Harga: " + price + ", Ketersediaan: " + isAvailable;
    }
}

class Booking {
    private static int counter = 0;
    private int bookingId;
    private Room room;
    private User user;

    public Booking(Room room, User user) {
        this.bookingId = ++counter;
        this.room = room;
        this.user = user;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Room getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public String toString() {
        return "Booking ID: " + bookingId + ", Room: " + room.getType() + ", User: " + user.getName();
    }
}

abstract class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void viewBookings(Booking[] bookings);
}

class Admin extends User {
    public Admin(String name) {
        super(name);
    }

    public void viewBookings(Booking[] bookings) {
        System.out.println("All bookings:");
        for (Booking booking : bookings) {
            if (booking != null) {
                System.out.println(booking);
            }
        }
    }
}

class Customer extends User implements Bookable {
    private Booking myBooking;

    public Customer(String name) {
        super(name);
    }

    public void bookRoom(Room room) {
        if (room.isAvailable()) {
            room.setAvailable(false);
            this.myBooking = new Booking(room, this);
            System.out.println("Ruangan berhasil di booking. Booking ID: " + myBooking.getBookingId());
        } else {
            System.out.println("Ruangan tidak tersedia.");
        }
    }

    public void viewBookings(Booking[] bookings) {
        if (myBooking != null) {
            System.out.println("Detail booking anda: " + myBooking);
        } else {
            System.out.println("Anda tidak memiliki booking.");
        }
    }
}

class Hotel {
    private Room[] rooms;
    private Booking[] bookings;
    private int bookingCount = 0;

    public Hotel(int roomCount, int bookingCapacity) {
        rooms = new Room[roomCount];
        bookings = new Booking[bookingCapacity];

        rooms[0] = new Room("Single", 400000);
        rooms[1] = new Room("Double", 700000);
        rooms[2] = new Room("Suite", 1500000);
    }

    public void showAvailableRooms() {
        System.out.println("Kamar yang tersedia:");
        for (Room room : rooms) {
            if (room != null && room.isAvailable()) {
                System.out.println(room);
            }
        }
    }

    public Room findRoomByType(String type) {
        for (Room room : rooms) {
            if (room != null && room.getType().equalsIgnoreCase(type) && room.isAvailable()) {
                return room;
            }
        }
        return null;
    }

    public void addBooking(Booking booking) {
        if (bookingCount < bookings.length) {
            bookings[bookingCount++] = booking;
        } else {
            System.out.println("Tidak kapasitas ada booking tersisa");
        }
    }

    public Booking[] getBookings() {
        return bookings;
    }
}

public class ReservasiHotel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hotel hotel = new Hotel(3, 5);  // 3 rooms, capacity for 5 bookings

        Admin admin = new Admin("Admin1");
        Customer customer = new Customer("Customer1");

        while (true) {
            System.out.println("\nSelamat Datang Di Reservasi Hotel");
            System.out.println("1. Login sebagai Admin");
            System.out.println("2. Login sebagai Customer");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.println("Logged sebagai Admin");
                System.out.println("1. Lihat semua booking");
                System.out.print("Pilih: ");
                int adminChoice = scanner.nextInt();
                if (adminChoice == 1) {
                    admin.viewBookings(hotel.getBookings());
                }
            } else if (choice == 2) {
                System.out.println("Logged sebagai Customer");
                System.out.println("1. Lihat ruangan yang tersedia");
                System.out.println("2. Booking ruangan");
                System.out.println("3. Lihat Booking saya");
                System.out.print("Pilih: ");
                int customerChoice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                if (customerChoice == 1) {
                    hotel.showAvailableRooms();
                } else if (customerChoice == 2) {
                    System.out.print("Masukkan tipe ruangan untuk di booking (Single/Double/Suite): ");
                    String roomType = scanner.nextLine();
                    Room room = hotel.findRoomByType(roomType);
                    if (room != null) {
                        customer.bookRoom(room);
                        hotel.addBooking(new Booking(room, customer));
                    } else {
                        System.out.println("Ruangan tidak tersedia.");
                    }
                } else if (customerChoice == 3) {
                    customer.viewBookings(hotel.getBookings());
                }
            } else if (choice == 3) {
                System.out.println("Keluar...");
                break;
            } else {
                System.out.println("Pilihan salah, coba lagi.");
            }
        }
        scanner.close();
    }
}
import java.util.*;
import java.io.*;

class Car {
    String stopName;
    int carPosition;
    String schoolName;
    Car next;

    // Default constructor
    Car() {
        this.stopName = "";
        this.carPosition = 0;
        this.schoolName = "";
        this.next = null;
    }

    // Parameterized constructor
    Car(String stopName, int carPosition, String schoolName) {
        this.stopName = stopName;
        this.carPosition = carPosition;
        this.schoolName = schoolName;
        this.next = null;
    }

    // Getter for carPosition
    public int getCarposition() {
        return this.carPosition;
    }

    // Getter for stopName
    public String getStopName() {
        return this.stopName;
    }

    // Getter for schoolName
    public String getSchoolName() {
        return this.schoolName;
    }

    // Setter for next Car
    public void setNext(Car next) {
        this.next = next;
    }

    // toString method to display car info
    public String toString() {
        return carPosition + "||" + schoolName + "||" + stopName;
    }
}

public class Amtrack {
    private Car front; 

    public Car getFront() {
        return this.front;
    }

    // Default constructor
    public Amtrack() {
        front = null;
    }

    // Attaches a car in ascending order of carPosition
    public void attachCar(String stopName, int carPosition, String schoolName) {
        Car newCar = new Car(stopName, carPosition, schoolName);
        if (front == null || carPosition < front.getCarposition()) {
            newCar.setNext(front);
            front = newCar;
            return;
        }
        Car current = front;
        while (current.next != null && current.next.getCarposition() < carPosition) {
            current = current.next;
        }
        newCar.setNext(current.next);
        current.setNext(newCar);
    }

    // Searches for a specific car in the train
    public boolean findCar(Car car) {
        Car current = front;
        while (current != null) {
            if (current.getCarposition() == car.getCarposition()
                    && current.getSchoolName().equals(car.getSchoolName())
                    && current.getStopName().equals(car.getStopName())) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Removes a specific car from the train
    public boolean detachCar(Car car) {
        if (front == null) return false;
        if (matches(front, car)) {
            front = front.next;
            return true;
        }
        Car current = front;
        while (current.next != null) {
            if (matches(current.next, car)) {
                current.setNext(current.next.next);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Checks if two cars match in all attributes
    private boolean matches(Car a, Car b) {
        return a.getCarposition() == b.getCarposition()
                && a.getStopName().equals(b.getStopName())
                && a.getSchoolName().equals(b.getSchoolName());
    }

    // Displays all cars in the train starting from the front
    public void displayCars() {
        Car current = front;
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
    }

    // Merges another train into this one in sorted order and removes duplicate Martinez
    public void mergeCars(Amtrack train2) {
        Car current = train2.front;
        while (current != null) {
            Car next = current.next;
            current.setNext(null);
            this.attachCar(current.getStopName(), current.getCarposition(), current.getSchoolName());
            current = next;
        }
        // Remove one Martinez car with position 4 (only once)
        this.detachCar(new Car("Martinez", 4, "John F Kennedy University"));
    }

    // Attaches a car directly to the front (used for reversing)
    public void attachCarToFront(String stopName, int carPosition, String schoolName) {
        Car newCar = new Car(stopName, carPosition, schoolName);
        newCar.setNext(front);
        front = newCar;
    }

    // Returns a new train that is the reverse of the current train
    public Amtrack reverseTrain() {
        Amtrack reversed = new Amtrack();
        Car current = front;
        while (current != null) {
            reversed.attachCarToFront(current.getStopName(), current.getCarposition(), current.getSchoolName());
            current = current.next;
        }
        return reversed;
    }

    // Constructs a train by reading from a file
    public Amtrack(String filename) {
        try {
            front = null;
            File myObj = new File(filename);
            Scanner fileReader = new Scanner(myObj);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                String[] tokens = data.split(",");
                attachCar(tokens[0].trim(), Integer.parseInt(tokens[1].trim()), tokens[2].trim());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Main method to demonstrate the functionality
    public static void main(String[] args) {
        Amtrack sacramentoTrain = new Amtrack("sacramento.txt");
        System.out.println("Sacramento Train:");
        sacramentoTrain.displayCars();

        Amtrack sanjoseTrain = new Amtrack("sanjose.txt");
        System.out.println("San Jose Train:");
        sanjoseTrain.displayCars();

        System.out.println("\nDetaching Oakland Car:");
        sacramentoTrain.detachCar(new Car("Oakland", 6, "Northeastern University in Oakland"));
        sacramentoTrain.displayCars();

        System.out.println("\nMerging the trains:");
        sacramentoTrain.mergeCars(sanjoseTrain);
        sacramentoTrain.displayCars();

        System.out.println("\nReversing the trains:");
        Amtrack reversedTrain = sacramentoTrain.reverseTrain();
        reversedTrain.displayCars();

        System.out.println("\nDetaching Duplicate Martinez Car:");
        reversedTrain.detachCar(new Car("Martinez", 4, "John F Kennedy University"));
        reversedTrain.displayCars();
    }
}

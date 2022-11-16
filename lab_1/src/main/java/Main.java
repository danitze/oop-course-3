import data.Airplane;
import data.CargoAirplane;
import data.PassengerAirplane;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String AIRPLANES_FILE_NAME = "airplanes";

    public static void main(String[] args) {
        enterInitialData();
        Airline airline = new Airline("Kubik airlines");
        airline.getAirplanesFromFile(AIRPLANES_FILE_NAME);
        String command;
        Scanner scanner = new Scanner(System.in);
        boolean shouldContinue = true;
        while (shouldContinue) {
            System.out.println("Enter command: (info, get, get-pas, get-cargo, get-sorted, get-distance-filtered, finish)");
            command = scanner.nextLine();
            switch (command) {
                case "info" -> {
                    System.out.println("get: returns all airplanes of the airline");
                    System.out.println("get-pas: returns overall passenger capacity");
                    System.out.println("get-cargo: returns overall cargo capacity");
                    System.out.println("get-sorted: returns airplanes sorted by flight distance");
                    System.out.println("get-distance-filtered: returns airplanes filtered by fuel consumption");
                    System.out.println("finish: finish app");
                }
                case "get" -> System.out.println(airline.getAirplanes());
                case "get-pas" -> System.out.println(airline.getPassengersCapacity());
                case "get-cargo" -> System.out.println(airline.getLoadCapacity());
                case "get-sorted" -> {
                    System.out.println("Enter sorting option (asc, desc)");
                    String sortingOption = scanner.nextLine();
                    switch (sortingOption) {
                        case "asc" -> System.out.println(airline.sortByFlightDistance(true));
                        case "desc" -> System.out.println(airline.sortByFlightDistance(false));
                        default -> System.out.println("Wrong command");
                    }
                }
                case "get-distance-filtered" -> {
                    System.out.println("Enter lower bound: ");
                    double lowerBound = Double.parseDouble(scanner.nextLine());
                    System.out.println("Enter upper bound: ");
                    double upperBound = Double.parseDouble(scanner.nextLine());
                    if(lowerBound > upperBound) {
                        double temp = upperBound;
                        upperBound = lowerBound;
                        lowerBound = temp;
                    }
                    System.out.println(airline.filterByFuelConsumption(lowerBound, upperBound));
                }
                case "finish" -> shouldContinue = false;
                case "" -> {}
                default -> System.out.println("Wrong command!");
            }
        }
    }

    private static void enterInitialData() {
        List<Airplane> airplanes = List.of(
                new PassengerAirplane("Airbus A380", 15200, 12000, 853),
                new CargoAirplane("An-225 Mriya", 15400, 20000, 250),
                new PassengerAirplane("Boeing 747", 14815, 10500, 366),
                new CargoAirplane("An-124 Ruslan", 20151, 17000, 120),
                new PassengerAirplane("Concorde", 7242, 25629, 100)
        );
        saveAirplanesToFile(AIRPLANES_FILE_NAME, airplanes);
    }

    private static void saveAirplanesToFile(String fileName, List<Airplane> airplanes) {
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            for (Airplane airplane: airplanes) {
                outputStream.writeObject(airplane);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
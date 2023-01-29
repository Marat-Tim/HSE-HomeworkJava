import automobile_factories.AutomobileFactory;
import automobile_factories.CoolAutomobileFactory;
import automobile_factories.UncoolAutomobileFactory;
import cargo_carriers.Truck;
import passenger_carriers.Car;

public class Main {
    // Место для настройки крутости автомобилей
    static AutomobileFactory automobileFactory = new UncoolAutomobileFactory();

    public static void main(String[] args) {
        Car car = automobileFactory.createCar();
        Truck truck = automobileFactory.createTruck();
        car.carryPassengers();
        truck.carryCargo();
    }
}

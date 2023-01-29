package automobile_factories;

import cargo_carriers.Gazelle;
import cargo_carriers.Truck;
import passenger_carriers.Car;
import passenger_carriers.Zhiguli;

public class UncoolAutomobileFactory extends AutomobileFactory {
    @Override
    public Truck createTruck() {
        return new Gazelle();
    }

    @Override
    public Car createCar() {
        return new Zhiguli();
    }
}

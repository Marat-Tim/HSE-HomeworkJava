package automobile_factories;

import cargo_carriers.Truck;
import passenger_carriers.Car;

public abstract class AutomobileFactory {
    public abstract Truck createTruck();

    public abstract Car createCar();
}

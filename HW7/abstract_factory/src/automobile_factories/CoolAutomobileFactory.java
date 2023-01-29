package automobile_factories;

import cargo_carriers.CoolTruck;
import cargo_carriers.Truck;
import passenger_carriers.Car;
import passenger_carriers.Mercedes;

public class CoolAutomobileFactory extends AutomobileFactory {
    @Override
    public Truck createTruck() {
        return new CoolTruck();
    }

    @Override
    public Car createCar() {
        return new Mercedes();
    }
}

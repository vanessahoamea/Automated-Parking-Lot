package b3.spl.splb.Services;

import b3.spl.splb.model.AppUser;
import b3.spl.splb.model.Car;

import java.util.List;

public interface CarService {
    Car saveCar (String userEmail, Car car);
    List<Car> getCars (AppUser user);
    boolean deleteCar(Long id);
    Car updateCarLicensePlate (Long carId, String newLicensePlate);
    Car getCarById(Long id);
}

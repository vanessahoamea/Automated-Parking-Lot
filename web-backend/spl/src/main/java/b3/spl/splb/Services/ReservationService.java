package b3.spl.splb.Services;

import b3.spl.splb.model.ParkingSpot;
import b3.spl.splb.model.Reservation;

public interface ReservationService {
    Reservation saveReservation(Long parkingSpotId, Long userId);
    void removeReservation(Long id);
}

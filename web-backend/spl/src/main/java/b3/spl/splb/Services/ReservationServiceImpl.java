package b3.spl.splb.Services;

import b3.spl.splb.model.AppUser;
import b3.spl.splb.model.ParkingSpot;
import b3.spl.splb.model.Reservation;
import b3.spl.splb.repository.AppUserRepo;
import b3.spl.splb.repository.ParkingSpotRepo;
import b3.spl.splb.repository.ReservationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService{
    private final ReservationRepo reservationRepo;
    private final AppUserRepo appUserRepo;
    private final ParkingSpotRepo parkingSpotRepo;

    @Override
    public Reservation saveReservation(Long parkingSpotId, Long userId) {
        AppUser user = appUserRepo.findById(userId).orElse(null);
        ParkingSpot parkingSpot = parkingSpotRepo.findById(parkingSpotId).orElse(null);
        if(Objects.isNull(user)) throw  new IllegalArgumentException("User not found");
        if(Objects.isNull(parkingSpot)) throw new IllegalArgumentException("ParkingSpot not found");
        if(!parkingSpot.isAvailable()) throw new IllegalArgumentException("ParkingSpot is not available");
        parkingSpot.setAvailable(false);
        return reservationRepo.save(new Reservation(null, parkingSpot, user, LocalDateTime.now() ,null));
    }

    @Override
    public void removeReservation(Long id) {
        Reservation reservation = reservationRepo.findById(id).orElse(null);
        if(Objects.isNull(reservation)) throw new IllegalArgumentException("Reservation not found");
        reservation.setClosedAt(LocalDateTime.now());
        reservation.getParkingSpot().setAvailable(true);
        reservationRepo.save(reservation);
    }
}

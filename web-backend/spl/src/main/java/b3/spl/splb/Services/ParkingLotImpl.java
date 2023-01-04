package b3.spl.splb.Services;

import b3.spl.splb.model.ParkingLot;
import b3.spl.splb.model.ParkingSpot;
import b3.spl.splb.repository.AppUserRepo;
import b3.spl.splb.repository.ParkingLotRepo;
import b3.spl.splb.repository.ParkingSpotRepo;
import b3.spl.splb.repository.RoleRepo;
import b3.spl.splb.util.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ParkingLotImpl implements ParkingLotService {
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final ParkingLotRepo parkingLotRepo;
    private final ParkingSpotRepo parkingSpotRepo;

    @Override
    public ParkingLot saveParkingLot(ParkingLot parkingLot, String email) {
        log.info("Saving new parking lot {} to the database", parkingLot.getName());
        parkingLot.setApproved(false);
        parkingLot.setUser(appUserRepo.findByEmail(email));
        ParkingLot parkingLot1= parkingLotRepo.save(parkingLot);
       // appUserRepo.findByEmail(email).getParkingLots().add(parkingLot);
        return parkingLot1;
    }

    @Override
    public List<ParkingLot> getParkingLots() {
        log.info("Sending parking lots from database");
        return parkingLotRepo.findAll();
    }


    @Override
    public List<ParkingLot> getClosestParkingLots(Point userLocation) {
        log.info("Fetching closest parkingLots");
        return parkingLotRepo.findClosestParkingLot(userLocation.getLatitude(),
                userLocation.getLongitude(), 0.01);
    }

    @Override
    public Optional<ParkingLot> setApproved(Long id, Boolean approved) {
        parkingLotRepo.findByIdAndSetApproved(id, approved);
        return parkingLotRepo.findById(id);
    }
    @Override
    public void deleteParkingLot(Long id){
        parkingLotRepo.deleteById(id);
    }

    @Override
    public boolean addParkingSpot(Long lot_id, Long spot_id) {
        Optional<ParkingLot> parkingLot = parkingLotRepo.findById(lot_id);
        Optional<ParkingSpot> parkingSpot = parkingSpotRepo.findById(spot_id);
        if(parkingLot.isPresent() && parkingSpot.isPresent()){
            parkingLot.get().getSpots().add(parkingSpot.get());
            return true;
        }
        return false;
    }
}

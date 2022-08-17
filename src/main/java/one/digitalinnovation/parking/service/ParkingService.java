package one.digitalinnovation.parking.service;

import one.digitalinnovation.parking.exception.ParkingNotFoundException;
import one.digitalinnovation.parking.model.Parking;
import one.digitalinnovation.parking.repository.ParkingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParkingService {

//    private static Map<String, Parking> parkingMap = new HashMap<>();

//    static {
//        var id = getUUID();
//        var id1 = getUUID();
//        Parking parking = new Parking(id, "DMS-1111","SC","CELTA","PRETO");
//        //Parking parking1 = new Parking(id1, "WAS-1234","SP","VW GOL","VERMELHO");
//        parkingMap.put(id,parking);
//        //parkingMap.put(id1,parking1);
//    }

    private final ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Transactional
    public List<Parking> findAll() {
        return parkingRepository.findAll();
//        return parkingMap.values().stream().collect(Collectors.toList());
    }

    private static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    @Transactional
    public Parking findById(String id) {
        return parkingRepository.findById(id).orElseThrow(
                () -> new ParkingNotFoundException(id)
        );
//        Parking parking = parkingMap.get(id);
//        if(parking == null) {
//            throw new ParkingNotFoundException(id);
//        }
//        return parking;
    }

    @Transactional
    public Parking create(Parking parkingCreate) {
        String uuid = getUUID();
        parkingCreate.setId(uuid);
        parkingCreate.setEntryDate(LocalDateTime.now());
        parkingRepository.save(parkingCreate);
//        parkingMap.put(uuid,parkingCreate);
        return parkingCreate;
    }

    @Transactional
    public void delete(String id) {
        findById(id);
        parkingRepository.deleteById(id);
//        parkingMap.remove(id);

    }

    @Transactional
    public Parking update(String id, Parking parkingCreate) {
        Parking parking = findById(id);
        parking.setColor(parkingCreate.getColor());
        parking.setState(parkingCreate.getState());
        parking.setModel(parkingCreate.getModel());
        parking.setLicense(parkingCreate.getLicense());

        parkingRepository.save(parking);
//        parkingMap.replace(id,parking);
        return parking;

    }

    @Transactional
    public Parking checkOut(String id) {
        Parking parking = findById(id);
        parking.setExitDate(LocalDateTime.now());
        parking.setBill(ParkingCheckOut.getBill(parking));
        return parkingRepository.save(parking);
    }
}

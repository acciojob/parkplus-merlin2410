package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user;
        ParkingLot parkingLot;
        try {
            user = userRepository3.findById(userId).get();
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }


        List<Spot> spotList = parkingLot.getSpotList();
        int minPrice = Integer.MAX_VALUE;
        Spot minPricedSpot = null;
        for(Spot spot: spotList){
            if(spot.getOccupied()==false){
                if((numberOfWheels>4 && spot.getSpotType()==SpotType.OTHERS) || (numberOfWheels>2 && numberOfWheels<=4 && spot.getSpotType()==SpotType.FOUR_WHEELER)
                        || (numberOfWheels<=2 && spot.getSpotType()==SpotType.TWO_WHEELER)){
                    int price = spot.getPricePerHour()*timeInHours;
                    if(price<minPrice){
                        minPrice = price;
                        minPricedSpot = spot;
                    }
                }
            }
        }
        if(minPricedSpot==null){
            throw new Exception("Cannot make reservation");
        }
        if(minPricedSpot.getOccupied()==true){
            throw new Exception("Cannot make reservation");
        }
        minPricedSpot.setOccupied(true);
        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(minPricedSpot);
        reservation.setUser(user);
        user.getReservationList().add(reservation);
        minPricedSpot.getReservationList().add(reservation);
        userRepository3.save(user);
        spotRepository3.save(minPricedSpot);
        return reservation;

    }
}

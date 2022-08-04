package com.project.ddm.controller;

import com.project.ddm.model.Station;
import com.project.ddm.repository.StationRepository;
import com.project.ddm.service.DeliveryService;
import com.project.ddm.service.DispatchService;
import com.project.ddm.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    private DispatchService dispatchService;

    private CheckoutService checkout;

    private DeliveryService deliveryService;

    private StationRepository stationRepository;

    @Autowired
    public OrderController(DispatchService dispatchService, CheckoutService checkout, DeliveryService deliveryService, StationRepository stationRepository) {
        this.dispatchService = dispatchService;
        this.checkout = checkout;
        this.deliveryService = deliveryService;
        this.stationRepository = stationRepository;
    }

    @GetMapping(value = "/order/search/station/{lat}/{lon}")
    public Long getStationId(@PathVariable double lat, @PathVariable double lon) {
        return dispatchService.getClosestStationId(lat, lon);
    }

    @GetMapping(value = "/order/search/device/{stationId}/{deviceType}")
    public List<Long> getDevice(@PathVariable Long stationId, @PathVariable String deviceType) {
        return dispatchService.getAvailableDeviceIdsAtStation(stationId, deviceType);
    }

    @GetMapping(value = "/order/search/device/{lon1}/{lat1}/{lon2}/{lat2}/{weight}/{size}/{device}")
    public double getCost(@PathVariable double lon1, @PathVariable double lat1, @PathVariable double lon2, @PathVariable double lat2, @PathVariable double weight, @PathVariable double size, @PathVariable String device) {
        return checkout.getCost(weight, size, lon1, lat1, lon2, lat2, device);
    }


    @GetMapping(value = "order/generate")
    public Map<String, Object> generateOrder(
            @RequestParam(name = "sending_lat") double sendingLat,
            @RequestParam(name = "sending_lon") double sendingLon,
            @RequestParam(name = "receiving_lat") double receivingLat,
            @RequestParam(name = "receiving_lon") double receivingLon) {

        Long stationId = dispatchService.getClosestStationId(sendingLon, sendingLat);
        System.out.println(stationId);
        Station station = stationRepository.findStationById(stationId);
        double stationLat = station.getLatitude();
        double stationLon = station.getLongitude();
        List<Long> pickUpTime = deliveryService.getPickUpTime(sendingLon, sendingLat, stationLon, stationLat);
        List<Long> deliveryTime = deliveryService.getDeliveryTime(sendingLon, sendingLat, receivingLon, receivingLat);

        Map<String, Object> map = new HashMap<>(2);
        map.put("pick_up_time", pickUpTime);
        map.put("delivery_time", deliveryTime);
        return map;
    }
}

package com.example.demo1.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddressDTO {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private GeoDTO geo;

    @Getter
    @ToString
    class GeoDTO {
        private double lat;
        private double lng;
    }
}

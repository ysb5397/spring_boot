package com.example.demo1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private long id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private Company company;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Geo {
            private String lat;
            private String lng;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Company {
        private String name;
        private String catchPhrase;
        private String bs;
    }

//    private int id;
//    private String name;
//    private String username;
//    private String email;
//    private AddressDTO address;
//    private String phone;
//    private String website;
//    private CompanyDTO company;
}

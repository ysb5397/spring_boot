package com.example.demo1.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String name;
    private int age;
    private String phoneNumber;

    public static void main(String[] args) {
        UserDTO userDTO = new UserDTO();
        System.out.println(userDTO.id);
        System.out.println(userDTO.name);
        System.out.println(userDTO.age);
        System.out.println(userDTO.phoneNumber);
    }
}

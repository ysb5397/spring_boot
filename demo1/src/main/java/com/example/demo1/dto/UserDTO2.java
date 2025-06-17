package com.example.demo1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class UserDTO2 {
    private String name;
    private int age;
    @JsonProperty("car_list")
    // @JsonProperty("car_list") 에서 괄호 내부는 실제로 던질 이름이다.
    // 따라서 이걸 설정해두면 본인이 원하는 이름으로 설정해도 된다.
    // --> 어차피 뭘 해두든 간에 저걸로 고정되어 있기 때문.
    private List<CarDTO> carList;

    // 내부 클래스
    @Getter
    @ToString
    static class CarDTO {
        private String name;
        @JsonProperty("car_number")
        private String carNumber;
    }
}

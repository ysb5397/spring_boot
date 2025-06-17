package com.example.demo1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetApiController2 {

    @GetMapping("/calcul")
    public String calculator(@RequestParam(name = "firstNum") String temp1, @RequestParam(name = "symbol") String symbol, @RequestParam(name = "secondNum") String temp2) {
        try {
            int num1 = Integer.parseInt(temp1);

            System.out.println("수신된 연산자: " + symbol);

            if (!symbol.equals("%2B") && !symbol.equals("-") && !symbol.equals("*") && !symbol.equals("/")) {
                return "잘못된 연산자입니다!";
            }

            int num2 = Integer.parseInt(temp2);

            switch (symbol) {
                case "%2B":
                    return String.valueOf(num1 + num2);
                case "-":
                    return String.valueOf(num1 - num2);
                case "*":
                    return String.valueOf(num1 * num2);
                case "/":
                    if (num2 == 0) {
                        return "0으로 나눌 수 없습니다!";
                    }
                    return String.valueOf(num1 / num2);
                default:
                    return "알 수 없는 연산자입니다.";
            }
        } catch (NumberFormatException e) {
            return "잘못된 입력값입니다!";
        } catch (ArithmeticException e) {
            return "0으로 나눌 수 없습니다!";
        }
    }
}
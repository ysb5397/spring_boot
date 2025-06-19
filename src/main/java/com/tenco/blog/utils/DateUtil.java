package com.tenco.blog.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.util.Date;

/**
 *  날짜, 시간 관련 유틸 클래스
 *  static 메서드로 바로 접근이 가능하도록 설계
 */
public class DateUtil {
    public static String timestampFormat(Timestamp time) {
        // board 엔티티에 선언된 Timestamp를 date 객체로 변환
        // getTime() 메서드를 호출해서 밀리초 단위로 시간을 받아 --> Date 객체 생성
        Date currentDate = new Date(time.getTime());

        // 아파치 commons 라이브러리
        return DateFormatUtils.format(currentDate, "yyyy년 MM월 dd일 HH시 mm분");
    }
}

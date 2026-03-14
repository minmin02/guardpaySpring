package com.example.guardpay.domain.map.converter;

import com.example.guardpay.domain.map.dto.res.BankRes;
import com.example.guardpay.domain.map.dto.res.LocationRes;

import java.util.Map;

public class MapConverter {

    public static BankRes toBankRes(Map<String, Object> doc) {
        String placeName = (String) doc.get("place_name");
        String bankName = extractBankName(placeName);

        return BankRes.builder()
                .id(Long.parseLong((String) doc.get("id")))
                .name(bankName)
                .branchName(placeName.replace(bankName, "").trim())
                .fullName(placeName)
                .address((String) doc.get("address_name"))
                .roadAddress((String) doc.get("road_address_name"))
                .lat(Double.parseDouble((String) doc.get("y")))
                .lon(Double.parseDouble((String) doc.get("x")))
                .phoneNumber((String) doc.get("phone"))
                .distance(toDouble(doc.get("distance")))
                .build();
    }

    private static String extractBankName(String placeName) {
        if (placeName == null) return "";
        String[] bankNames = {"신한은행", "국민은행", "우리은행", "하나은행", "NH농협은행", "기업은행"}; // ... 리스트 유지
        for (String name : bankNames) {
            if (placeName.contains(name)) return name;
        }
        return placeName.split(" ")[0];
    }

    private static Double toDouble(Object value) {
        if (value == null || value.toString().isEmpty()) return 0.0;
        return Double.parseDouble(value.toString());
    }

    public static LocationRes toLocationResFromKeyword(Map<String, Object> doc) {
        return LocationRes.builder()
                .placeName((String) doc.get("place_name"))
                .address((String) doc.get("address_name"))
                .roadAddress((String) doc.get("road_address_name"))
                .latitude(toDouble(doc.get("y")))
                .longitude(toDouble(doc.get("x")))
                .build();
    }

    public static LocationRes toLocationResFromAddress(Map<String, Object> doc) {
        Map<String, Object> addressObj = (Map<String, Object>) doc.get("address");
        Map<String, Object> roadAddressObj = (Map<String, Object>) doc.get("road_address");

        String mainName = (roadAddressObj != null)
                ? (String) roadAddressObj.get("address_name")
                : (String) addressObj.get("address_name");

        return LocationRes.builder()
                .placeName(mainName)
                .address(addressObj != null ? (String) addressObj.get("address_name") : "")
                .roadAddress(roadAddressObj != null ? (String) roadAddressObj.get("address_name") : "")
                .latitude(toDouble(doc.get("y"))) // 주소 검색의 루트 y, x 사용
                .longitude(toDouble(doc.get("x")))
                .build();
    }
}
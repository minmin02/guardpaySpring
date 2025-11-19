package com.example.guardpay.domain.map.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressSearchReq {

    @NotBlank(message = "검색어를 입력해주세요")
    private String query;
}
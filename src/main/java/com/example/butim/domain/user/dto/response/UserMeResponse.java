package com.example.butim.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMeResponse {

    private String name;
    private String email;
    private String phoneNumber;
}
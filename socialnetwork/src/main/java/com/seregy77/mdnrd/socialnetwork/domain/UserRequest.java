package com.seregy77.mdnrd.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {
    private String password;
    @JsonProperty("registration_date")
    private Date registrationDate;
    private String surname;
    private String name;
    private long id;
    private String email;
    private String username;
    private byte[] image;
}

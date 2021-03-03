package edu.kpi.testcourse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User(@JsonProperty("email") String email, @JsonProperty("password") String password) {}

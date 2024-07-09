package com.telemedicicne.telemedicicne.Dtos;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoTokenRequestDTO {
    private String videoToken;
    private Long refId;
}

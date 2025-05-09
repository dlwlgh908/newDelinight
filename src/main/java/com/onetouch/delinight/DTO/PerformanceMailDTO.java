package com.onetouch.delinight.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceMailDTO {

    private String email;
    private String name;
    private String targetName;
    private String aiResponse;
    private LocalDate date;

}

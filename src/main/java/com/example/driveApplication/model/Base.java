package com.example.driveApplication.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

@Data
@Accessors(chain = true)
public class Base {

    @Id
    String id;

}

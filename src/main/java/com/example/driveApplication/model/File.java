package com.example.driveApplication.model;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@Document
@TypeAlias("File")
public class File extends Base {

    private String name;
    private String uniqueName;
    private long size; // fileSize 2kb gibi
    private String ownerId; // dosyayı yukleyen, sahibi olan
    private List<String> sharedUserIds; // dosyaya erisebilecek, dosyanin paylasildigi kisilerin id
    private String extension; // jpg,png,doc ...

   // private MediaType mediaType;

    @CreatedDate
    private Date creationTime = new Date();
    @LastModifiedDate
    private Date modifiedTime = new Date();


    public List<String> getSharedUserIds() {
        if (sharedUserIds == null) {
            return new ArrayList<String>();
        }
        return sharedUserIds;
    }
}

package com.example.driveApplication.model;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Document
@TypeAlias("User")
@Accessors(chain = true)
public class User extends Base {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private List<String> sharedWithMeIds; // baska kullanici tarafindan benimle paylasilmis dosya, klasor id
    private List<String> fileIds; // kullanicinin sahibi oldugu file id
    private List<String> folderIds; // kullancinin sahibi oldugu klasor id
    private HashMap<String, List<String>> sharedFiles; // .. id'li file List<String> içerisindeki id'li kullanıcılar ile paylaşıldı


    public List<String> getSharedWithMeIds() {
        if (sharedWithMeIds == null) {
            return new ArrayList<>();
        }
        return sharedWithMeIds;
    }

    public List<String> getFileIds() {
        if (fileIds == null) {
            return new ArrayList<>();
        }
        return fileIds;
    }

    public List<String> folderIds() {
        if (folderIds == null) {
            return new ArrayList<>();
        }
        return folderIds;
    }

    public HashMap<String, List<String>> getSharedFiles() {
        if (sharedFiles==null) {
            return new HashMap<String,List<String>>();
        }
        return sharedFiles;
    }
}

package com.example.driveApplication.repository;

import com.example.driveApplication.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File,String> {
}

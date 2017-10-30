package com.hz.myspace.common.fileupload;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file) throws IOException;
    
    void store(MultipartFile file,String fileName) throws IOException;

    Stream<Path> loadAll();
    
    Boolean isFileExist(String filename);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
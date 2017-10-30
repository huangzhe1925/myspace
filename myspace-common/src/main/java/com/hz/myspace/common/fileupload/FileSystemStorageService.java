package com.hz.myspace.common.fileupload;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hz.myspace.common.fileupload.exceptions.StorageException;
import com.hz.myspace.common.fileupload.exceptions.StorageFileNotFoundException;

public class FileSystemStorageService implements StorageService {

	
	private final Path DEFAULT_LOCATION=Paths.get(".");
    private final Path rootLocation;
    
    public FileSystemStorageService() {
    	this.rootLocation=DEFAULT_LOCATION;
    }
    
    public FileSystemStorageService(String rootLocationString) {
    	if(StringUtils.isEmpty(rootLocationString)){
    		this.rootLocation=DEFAULT_LOCATION;
    	}else{
    		this.rootLocation = Paths.get(rootLocationString);
        }
    }

    @Override
    public void store(MultipartFile file)throws IOException  {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        store(file, filename);
    }
    
    @Override
    public void store(MultipartFile file,String fileName) throws IOException {
        String filename = StringUtils.cleanPath(fileName);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

	@Override
	public Boolean isFileExist(String filename) {
		return rootLocation.resolve(filename).toFile().exists();
	}
}
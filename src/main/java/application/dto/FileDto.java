package application.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileDto {

    public MultipartFile file;

    public FileDto() {
    }

    public FileDto(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

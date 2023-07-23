package com.example.Masterproject4;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class FileConverter{

    public File convertFile(MultipartFile fileInput) throws IOException {
        File fileConverted = new File(Objects.requireNonNull(fileInput.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(fileConverted)) {
            fos.write(fileInput.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Charset charset = StandardCharsets.UTF_8;
        String content = Files.readString(fileConverted.toPath(), charset);
        content = content.replaceAll("aas:", "");
        content = content.replaceAll("IEC:", "");
        Files.writeString(fileConverted.toPath(), content, charset);

        return fileConverted;
    }

}

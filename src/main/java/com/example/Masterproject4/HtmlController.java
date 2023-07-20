package com.example.Masterproject4;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
public class HtmlController {

    private final ResourceLoader resourceLoader;

    public HtmlController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("/submit-form")
    public String submitForm(@RequestParam("Temperatur") String Temperatur,
                             @RequestParam("Luftfeuchtigkeit") String Luftfeuchtigkeit,
                             @RequestParam("Pneumatikversorgung") String Pneumatikversorgung,
                             @RequestParam("Stromversorgung") String Stromversorgung,
                             @RequestParam("Flächenkosten") String Flächenkosten,
                             @RequestParam("Einsatzdauer") String Einsatzdauer,
                             @RequestParam("Ressourcen") MultipartFile RessourcenFile,
                             @RequestParam("file") MultipartFile file) throws IOException {

        System.out.println("Temperatur: " + Temperatur);
        System.out.println("Luftfeuchtigkeit: " + Luftfeuchtigkeit);
        System.out.println("Pneumatikversorgung: " + Pneumatikversorgung);
        System.out.println("Stromversorgung: " + Stromversorgung);
        System.out.println("Flächenkosten: " + Flächenkosten);
        System.out.println("Einsatzdauer: " + Einsatzdauer);
        System.out.println("Filetyp: " + file.getContentType());


        String path = "file:/C:/Users/domin/Desktop/Wilhelm Büchner - Hochschule/Masterarbeit/AASX_Files/*.json";
        // Dateien einlesen
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources(path);


        for (Resource resource : resources) {
            if (resource.isReadable()) {
                System.out.println("Datei: " + resource.getFilename());
            }
        }

        if (file.getContentType().equals("application/json")) {
            return "JSON-Datei erfolgreich verarbeitet.";
        } else if (file.getContentType().equals("text/xml")) {
            return "HTML erfolgreich verarbeiet";
        }

        return path;
    }
};
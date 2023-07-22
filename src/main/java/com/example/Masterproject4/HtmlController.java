package com.example.Masterproject4;

import com.example.Masterproject4.JAXBModels.XMLStructure;
import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import jakarta.xml.bind.JAXBException;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Logger;

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
                             @RequestParam("file") MultipartFile fileOfUser) throws IOException, JAXBException {

        System.out.println("Temperatur: " + Temperatur);
        System.out.println("Luftfeuchtigkeit: " + Luftfeuchtigkeit);
        System.out.println("Pneumatikversorgung: " + Pneumatikversorgung);
        System.out.println("Stromversorgung: " + Stromversorgung);
        System.out.println("Flächenkosten: " + Flächenkosten);
        System.out.println("Einsatzdauer: " + Einsatzdauer);
        System.out.println("Filetyp: " + fileOfUser.getContentType());

        if (fileOfUser.getContentType().equals("application/json")) {
            return "JSON-Datei erfolgreich verarbeitet.";
        } else if (fileOfUser.getContentType().equals("text/xml")) {
            ProductRequirementMapper mapper = new ProductRequirementMapper();

            // File Konvertieren von Multipart zu File
            File fileConverted = new File(Objects.requireNonNull(fileOfUser.getOriginalFilename()));
            try (FileOutputStream fos = new FileOutputStream(fileConverted)) {
                fos.write(fileOfUser.getBytes());
            }
            Charset charset = StandardCharsets.UTF_8;
            String content = Files.readString(fileConverted.toPath(), charset);
            content = content.replaceAll("aas:", "");
            content = content.replaceAll("IEC:", "");
            Files.writeString(fileConverted.toPath(), content, charset);


            XMLStructure XMLStructureOfMapper = mapper.unmarschallXML(fileConverted);
            ProductRequirementFullObject productRequirementFullObject = mapper.mapXMLToClass(XMLStructureOfMapper);
            System.out.println("---------------------------------------------");
            System.out.println("Infos zum Objekt ProductRequirementFullObject");
            System.out.println(productRequirementFullObject.getAssetId());
            System.out.println(productRequirementFullObject.getPart());
            System.out.println(productRequirementFullObject.getPart().size());
            System.out.println(productRequirementFullObject.getTeilVorgang());
            System.out.println(productRequirementFullObject.getTeilVorgang().size());

            return "HTML erfolgreich verarbeiet";
        }

        return "Alles Top";
    }
};
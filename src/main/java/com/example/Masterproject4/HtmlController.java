package com.example.Masterproject4;

import com.example.Masterproject4.JAXBModels.XMLStructure;
import com.example.Masterproject4.Mapper.AssuranceMapper;
import com.example.Masterproject4.Repository.AssuranceRepository;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
public class HtmlController {

    private final ResourceLoader resourceLoader;

    @Autowired
    private AssuranceRepository assuranceRepository;
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
                             @RequestParam("file") MultipartFile fileOfUser,
                             @RequestParam("assurance") MultipartFile assurance
    ) throws IOException, JAXBException {

        // Prüfen ob Zusicherungen hochgeladen werden müssen
        if(!assurance.isEmpty()) {
            File convertedFile = new FileConverter().convertFile(assurance);
            AssuranceMapper assuranceMapper = new AssuranceMapper();
            assuranceRepository.save(assuranceMapper.saveXMLToDatabase(convertedFile));

        }

        return "Werte für die Files sind (ressourcenfiel,anforderung,assurance)" + RessourcenFile.isEmpty() + " " + fileOfUser.isEmpty()  + " " + assurance.isEmpty() ;





        /*
        if (Objects.equals(fileOfUser.getContentType(), "application/json")) {
            return "JSON-Datei erfolgreich verarbeitet.";
        } else if (Objects.equals(fileOfUser.getContentType(), "text/xml")) {



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
        }*/

    }
}
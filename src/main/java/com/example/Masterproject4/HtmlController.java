package com.example.Masterproject4;

import com.example.Masterproject4.JAXBModels.XMLStructure;
import com.example.Masterproject4.Mapper.AssuranceMapper;
import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import com.example.Masterproject4.Repository.AssuranceRepository;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


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
                             @RequestParam("assurance") MultipartFile[] assurance
    ) throws IOException, JAXBException {

        // Prüfen ob Zusicherungen hochgeladen werden müssen
        for(MultipartFile fileInAssurance : assurance) {
            if(!fileInAssurance.isEmpty()) {
                File convertedFile = new FileConverter().convertFile(fileInAssurance);
                AssuranceMapper assuranceMapper = new AssuranceMapper();
                assuranceRepository.save(assuranceMapper.saveXMLToDatabase(convertedFile));
            }
        }

        if(!fileOfUser.isEmpty()) {
            File convertedFile = new FileConverter().convertFile(fileOfUser);
            ProductRequirementMapper productRequirementMapper = new ProductRequirementMapper();
            ProductRequirementFullObject fullObjectProductRequirement = productRequirementMapper.mapXMLToClass(convertedFile);
            System.out.println(fullObjectProductRequirement);
        }

        return "Verarbeitung erfolgt";


    }
}
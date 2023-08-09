package com.example.Masterproject4;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Handler.Constraints;
import com.example.Masterproject4.Handler.FileConverter;
import com.example.Masterproject4.Handler.RessourceChecker;
import com.example.Masterproject4.Handler.RessourceHolder;
import com.example.Masterproject4.Mapper.AssuranceMapper;
import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import com.example.Masterproject4.ProduktAnforderung.ProcessRequirement;
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
import java.util.ArrayList;
import java.util.List;


@RestController
public class HtmlController {

    private final ResourceLoader resourceLoader;

    @Autowired
    private AssuranceRepository assuranceRepository;

    @Autowired
    private RessourceChecker ressourceChecker;


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
        for (MultipartFile fileInAssurance : assurance) {
            if (!fileInAssurance.isEmpty()) {
                File convertedFile = new FileConverter().convertFile(fileInAssurance);
                AssuranceMapper assuranceMapper = new AssuranceMapper();
                assuranceRepository.save(assuranceMapper.saveXMLToDatabase(convertedFile));
            }
        }

        if (!fileOfUser.isEmpty()) {
            File convertedFile = new FileConverter().convertFile(fileOfUser);
            ProductRequirementMapper productRequirementMapper = new ProductRequirementMapper();
            ProductRequirementFullObject fullObjectProductRequirement = productRequirementMapper.mapXMLToClass(convertedFile);
            List<ProcessRequirement> processRequirementList = fullObjectProductRequirement.getProcessRequirement();


            List<RessourceHolder> ressourceHolderList = productRequirementMapper.fillAndSortRequirementList(processRequirementList, 1);
            productRequirementMapper.printRessourceHolderList(ressourceHolderList);

            // ConstraintListe von Assurance
            List<AssuranceFullObject> assuranceList = assuranceRepository.findAll();
            List<Constraints> assuranceFullList = new ArrayList<>();
            assuranceList.forEach(assuranceObject -> {
                Constraints constraintAssurance = Constraints.builder()
                        .idShort(assuranceObject.getAssetId())
                        .connectionType(assuranceObject.getConnectionType())
                        .restApi(assuranceObject.getRestAPIAdress())
                        .forceX(assuranceObject.getForceX())
                        .forceY(assuranceObject.getForceY())
                        .forceZ(assuranceObject.getForceZ())
                        .torqueX(assuranceObject.getTorqueX())
                        .torqueY(assuranceObject.getTorqueY())
                        .torqueZ(assuranceObject.getTorqueZ())
                        .positionRepetitionAccuracyX(assuranceObject.getPositionRepetitionAccuracyX())
                        .positionRepetitionAccuracyY(assuranceObject.getPositionRepetitionAccuracyY())
                        .positionRepetitionAccuracyZ(assuranceObject.getPositionRepetitionAccuracyZ())
                        .rotationRepetitionAccuracyX(assuranceObject.getRotationRepetitionAccuracyX())
                        .rotationRepetitionAccuracyY(assuranceObject.getRotationRepetitionAccuracyY())
                        .rotationRepetitionAccuracyZ(assuranceObject.getRotationRepetitionAccuracyZ())
                        .build();
                assuranceFullList.add(constraintAssurance);
            });

            // Zuerst Greifer prüfen
            ressourceChecker.checkConstraintsOfRequirement(ressourceHolderList,assuranceFullList,true);


        }


        return "Verarbeitung erfolgt";


    }

}
package com.example.Masterproject4;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Handler.Constraints;
import com.example.Masterproject4.Handler.FileConverter;
import com.example.Masterproject4.Handler.RessourceChecker;
import com.example.Masterproject4.ProduktAnforderung.ProductProcessReference;
import com.example.Masterproject4.ProduktAnforderung.RessourceHolder;
import com.example.Masterproject4.Mapper.AssuranceMapper;
import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import com.example.Masterproject4.ProduktAnforderung.ProcessRequirement;
import com.example.Masterproject4.ProduktAnforderung.ProductRequirementFullObject;
import com.example.Masterproject4.Repository.AssuranceRepository;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private ProductProcessReference productProcessReference;

    @Autowired
    private ProductRequirementMapper productRequirementMapper;

    @Autowired
    private ProductRequirementFullObject productRequirementFullObject;

    @Autowired
    private AssuranceMapper assuranceMapper;


    public HtmlController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private static final Logger Log = LoggerFactory.getLogger(HtmlController.class);


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
            /*
                Fertiges Objekt mit allen Daten der Produktanforderung
            */
            productRequirementFullObject = productRequirementMapper.mapXMLToClass(convertedFile);
            List<ProcessRequirement> processRequirementList = productRequirementFullObject.getProcessRequirement();


            /*
                Objekt zur Haltung der Beziehungen zwischen Produkt und Teilvorgängen
            */
            List<ProductProcessReference> listOfProductProcessReference = productRequirementMapper.getAllProductProcessReference(productRequirementFullObject);

            Log.info("ProductProcessReference ->");
            Log.info(String.valueOf(listOfProductProcessReference.size()));
            listOfProductProcessReference.forEach(productProcessReference -> {
                Log.info(productProcessReference.toString());
            });


            /*
                Objekt zur Haltung der Permutationen einer Sequenz von Teilvorgängen
             */
            List<RessourceHolder> ressourceHolderList = productRequirementMapper.fillAndSortRequirementList(processRequirementList,listOfProductProcessReference);
            Log.info("Ressourcenliste ->");
            ressourceHolderList.forEach(ressourceHolder -> {
               Log.info(ressourceHolder.toString());
            });


            /*
                Objekt zur Haltunge der Daten für die Zusicherungen
            */
            List<AssuranceFullObject> assuranceList = assuranceRepository.findAll();
            Log.info("AssuranceListFullObject ->");
            assuranceList.forEach(assuranceInLists -> {
                Log.info(assuranceInLists.toString());
            });





            /*
                Passende Ressource Greifer suchen (AutomaticallyRemoveable)

            */
            ressourceChecker.searchForGripper(ressourceHolderList,assuranceList);

            /*
                Passender Greifer wurde nun ausgesucht -> Suche nach Achse oder passenden Roboter
             */
            productRequirementMapper.setNewProperties(ressourceHolderList);
            Log.info("Neue Sequenz mit Greifer + Produkt");
            ressourceHolderList.forEach(ressourceHolder -> {
                Log.info(ressourceHolder.toString());
            });

        }


        return "Verarbeitung erfolgt";


    }

}
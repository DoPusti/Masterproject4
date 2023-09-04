package com.example.Masterproject4;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Handler.FileConverter;
import com.example.Masterproject4.Handler.RessourceChecker;
import com.example.Masterproject4.Mapper.AssuranceMapper;
import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import com.example.Masterproject4.ProduktAnforderung.*;
import com.example.Masterproject4.Repository.AssuranceRepository;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class HtmlController {

    private static final Logger Log = LoggerFactory.getLogger(HtmlController.class);
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
    @Autowired
    private StateOfStability stateOfStability;

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
    ) throws IOException, JAXBException, IllegalAccessException, NoSuchFieldException {


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
            /*
            List<ProcessRequirement> processRequirementList = productRequirementFullObject.getProcessRequirement();
            List<ProductProperty> productPropertyList = productRequirementFullObject.getProductProperty();
            List<StateOfStability> stabilityList = productRequirementMapper.setStateOfStability(processRequirementList);
            Map<String,String> listOfRelevantParameters = new HashMap<>();
            listOfRelevantParameters.put("positionX", "PersistentStateChange");
            listOfRelevantParameters.put("positionY", "PersistentStateChange");
            listOfRelevantParameters.put("positionZ", "PersistentStateChange");
            listOfRelevantParameters.put("forceX", "Constraints");
            listOfRelevantParameters.put("forceY", "Constraints");
            listOfRelevantParameters.put("forceZ", "Constraints");

            System.out.println("Stabilitätsliste ->");
            stabilityList.forEach(stability -> {
                System.out.println(stability.toString());
            });
            System.out.println("MatchingAttributeListe ->");
            System.out.println(listOfRelevantParameters);


            System.out.println("ProductRequirementFullObject ->");
            System.out.println("ProcessRequirement");
            processRequirementList.forEach(processRequirement -> {
                System.out.println(processRequirement.toString());
            });

            System.out.println("ProductProperty");
            productPropertyList.forEach(productProperty -> {
                System.out.println(productProperty.toString());
            });

             */

            //List<RequirementSequence> requirements = productRequirementMapper.getAllSequencesOfRequirements(productRequirementFullObject,listOfRelevantParameters);

            /*
                Objekt zur Haltung der Beziehungen zwischen Produkt und Teilvorgängen


            List<ProductProcessReference> listOfProductProcessReference = productRequirementMapper.getAllProductProcessReference(productRequirementFullObject);

            System.out.println("ProductProcessReference ->");
            listOfProductProcessReference.forEach(System.out::println);

            /*
                Objekt zur Haltung der Permutationen einer Sequenz von Teilvorgängen


            List<RessourceHolder> ressourceHolderList = productRequirementMapper.fillAndSortRequirementList(processRequirementList, listOfProductProcessReference);
            System.out.println("Ressourcenliste ->");
            ressourceHolderList.forEach(ressourceHolder -> {
                System.out.println(ressourceHolder.getStringSequence());
            });


            /*
                Objekt zur Haltunge der Daten für die Zusicherungen



            List<AssuranceFullObject> assuranceList = assuranceRepository.findAll();
            System.out.println("Zusicherungen ->");
            Log.info("AssuranceListFullObject ->");
            assuranceList.forEach(assuranceInLists -> {
                System.out.println(assuranceInLists.getStringSequence());
                Log.info(assuranceInLists.getStringSequence());
            });

            /*
                Passende Ressource Greifer suchen (AutomaticallyRemoveable)




            ressourceChecker.searchForGripper(ressourceHolderList, assuranceList);

            /*
                Passender Greifer wurde nun ausgesucht -> Suche nach Achse oder passenden Roboter



            productRequirementMapper.setNewProperties(ressourceHolderList);
            Log.info("Neue kombinierte Anforderung mit Greifer + Produkt");
            System.out.println("Neue kombinierte Anforderung mit Greifer + Produkt");
            ressourceHolderList.forEach(ressourceHolder -> {
                System.out.println(ressourceHolder.getStringSequence());
                Log.info(ressourceHolder.toString());
            });

            /*
                Passende Achse bzw. Roboter finden


            ressourceChecker.checkKinematicChain(ressourceHolderList, assuranceList, stabilityList, listOfRelevantParameters);

             */

        }
        // Pfad zum HTML-File im classpath:static Ordner
        String htmlFilePath = "static/responseFile.html";

        // Lese den Inhalt des HTML-Files
        Resource resource = new ClassPathResource(htmlFilePath);
        String htmlContent = Files.lines(Path.of(resource.getURI())).collect(Collectors.joining("\n"));
        // Thymeleaf ist eine Template-Engine für Java
        return htmlContent;


    }

}
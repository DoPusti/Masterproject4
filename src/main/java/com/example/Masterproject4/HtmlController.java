package com.example.Masterproject4;

import com.example.Masterproject4.CombinedRessources.ProductProcessReference;
import com.example.Masterproject4.CombinedRessources.RequirementSequence;
import com.example.Masterproject4.CombinedRessources.StateOfStability;
import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Handler.FileConverter;
import com.example.Masterproject4.Handler.RessourceChecker;
import com.example.Masterproject4.Mapper.AssuranceToDB;
import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import com.example.Masterproject4.Repository.AssuranceRepository;
import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.ProductRequirementFullObject;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private AssuranceToDB assuranceToDB;
    @Autowired
    private StateOfStability stateOfStability;

    @Autowired
    private RequirementSequence requirementSequence;

    @Autowired
    private RequirementSequence matchedRequirementSequence;


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
                AssuranceToDB assuranceToDB = new AssuranceToDB();
                assuranceRepository.save(assuranceToDB.saveXMLToDatabase(convertedFile));
            }
        }

        if (!fileOfUser.isEmpty()) {
            File convertedFile = new FileConverter().convertFile(fileOfUser);
            // Alle relevante Attribute von Product Property und Process Requirement
            productRequirementFullObject = productRequirementMapper.mapXMLToClass(convertedFile);
            // Fertige Liste von sortierten Attributen in Zusammenhang des jeweiligen Teilvorgangs
            requirementSequence = productRequirementMapper.mapProductRequirementFullObjectToSequence(productRequirementFullObject);
            // Erstellung von kinematischen Ketten. Sortierung nach aufsteigenden Werten
            productRequirementMapper.sortPropertiesInAscendingOrder(requirementSequence);
            // Zusicherungsliste füllen
            List<AssuranceFullObject> assuranceList = assuranceRepository.findAll();
            // Zusicherungen auf die Mapperklasse sortieren
            List<AssuranceMapper> assuranceMapList = ressourceChecker.fillAssuranceMapper(assuranceList);
            for (AssuranceMapper assuranceMapper : assuranceMapList) {
                System.out.println("Asset ID: " + assuranceMapper.getAssetId());
                System.out.println("ID      : " + assuranceMapper.getId());

                Map<String, PropertyInformation> propertyParameters = assuranceMapper.getPropertyParameters();
                for (Map.Entry<String, PropertyInformation> entry : propertyParameters.entrySet()) {
                    String propertyName = entry.getKey();
                    PropertyInformation propertyInformation = entry.getValue();
                    String valueOfParameter = String.valueOf(propertyInformation.getValueOfParameter());
                    System.out.println("Property Name: " + propertyName);
                    System.out.println("Value of Parameter: " + valueOfParameter);
                }

                System.out.println(); // Leerzeile zur Trennung der Einträge
            }
            // Zu Testzwecken eine Liste von relevanten Attributen
            Map<String, String> listOfRelevantParameters = new HashMap<>();
            listOfRelevantParameters.put("positionX", "PersistentStateChange");
            listOfRelevantParameters.put("positionY", "PersistentStateChange");
            listOfRelevantParameters.put("positionZ", "PersistentStateChange");
            listOfRelevantParameters.put("forceX", "Constraints");
            listOfRelevantParameters.put("forceY", "Constraints");
            listOfRelevantParameters.put("forceZ", "Constraints");
            // Finden eines passenden Greifers
            matchedRequirementSequence = ressourceChecker.searchForGripper(requirementSequence, assuranceList);
            // passende Zusicherungen finden





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
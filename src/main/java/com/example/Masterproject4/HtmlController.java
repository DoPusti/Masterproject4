package com.example.Masterproject4;

import com.example.Masterproject4.CombinedRessources.AttributeGroupedByName;
import com.example.Masterproject4.CombinedRessources.KinematicChainTree;
import com.example.Masterproject4.CombinedRessources.ProductProcessReference;
import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.Handler.FileConverter;
import com.example.Masterproject4.Handler.RessourceChecker;
import com.example.Masterproject4.Mapper.AssuranceToDB;
import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import com.example.Masterproject4.Mapper.RequirementTable;
import com.example.Masterproject4.RCZwei.RessourceChecker2;
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
    private RessourceChecker2 ressourceChecker2;
    @Autowired
    private ProductProcessReference productProcessReference;
    @Autowired
    private ProductRequirementMapper productRequirementMapper;
    @Autowired
    private ProductRequirementFullObject productRequirementFullObject;
    @Autowired
    private AssuranceToDB assuranceToDB;

    @Autowired
    private AttributeGroupedByName attributeGroupedByName;

    @Autowired
    private RequirementTable requirementTable;
    @Autowired
    private KinematicChainTree root;

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
                assuranceRepository.save(assuranceToDB.saveXMLToDatabase(convertedFile));
            }
        }

        if (!fileOfUser.isEmpty()) {
            File convertedFile = new FileConverter().convertFile(fileOfUser);
            // Zu Testzwecken eine Liste von relevanten Attributen
            Map<String, String> listOfRelevantParameters = new HashMap<>();
            listOfRelevantParameters.put("positionX", "PersistentStateChange");
            listOfRelevantParameters.put("positionY", "PersistentStateChange");
            //listOfRelevantParameters.put("positionZ", "PersistentStateChange");
            listOfRelevantParameters.put("forceX", "Constraints");
            listOfRelevantParameters.put("forceZ", "Constraints");
            listOfRelevantParameters.put("mass", "object");
            //listOfRelevantParameters.put("forceZ", "Constraints");
            productRequirementMapper.setListOfRelevantParameters(listOfRelevantParameters);
            // Alle relevante Attribute von Product Property und Process Requirement
            productRequirementFullObject = productRequirementMapper.mapXMLToClass(convertedFile);
            // Fertige Liste von sortierten Attributen in Zusammenhang des jeweiligen Teilvorgangs
            attributeGroupedByName = productRequirementMapper.mapProductRequirementFullObjectToSequence(productRequirementFullObject);
            // Erstellung von kinematischen Ketten. Sortierung nach aufsteigenden Werten
            productRequirementMapper.sortPropertiesInAscendingOrder(attributeGroupedByName);
            // Zusicherungsliste füllen
            List<AssuranceFullObject> assuranceList = assuranceRepository.findAll();
            // Zusicherungen auf die Mapperklasse sortieren
            List<AssuranceMapper> assuranceMapList = ressourceChecker.fillAssuranceMapper(assuranceList);
            // Map nun auf eine Zeilen-Spalten-Struktur parsen
            Map.Entry<String, Map<String, PropertyInformation>> firstEntry = attributeGroupedByName.getPropertyParameters().entrySet().iterator().next();
            int rowSize = firstEntry.getValue().size();
            int columnSize = attributeGroupedByName.getPropertyParameters().size();
            // Zeilen entsprechen der Anzahl von Teilvorgängen und Spalten der Anzahl der relevanten Attribute
            PropertyInformation[][] tableOfRequirement = new PropertyInformation[rowSize][columnSize];
            // Sortierte Anforderung auf eine 2-Dimensionale Tabelle mappen
            productRequirementMapper.mapToTableOfRequirement(attributeGroupedByName,tableOfRequirement);
            ressourceChecker2.setAssuranceMap(assuranceMapList);
            ressourceChecker2.assemblyByDisassembly(tableOfRequirement);


            //ressourceChecker.setAssuranceMap(assuranceMapList);
            //ressourceChecker.assemblyByDisassembly(tableOfRequirement);

            //ressourceChecker.assemblyByDisassemblyEasyVariant(tableOfRequirement);






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
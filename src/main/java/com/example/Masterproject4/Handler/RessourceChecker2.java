package com.example.Masterproject4.Handler;

import com.example.Masterproject4.CombinedRessources.KinematicChain;
import com.example.Masterproject4.Entity.AssuranceFullObject;
import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.*;

@Component
@Data
@Builder
public class RessourceChecker2 {

    private static final Logger Log = LoggerFactory.getLogger(RessourceChecker2.class);
    List<AssuranceMapper> assuranceMap;

    /***
     * Parsen einer Liste von Zusicherungen auf eine Liste von Zusicherungen im Map-Format
     * @param assuranceListIn   : Liste von Zusicherungen, die aus der Datenbank direkt kommen
     * @return assuranceListOut : Liste von Zusicherungen auf eine Map angepasst
     */
    public List<AssuranceMapper> fillAssuranceMapper(List<AssuranceFullObject> assuranceListIn) {
        List<AssuranceMapper> assuranceListOut = new ArrayList<>();

        for (AssuranceFullObject fullObject : assuranceListIn) {
            AssuranceMapper mapper = new AssuranceMapper();
            mapper.setAssetId(fullObject.getAssetId());
            mapper.setId((fullObject.getId()));
            mapper.setConnectionType(fullObject.getConnectionType());
            Map<String, PropertyInformation> propertyParameters = new TreeMap<>();
            // Verwende Reflection, um alle Double-Attribute zu extrahieren
            Field[] fields = fullObject.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == double.class) {
                    try {
                        double value = field.getDouble(fullObject);
                        if (field.getName().equals("price")) {
                            mapper.setPrice(value);
                        } else {
                            PropertyInformation newPropertyInformationForAssuranceMapper = new PropertyInformation();
                            newPropertyInformationForAssuranceMapper.setValueOfParameter(value);
                            propertyParameters.put(field.getName(), newPropertyInformationForAssuranceMapper);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            mapper.setPropertyParameters(propertyParameters);
            assuranceListOut.add(mapper);
        }
        return assuranceListOut;
    }

    public KinematicChain assemblyByDisassembly(PropertyInformation[][] tableOfRequirement) {
        KinematicChain rootObject = new KinematicChain();
        rootObject.setUuid(0);
        rootObject.setTableOfRemainingRequirement(tableOfRequirement);
        AssuranceMapper dummyMapper = new AssuranceMapper();
        dummyMapper.setPrice(0);
        dummyMapper.setId(0L);
        rootObject.setGripperOrAxis(dummyMapper);
        searchForGripper(rootObject, tableOfRequirement);
        return rootObject;

    }

    public void searchForGripper(KinematicChain parentNode, PropertyInformation[][] remainingRequirement) {
        //Prüfung ob Requirmentset leer ist
        Log.info("Aufruf SEARCHFORGRIPPER");
        Log.info("===============================");
        Log.info("      Prüfung ob Anforderungsliste noch leer ist:");
        Log.info(Arrays.deepToString(remainingRequirement));
        if (!(remainingRequirement == null || remainingRequirement.length == 0)) {
            Log.info("      Anforderungsliste ist noch nicht leer");
            //Schleife über alle Greifer
            assuranceMap.forEach(assurance -> {
                PropertyInformation[][] requirementForAssurance = remainingRequirement;
                Set<String> remainingSequences = new HashSet<>();
                Map<String, PropertyInformation> sequenceOfAllProperties = new HashMap<>();
                if (assurance.getConnectionType().equals("AutomaticallyRemoveable") && (assurance.getId() == 52 || assurance.getId() == 202)) {
                    Log.info("      Betrachtung der Zusicherung : " + assurance.getId());
                    Map<String, PropertyInformation> propertiesOfAssurance = new HashMap<>(assurance.getPropertyParameters());
                    boolean gripperIsRelevant = true;
                    // Schleife über die Tabelle der tableOfRequirement
                    for (int col = 0; col < requirementForAssurance[0].length; col++) {
                        // Mindestens in einer Zeile des Attributs muss ein passender Wert gefunden werden
                        boolean matchingColumnFound = false;
                        // Dient zur Prüfung, ob bereits ein ähnlicher Greifer mit gleicher Sequenz gefunden wurde
                        boolean columnForSequenceSet = false;
                        String attributeName = requirementForAssurance[0][col].getAttributeName();
                        String attributeSpecification = requirementForAssurance[0][col].getDataSpecification();
                        // Es werden nur Spalten betrachtet mit Constraints, da PersistentStateChange bei einem Greifer nicht möglich ist
                        if (attributeSpecification.equals("Constraints")) {
                            for (int row = 0; row < requirementForAssurance.length; row++) {
                                //Passender Greifer filtern
                                double valueOfRequirement = requirementForAssurance[row][col].getValueOfParameter();
                                double valueOfAssuranceAttribute = propertiesOfAssurance.get(attributeName).getValueOfParameter();
                                // Vergleich des Wertes aus Zusicherung mit Anforderung
                                if (valueOfAssuranceAttribute >= valueOfRequirement) {
                                    requirementForAssurance[row][col].setRequirementFullFilled(true);
                                    //Log.info("          Attribut " + attributeName + " von Zusicherung erfüllt mit " + valueOfAssuranceAttribute + " >= " + valueOfRequirement + ".");
                                    matchingColumnFound = true;
                                    if (!columnForSequenceSet) {
                                        //Log.info("          Attribut " + attributeName + " für Sequenze wurde gesetzt.");
                                        sequenceOfAllProperties.put(requirementForAssurance[row][col].getAttributeName(), requirementForAssurance[row][col]);
                                        columnForSequenceSet = true;
                                    }
                                } else {
                                    //Log.info("          Attribut " + attributeName + " wird auf false gesetzt in Zeile/" + row + "/Spalte " + col);
                                    requirementForAssurance[row][col].setRequirementFullFilled(false);
                                    remainingSequences.add(requirementForAssurance[row][col].getSubProcessId());
                                }
                            }
                        }
                        if (!matchingColumnFound && requirementForAssurance[0][col].getDataSpecification().equals("Constraints")) {
                            gripperIsRelevant = false;
                            Log.info("          Greifer ist nicht relevant, weil mindestens eine Spalte nicht erfüllt wurde.");
                            break;
                        }
                    }
                    Log.info("          Greifer relevant : " + gripperIsRelevant);
                    //Wenn Greifer passt -> Sequenz erstellen
                    if (gripperIsRelevant) {
                        // Wenn alle Teilvorgänge erfüllt sind. Also alles aus Zeile 1 der Maximatabelle
                        if (remainingSequences.isEmpty()) remainingSequences.add("Alle Teilvorgänge sind erfüllt.");
                        Log.info("          Übrige Sequence der Teilvorgänge die nicht passen : " + remainingSequences);
                        boolean setIsRelevant = true;
                        boolean sameSequenceExists = false;
                        // Jeder bereits vorhandene Leaf wird geprüft
                        if (parentNode.getChilds() != null) {
                            for (KinematicChain child : parentNode.getChilds()) {
                                // Jeder Set-Eintrag
                                if (child.getRemainingSequence().equals(remainingSequences)) {
                                    Log.info("          SetEintrag mit " + remainingSequences + " bereits vorhanden.");
                                    Log.info("                    Alter Wert: " + child.getGripperOrAxis().getPrice());
                                    Log.info("                    Neuer Wert: " + assurance.getPrice());
                                    sameSequenceExists = true;
                                    if (assurance.getPrice() > child.getGripperOrAxis().getPrice()) {
                                        Log.info("          Greifer ist damit eindeutig relevant, da der Preis geringer ist.");
                                        setIsRelevant = false;
                                        break;
                                    }
                                }
                            }
                        }
                        Log.info("          Gleiche Sequence exisitert? : " + sameSequenceExists + " und Set ist relevant?  : " + setIsRelevant);
                        if (setIsRelevant) {
                            Log.info("          Los gehts. Knotenpunkt wird erstellt.");
                            PropertyInformation[][] remainingRequirmentsAfterGripper = null;
                            if (!(remainingSequences.contains("Alle Teilvorgänge sind erfüllt."))) {
                                // Nur wenn Teilvorgänge übrig sind, muss ein Komplementärset erstellt werden, ansonsten ist es leer
                                remainingRequirmentsAfterGripper = createComplementaryRequirementSet(remainingSequences, remainingRequirement);
                            }
                            // Die PersistentStateChanges müssen noch in die Sequenz eingesetzt werden.
                            // Dabei wird geprüft, welche Teilvorgänge bereits erfüllt werden und von diesen müssen dann die höchsten PersistentStateChanges genommen werden
                            fillKinematicChainPropertiesWithPersistentStateChange(remainingRequirement, remainingSequences, sequenceOfAllProperties);
                            //Knotenpunkt erstellen
                            //Markiere Node als Greifer
                            //Sequenz dem Knoten hinzufügen
                            //Komplementärset erstellen
                            //Ressource hinzufügen
                            //Prüfung ob ähnliche Sequenz innerhalb des Parent-Node bereits vorhanden ist und billiger oder nicht vorhanden -> neu hinzufügen
                            KinematicChain nodeForGripper = KinematicChain.builder()
                                    .childs(new ArrayList<>())
                                    .uuid((int) (Math.random() * (10000 - 1)))
                                    .gripperOrAxis(assurance)                                      // Werte der Zusicherung
                                    .remainingSequence(remainingSequences)                         // Teilvorgänge, die noch später bearbeitet werden müssen
                                    .tableOfRemainingRequirement(remainingRequirmentsAfterGripper) // Komplementärset
                                    .nameOfAssurance("Gripper")                                    // Art der Zusicherung
                                    .propertiesOfCurrentKinematicChain(sequenceOfAllProperties)    // Aktuelle kinematische Kette, die zu betrachten ist
                                    .build();
                            parentNode.addChild(nodeForGripper);
                            Log.info("              Alle Informationen zum aktuellen Knotenpunkt: " + nodeForGripper.getUuid());
                            Log.info("                        GreiferID                                              :" + nodeForGripper.getGripperOrAxis().getId());
                            Log.info("                        Typ                                                    :" + nodeForGripper.getNameOfAssurance());
                            Log.info("                        Teilvorgänge, die noch später bearbeitet werden müssen : ");
                            nodeForGripper.getRemainingSequence().forEach(Log::info);
                            ;
                            Log.info("                        Gerade zu betrachtende kineamtische Kette              : ");
                            for (Map.Entry<String, PropertyInformation> entry : nodeForGripper.getPropertiesOfCurrentKinematicChain().entrySet()) {
                                Log.info("                          Attribut: " + entry.getKey() + "/" + entry.getValue().getValueOfParameter() + "/" + entry.getValue().getSubProcessId());
                            }
                            //Für diesen Greifer nun passende Achsen finden -> searchForAxis
                            searchForAxis(nodeForGripper);
                        }
                    }
                }
            });

        }
    }

    public void searchForAxis(KinematicChain node) {
        //Prüfen ob RequiredStateChanges noch zu erfüllen sind
        Log.info(Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.info("===============================");
        Log.info("UPRO : searchForAxis");
        Log.info("===============================");
        Log.info("              Knotenpunkt " + node.getUuid() + " wird untersucht.");
        Map<String, PropertyInformation> propertiesOfCurrentKinematicChain = node.getPropertiesOfCurrentKinematicChain();

        if (requiredStateChangesLeft(propertiesOfCurrentKinematicChain)) {
            // Suche Achse, zur Sequence, wo findest ein RequiredStateChange erfüllt wurde und alle Constraints
            assuranceMap.forEach(assurance -> {
                Map<String, PropertyInformation> newChildMap = new HashMap<>();
                boolean persistentStateChangeFullFilled = false;
                boolean assuranceIsRelevant = true;
                if (assurance.getConnectionType().equals("NotAutomaticallyRemovable")) {
                    Log.info("              Anzahl der aktuellen Kindknoten " + node.getChilds().size());
                    Map<String, PropertyInformation> propertiesOfAssurance = assurance.getPropertyParameters();
                    Log.info("              Achse mit ID " + assurance.getId() + " wird geprüft");
                    // Jeden Eintrag aus der Anforderung prüfen
                    Iterator<Map.Entry<String, PropertyInformation>> iterator = propertiesOfCurrentKinematicChain.entrySet().iterator();
                    while (iterator.hasNext() && assuranceIsRelevant) {
                        Map.Entry<String, PropertyInformation> entry = iterator.next();
                        String attributeName = entry.getKey();
                        double attributeValue = entry.getValue().getValueOfParameter();
                        String dataSpecification = entry.getValue().getDataSpecification();
                        switch (dataSpecification) {
                            case "Constraints" -> {
                                newChildMap.put(attributeName, entry.getValue());
                                if (!(attributeValue <= propertiesOfAssurance.get(attributeName).getValueOfParameter())) {
                                    assuranceIsRelevant = false;
                                    Log.info("              Constraint kann nicht erfüllt werden");
                                    Log.info("              " + attributeName + " von Zusicherung ist <= dem Anforderungswert:" +
                                            propertiesOfAssurance.get(attributeName).getValueOfParameter() + "<=" + attributeValue);
                                    ;
                                }
                            }
                            case "PersistentStateChange" -> {
                                if (attributeValue > 0 && (attributeValue <= propertiesOfAssurance.get(attributeName).getValueOfParameter())) {
                                    Log.info("              PersistentStateChange für Attribut " + attributeName + " wurde erfüllt.");
                                    persistentStateChangeFullFilled = true;
                                    PropertyInformation newPropertyInformationForPersistentStateChange = new PropertyInformation(entry.getValue());
                                    // Wert wurde erfüllt und damit auf 0 gesetzt
                                    newPropertyInformationForPersistentStateChange.setValueOfParameter(0);
                                    newChildMap.put(attributeName, newPropertyInformationForPersistentStateChange);
                                } else {
                                    newChildMap.put(attributeName, entry.getValue());
                                }
                            }
                        }
                    }
                    if (persistentStateChangeFullFilled && assuranceIsRelevant) {
                        Log.info("              !!Zusicherung mit ID " + assurance.getId() + " ist relevant für die nächsten Schritte!!.");
                        KinematicChain newChildNode = KinematicChain.builder()
                                .uuid((int) (Math.random() * (10000 - 1)))
                                .childs(new ArrayList<>())
                                .gripperOrAxis(assurance)                                               // Werte der Zusicherung
                                .remainingSequence(node.getRemainingSequence())                         // Teilvorgänge, die noch später bearbeitet werden müssen
                                .tableOfRemainingRequirement(node.getTableOfRemainingRequirement())     // Komplementärset
                                .nameOfAssurance("Achse")
                                .propertiesOfCurrentKinematicChain(new HashMap<>(newChildMap))// Art der Zusicherung
                                //.propertiesOfCurrentKinematicChain(new HashMap<>(newChildMap))          // Aktuelle kinematische Kette, die zu betrachten ist
                                .build();
                        // KinematicChain newChildNode = node;
                        node.addChild(newChildNode);
                        Log.info("       Kinematische Kette nach der Achsensuche            : ");

                        Log.info("              Alle Informationen zum aktuellen Kind-Knotenpunkt: " + newChildNode.getUuid());
                        Log.info("                            " + newChildNode.getNameOfAssurance() + "/" + newChildNode.getGripperOrAxis().getId());
                        /*
                        Log.info("                            Teilvorgänge, die noch später bearbeitet werden müssen : ");
                        newChildNode.getRemainingSequence().forEach(Log::info);
                        ;
                         */
                        for (Map.Entry<String, PropertyInformation> entry : newChildNode.getPropertiesOfCurrentKinematicChain().entrySet()) {
                            Log.info("                            Attribut: " + entry.getKey() + "/" + entry.getValue().getValueOfParameter() + "/" + entry.getValue().getSubProcessId());
                        }
                        Log.info("              Alle Informationen zum aktuellen Eltern-Knotenpunkt: " + node.getUuid());
                        Log.info("                            " + node.getNameOfAssurance() + "/" + node.getGripperOrAxis().getId());
                        /*
                        Log.info("                            Teilvorgänge, die noch später bearbeitet werden müssen : ");
                        node.getRemainingSequence().forEach(Log::info);
                        ;
                         */
                        Log.info("                            Kinematische Kette nach der Achsensuche            : ");
                        for (Map.Entry<String, PropertyInformation> entry : node.getPropertiesOfCurrentKinematicChain().entrySet()) {
                            Log.info("                            Attribut: " + entry.getKey() + "/" + entry.getValue().getValueOfParameter() + "/" + entry.getValue().getSubProcessId());
                        }
                        searchForAxis(newChildNode);


                    } else {
                        Log.info("                            Achse nicht relevant");
                    }
                }
            });
        } else {
            Log.info("                            Knotenpunkt hat keine offenen Werte mehr.");
        }
    }

    public Boolean requiredStateChangesLeft(Map<String, PropertyInformation> kinematicChainIn) {
        /*
        Log.info("===============================");
        Log.info("UPRO : requiredStateChangesLeft");
        Log.info("===============================");

         */
        boolean mininumOfOneAttributeNotFullFilled = false;

        for (Map.Entry<String, PropertyInformation> entry : kinematicChainIn.entrySet()) {
            if (entry.getValue().getDataSpecification().equals("PersistentStateChange") && entry.getValue().getValueOfParameter() > 0) {
                /*
                Log.info("              Attribut muss noch erfüllt werden");
                Log.info("                            Name : " + entry.getValue().getAttributeName());
                Log.info("                            Wert : " + entry.getValue().getValueOfParameter());

                 */
                mininumOfOneAttributeNotFullFilled = true;
                break;
            }
        }

        return mininumOfOneAttributeNotFullFilled;
    }

    public PropertyInformation[][] createComplementaryRequirementSet(Set<String> remainingSequencesIn,
                                                                     PropertyInformation[][] originListOfRequirement) {
        Log.info("===============================");
        Log.info("UPRO : createComplementaryRequirementSet ");
        Log.info("===============================");
        int columnLength = originListOfRequirement[0].length;
        int rowLength = remainingSequencesIn.size();
        PropertyInformation[][] complementaryRequirementSet = new PropertyInformation[rowLength][columnLength];
        Log.info("          Übrige Sequenzen, die zu prüfen sind: ");
        for (String element : remainingSequencesIn) {
            Log.info(element);
        }
        Log.info("          Damit Erstellung eines Komplementärsets mit Zeilen=" + rowLength + " und Spalten=" + columnLength);

        int counterForColumn = 0;
        for (int col = 0; col < originListOfRequirement[0].length; col++) {
            Log.info("          Spalte " + col + " wird gesetzt.");
            int counterForRow = 0;
            for (PropertyInformation[] propertyInformations : originListOfRequirement) {
                // Es werden nur Teilvorgänge in der korrekten Order abgefragt, die noch übrig sind
                if (remainingSequencesIn.contains(propertyInformations[col].getSubProcessId())) {
                    Log.info("          Teilvorgang " + propertyInformations[col].getSubProcessId() + " wird betrachtet");
                    complementaryRequirementSet[counterForRow][counterForColumn] = propertyInformations[col];
                    counterForRow++;
                }
            }
            counterForColumn++;
        }
        Log.info("          KomplementärSet wurde erstellt");
        for (int col = 0; col < complementaryRequirementSet[0].length; col++) {
            for (int row = 0; row < complementaryRequirementSet.length; row++) {
                Log.info("          Zeile " + row + "/Spalte " + col + "=" + complementaryRequirementSet[row][col]);
            }
        }
        return originListOfRequirement;
    }

    public void fillKinematicChainPropertiesWithPersistentStateChange(PropertyInformation[][] originListOfRequirement,
                                                                      Set<String> remainingSequencesIn,
                                                                      Map<String, PropertyInformation> propertiesOfCurrentKinematicChainIn) {
        Log.info("===============================");
        Log.info("UPRO : fillKinematicChainPropertiesWithPersistentStateChange ");
        Log.info("===============================");
        for (int col = 0; col < originListOfRequirement[0].length; col++) {
            String attributeName = originListOfRequirement[0][col].getAttributeName();
            String attributeSpecification = originListOfRequirement[0][col].getDataSpecification();
            if (attributeSpecification.equals("PersistentStateChange")) {
                for (PropertyInformation[] propertyInformations : originListOfRequirement) {
                    String attributeSequence = propertyInformations[col].getSubProcessId();
                    if (!remainingSequencesIn.contains(attributeSequence)) {
                        propertiesOfCurrentKinematicChainIn.put(propertyInformations[col].getAttributeName(), propertyInformations[col]);
                        Log.info("          Attribut für Kinematische Kette gefunden.");
                        Log.info("                    Name: " + attributeName);
                        Log.info("                    Wert: " + propertyInformations[col].getValueOfParameter());
                        break;
                    }
                }
            }
        }
    }

    public void callRestService(List<Constraints> matchedAssurances) {
        matchedAssurances.forEach(assurance -> {
            if (!(assurance.getRestApi() == null)) {
                String restCall;
                restCall = assurance.getRestApi() + "/getPayload?AssetId=" + assurance.getIdShort();
                Log.info("RestCall " + restCall);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> call = restTemplate.getForEntity(restCall, String.class);
                Log.info(call.getBody());
            }
        });

    }


}

package com.example.Masterproject4.Handler;

import com.example.Masterproject4.CombinedRessources.KinematicChainNode;
import com.example.Masterproject4.CombinedRessources.KinematicChainRoot;
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
public class RessourceChecker {


    private static final Logger Log = LoggerFactory.getLogger(RessourceChecker.class);
    List<AssuranceMapper> assuranceMap = new ArrayList<>();


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

    public void assemblyByDisassembly(PropertyInformation[][] tableOfRequirement) {
        Log.info("UPRO : assemblyByDisassembly -->");
        KinematicChainRoot startingPointOfChain = new KinematicChainRoot();
        // Zeilen kürzen
        //int rowsToKeep = 3; // Die Anzahl der Zeilen, die du behalten möchtest
        //PropertyInformation[][] newTable = new PropertyInformation[rowsToKeep][];
        //System.arraycopy(tableOfRequirement, 0, newTable, 0, rowsToKeep);
        Log.info("  Suche aller aktuellen Greifer mit dem Anforderungsset: ");
        printRequirementTable(tableOfRequirement);
        // Nodes für den Start festlegen
        startingPointOfChain.setLeaveNodes(searchForGripper(tableOfRequirement));
        Log.info("  Alle Startpunkte wurden nun festgelegt. Es sind " + startingPointOfChain.getLeaveNodes().size() + " Stück.");
        List<KinematicChainNode> leaveNodes = startingPointOfChain.getLeaveNodes();
        for (KinematicChainNode node : leaveNodes) {
            // Jeder Knoten durchläuft eine Rekursive Funktion
            findCombinedRessources(node);
        }






    }
    public void findCombinedRessources(KinematicChainNode nodeIn) {

    }


    public void printRequirementTable(PropertyInformation[][] tableOfRequirement) {
        for (int i = 0; i < tableOfRequirement.length; i++) {
            for (int j = 0; j < tableOfRequirement[i].length; j++) {
                PropertyInformation element = tableOfRequirement[i][j];
                Log.info("(" + i + "/" + j + "): " + element.getSubProcessId() + "/" + element.getAttributeName() + "/" + element.getValueOfParameter());
            }
        }
    }

    public List<KinematicChainNode> searchForGripper(PropertyInformation[][] tableOfRequirementIn) {
        Log.info("UPRO : searchForGripper --> ");
        // Erstmal alle Greifer suchen

        Map<Set<String>, KinematicChainNode> listOfAllSequencesWithNodes = new HashMap<>();
        String kindOfAssurance = "AutomaticallyRemoveable";
        // Jede Zusicherung wird geprüft
        assuranceMap.forEach(assurance -> {
            KinematicChainNode nodeForAssurance = new KinematicChainNode();
            PropertyInformation[][] requirementTableForAssurance = tableOfRequirementIn;
            // Greifer
            if (assurance.getConnectionType().equals(kindOfAssurance)) {
                Log.info("  Zusicherung " + assurance.getId() + " wird betrachtet.");
                Map<String, PropertyInformation> propertiesOfAssurance = assurance.getPropertyParameters();
                // Schleife über die Tabelle der tableOfRequirement
                for (int col = 0; col < requirementTableForAssurance[0].length; col++) {
                    for (int row = 0; row < requirementTableForAssurance.length; row++) {
                        //Wert zum gesuchten Attribut finden
                        String attributeName = requirementTableForAssurance[row][col].getAttributeName();
                        double valueOfRequirement = requirementTableForAssurance[row][col].getValueOfParameter();
                        double valueOfAssuranceAttribute = propertiesOfAssurance.get(attributeName).getValueOfParameter();
                        String attributeSpecification = requirementTableForAssurance[row][col].getDataSpecification();
                        if (valueOfAssuranceAttribute >= valueOfRequirement && attributeSpecification.equals("Constraints")) {
                            requirementTableForAssurance[row][col].setRequirementFullFilled(true);
                            Log.info("  Zeile " + row + "/Spalte " + col);
                            Log.info("  Attribut " + attributeName + " von Zusicherung mit " + valueOfAssuranceAttribute + " >= " + valueOfRequirement + "." + kindOfAssurance + "/" + attributeSpecification);
                            //break;
                        } else {
                            Log.info("  Attribut " + attributeName + " wird auf false gesetzt in Zeile/" + row + "/Spalte " + col);
                            requirementTableForAssurance[row][col].setRequirementFullFilled(false);
                        }
                    }
                }
                // Nun wurde die komplette Requirementliste durchgeloopt und musst geprüft werden, ob dieser relevant wäre. Wenn Mindestens 1 True pro Spalte
                // vorhanden ist, gilt es als gültig
                Set<String> remainingSequences = new HashSet<>();
                ArrayList<PropertyInformation> requirementSequence = createSequence(requirementTableForAssurance, remainingSequences);
                // Wenn Sequence noch nicht vorhanden ist, wird sie eingetragen. Falls vorhanden und Preis billiger, wird sie auch eingetragen
                Log.info("Prüfung auf ähnliche Sequenzen");
                // Für jede Spalte muss ein Eintrag gefunden sein, ansonsten kann die Sequence nicht betrachtet werden
                if (!(requirementSequence == null)) {
                    nodeForAssurance.setGripperOrAxis(assurance);
                    Log.info("Bisherige Einträge von Sequenzen : ");
                    // Iterieren Sie über die Schlüssel und geben Sie sie aus
                    for (Set<String> key : listOfAllSequencesWithNodes.keySet()) {
                        Log.info("Schlüssel: " + key);
                    }
                    Log.info("Zu suchende Sequenz : ");
                    Log.info(remainingSequences.toString());
                    if (!listOfAllSequencesWithNodes.containsKey(remainingSequences) ||
                            (listOfAllSequencesWithNodes.get(remainingSequences).getGripperOrAxis().getPrice() > assurance.getPrice())) {
                        Log.info("Sequenz ist entweder noch nicht vorhanden, oder Preis ist billiger");
                        Log.info("Preis der aktuellen Zusicherung : " + assurance.getPrice());
                        nodeForAssurance.setRequirementSequence(requirementSequence);
                        Log.info("Sequence ist gültig");
                        Log.info("Teilvorgänge die nicht erfüllt wurden: ");
                        Log.info(remainingSequences.toString());
                        // Übrige Anfoderung ersetzen
                        if(!remainingSequences.isEmpty()) {
                            nodeForAssurance.setTableOfRemainingRequirement(createComplementaryRequirementSet(remainingSequences, requirementTableForAssurance));
                        }
                        listOfAllSequencesWithNodes.put(remainingSequences,nodeForAssurance);

                    } else {
                        Log.info("Sequenz wird nicht benötigt.");
                    }
                }
            }
        });



        Log.info("Alle Zusicherungen wurden geprüft und werden der Liste hinzugefügt");
        List<KinematicChainNode> nodes = new ArrayList<>(listOfAllSequencesWithNodes.values());
        Log.info(nodes.toString());


        return nodes;
    }

    public ArrayList<PropertyInformation> createSequence(PropertyInformation[][] remainingRequirementListIn, Set<String> remainingSequencesIn) {

        Log.info("UPRO : createSequence --> ");
        ArrayList<PropertyInformation> sequence = new ArrayList<>();
        for (int col = 0; col < remainingRequirementListIn[0].length; col++) {
            if (remainingRequirementListIn[0][col].getDataSpecification().equals("Constraints")) {
                boolean foundTrueForColumn = false;
                for (int row = 0; row < remainingRequirementListIn.length; row++) {
                    if (remainingRequirementListIn[row][col].isRequirementFullFilled()) {
                        sequence.add(remainingRequirementListIn[row][col]);
                        Log.info("  Wert erfüllt Kriterium  : " + remainingRequirementListIn[row][col].getAttributeName() + "/" + row + "/" + col + "/" + remainingRequirementListIn[row][col].isRequirementFullFilled());
                        foundTrueForColumn = true;
                        break;
                    } else {
                        // Teilvorgänge, die nicht erfüllt sind in Liste speichern
                        Log.info("  Teilvorgang " + remainingRequirementListIn[row][col].getSubProcessId() + " nicht erfüllt.");
                        remainingSequencesIn.add(remainingRequirementListIn[row][col].getSubProcessId());
                    }
                }
                if (!foundTrueForColumn) {
                    sequence.clear();
                    break;
                }
            }

        }


        return sequence;
    }


    public PropertyInformation[][] createComplementaryRequirementSet(Set<String> remainingSequencesIn, PropertyInformation[][] originListOfRequirement) {

        int columnLength = originListOfRequirement[0].length;
        int rowLength = remainingSequencesIn.size();
        PropertyInformation[][] complementaryRequirementSet = new PropertyInformation[rowLength][columnLength];
        Log.info("UPRO : createComplementaryRequirementSet ");
        Log.info("Übrige Sequenzen, die zu prüfen sind: ");
        for (String element : remainingSequencesIn) {
            Log.info(element);
        }
        Log.info("Damit Erstellung eines Komplementärsets mit Zeilen=" + rowLength + " und Spalten=" + columnLength);

        int counterForColumn = 0;
        for (int col = 0; col < originListOfRequirement[0].length; col++) {
            Log.info("Spalte " + col + " wird gesetzt.");
            int counterForRow = 0;
            for (PropertyInformation[] propertyInformations : originListOfRequirement) {
                // Es werden nur Teilvorgänge in der korrekten Order abgefragt, die noch übrig sind
                if (remainingSequencesIn.contains(propertyInformations[col].getSubProcessId())) {
                    Log.info("Teilvorgang " + propertyInformations[col].getSubProcessId() + " wird betrachtet");
                    complementaryRequirementSet[counterForRow][counterForColumn] = propertyInformations[col];
                    counterForRow++;
                }
            }
            counterForColumn++;
        }
        Log.info("KomplementärSet wurde erstellt");
        for (int col = 0; col < complementaryRequirementSet[0].length; col++) {
            for (int row = 0; row < complementaryRequirementSet.length; row++) {
                Log.info("Zeile " + row + "/Spalte " + col + "=" + complementaryRequirementSet[row][col]);
            }
        }
        return originListOfRequirement;
    }

    public void checkForStability() {

    }

}

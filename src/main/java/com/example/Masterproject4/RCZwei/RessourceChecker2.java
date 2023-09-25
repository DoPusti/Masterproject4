package com.example.Masterproject4.RCZwei;

import com.example.Masterproject4.XMLAttributeHolder.AssuranceMapper;
import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
@Builder
public class RessourceChecker2 {

    private static final Logger Log = LoggerFactory.getLogger(RessourceChecker2.class);
    List<AssuranceMapper> assuranceMap;

    public void assemblyByDisassembly(PropertyInformation[][] tableOfRequirement) {
        KinematicChain rootObject = new KinematicChain();
        rootObject.setTableOfRemainingRequirement(tableOfRequirement);
        searchForGripper(rootObject, tableOfRequirement);


    }

    public void searchForGripper(KinematicChain node, PropertyInformation[][] remainingRequirement) {
        //Prüfung ob Requirmentset leer ist
        Log.info("Aufruf SEARCHFORGRIPPER");
        Log.info("===============================");
        Log.info("      Prüfung ob Anforderungsliste noch leer ist:");
        Log.info(Arrays.deepToString(remainingRequirement));
        if (!(remainingRequirement == null || remainingRequirement.length == 0)) {
            //Schleife über alle Greifer
            assuranceMap.forEach(assurance -> {
                PropertyInformation[][] requirementForAssurance = remainingRequirement;
                Set<String> remainingSequences = new HashSet<>();
                Set<String> requiredStateChanges = new HashSet<>();
                ArrayList<PropertyInformation> sequenceOfAllProperties = new ArrayList<>();
                if (assurance.getConnectionType().equals("AutomaticallyRemoveable")) {
                    Log.info("      Betrachtung der Zusicherung : " + assurance.getId());
                    Map<String, PropertyInformation> propertiesOfAssurance = assurance.getPropertyParameters();
                    boolean gripperIsRelevant = true;
                    // Schleife über die Tabelle der tableOfRequirement
                    for (int col = 0; col < requirementForAssurance[0].length; col++) {
                        boolean matchingColumnFound = false;
                        boolean columnForSequenceSet = false;
                        for (int row = 0; row < requirementForAssurance.length; row++) {
                            //Passender Greifer filtern
                            String attributeName = requirementForAssurance[row][col].getAttributeName();
                            double valueOfRequirement = requirementForAssurance[row][col].getValueOfParameter();
                            double valueOfAssuranceAttribute = propertiesOfAssurance.get(attributeName).getValueOfParameter();
                            String attributeSpecification = requirementForAssurance[row][col].getDataSpecification();
                            if (valueOfAssuranceAttribute >= valueOfRequirement && attributeSpecification.equals("Constraints")) {
                                requirementForAssurance[row][col].setRequirementFullFilled(true);
                                Log.info("          Zeile " + row + "/Spalte " + col);
                                Log.info("          Attribut " + attributeName + " von Zusicherung mit " + valueOfAssuranceAttribute + " >= " + valueOfRequirement + ".");
                                matchingColumnFound = true;
                                if(!columnForSequenceSet) {
                                    Log.info("      Attribut " + attributeName + " für Sequenze wurde gesetzt.");
                                    sequenceOfAllProperties.add(requirementForAssurance[row][col]);
                                    columnForSequenceSet = true;
                                }

                            } else {
                                Log.info("          Attribut " + attributeName + " wird auf false gesetzt in Zeile/" + row + "/Spalte " + col);
                                requirementForAssurance[row][col].setRequirementFullFilled(false);
                                remainingSequences.add(requirementForAssurance[row][col].getSubProcessId());
                            }
                            if (attributeSpecification.equals("PersistentStateChange") && valueOfAssuranceAttribute > 0) {
                                requiredStateChanges.add(attributeName);
                               }
                        }
                        if (!matchingColumnFound) {
                            gripperIsRelevant = false;
                            Log.info("      Greifer ist nicht relevant, weil eine Spalte nicht erfüllt wurde.");
                            break;
                        }
                    }
                    Log.info("  Greifer relevant : " + gripperIsRelevant);
                    //Wenn Greifer passt -> Sequenz erstellen
                    if (gripperIsRelevant) {
                        if (remainingSequences.isEmpty()) remainingSequences.add("Alle Teilvorgänge sind erfüllt.");
                        Log.info("      Übrige Sequence der Teilvorgänge die nicht passen : " + remainingSequences.toString());
                        Log.info("      Gibt es diese Sequence bereits?");
                        boolean setIsRelevant = false;
                        boolean sameSequenceExists = false;
                        // Jeder bereits vorhandene Leaf wird geprüft
                        for (KinematicChain child : node.getChilds()) {
                            // Jeder Set-Eintrag
                            if (child.getRemainingSequence().equals(remainingSequences)) {
                                Log.info("      SetEintrag mit " + remainingSequences + " bereits vorhanden.");
                                Log.info("      Alter Wert: " + child.getGripperOrAxis().getPrice());
                                Log.info("      Neuer Wert: " + assurance.getPrice());
                                sameSequenceExists = true;
                                if (assurance.getPrice() < child.getGripperOrAxis().getPrice()) {
                                    Log.info("      Greifer ist damit eindeutig relevant");
                                    setIsRelevant = true;
                                    break;
                                }
                            }
                        }
                        Log.info("      Gleiche Sequence exisitert : " + sameSequenceExists + " und Set ist relevant  : " + setIsRelevant);
                        if (setIsRelevant || !sameSequenceExists) {
                            Log.info("      Los gehts. Knotenpunkt wird erstellt.");
                            PropertyInformation[][] remainingRequirmentsAfterGripper = createComplementaryRequirementSet(remainingSequences, remainingRequirement);
                            //Knotenpunkt erstellen
                            //Markiere Node als Greifer
                            //Sequenz dem Knoten hinzufügen
                            //Komplementärset erstellen
                            //Ressource hinzufügen
                            //Prüfung ob ähnliche Sequenz innerhalb des Parent-Node bereits vorhanden ist und billiger oder nicht vorhanden -> neu hinzufügen
                            KinematicChain nodeForGripper = KinematicChain.builder()
                                    .gripperOrAxis(assurance)
                                    .remainingSequence(remainingSequences)
                                    .tableOfRemainingRequirement(remainingRequirmentsAfterGripper)
                                    .nameOfAssurance("Gripper")
                                    .remainingRequiredStateChanges(requiredStateChanges)
                                    .sequenceOfAllProperties(sequenceOfAllProperties)
                                    .build();
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
        Log.info("Aufruf SEARCHFORAXIS");
        Log.info("===============================");
        Log.info("      Noch zu erfüllende RequiredStateChanges " + node.getRemainingRequiredStateChanges());
        if (!(node.getRemainingRequiredStateChanges() == null || node.getRemainingRequiredStateChanges().isEmpty())) {
            // Suche Achse, zur Sequence, wo findest ein RequiredStateChange erfüllt wurde und alle Constraints
            assuranceMap.forEach(assurance -> {
                if (assurance.getConnectionType().equals("NotAutomaticallyRemoveable")) {
                    Map<String, PropertyInformation> propertiesOfAssurance = assurance.getPropertyParameters();
                    Log.info("       Greifer mit ID " + assurance.getId());
                    //for(nod)
                }
            });
        }
    }


    //Node erstellen

    //Wenn im ParentNode bereits diese Sequence vorhanden ist, dann schauen welcher billiger ist. Wenn billiger oder nicht vorhanden dann Node eintragen

    //rufe searchForAxis mit gleichem auf

    //Wenn nichts mehr zu erfüllen ist, rufe searchForGripper auf



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


}

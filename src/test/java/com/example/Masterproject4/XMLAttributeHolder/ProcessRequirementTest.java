package com.example.Masterproject4.XMLAttributeHolder;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProcessRequirementTest {

    @Test
    public void testMapclass() {
        // Annahme: Sie haben bereits eine Map<String, Map<String, PropertyInformation>> mit Daten gefüllt
        Map<String, Map<String, PropertyInformation>> outerMap = new HashMap<>();

        // Beispiel-Daten hinzufügen
        Map<String, PropertyInformation> innerMap1 = new HashMap<>();
        innerMap1.put("entry1", new PropertyInformation("TV1",3.0,true,"",false));
        innerMap1.put("entry2", new PropertyInformation("TV2",1.0,true,"",false));
        innerMap1.put("entry3", new PropertyInformation("TV3",2.0,true,"",false));

        Map<String, PropertyInformation> innerMap2 = new HashMap<>();
        innerMap2.put("entry1", new PropertyInformation("TV1",7.0,true,"",false));
        innerMap2.put("entry2", new PropertyInformation("TV2",5.0,true,"",false));
        innerMap2.put("entry3", new PropertyInformation("TV3",6.0,true,"",false));

        outerMap.put("key1", innerMap1);
        outerMap.put("key2", innerMap2);

        // ... (Daten wie im vorherigen Beispiel hinzufügen) ...

        // Sortieren der inneren Maps absteigend nach valueOfParameter
        for (Map.Entry<String, Map<String, PropertyInformation>> outerEntry : outerMap.entrySet()) {
            Map<String, PropertyInformation> innerMap = outerEntry.getValue();
            List<Map.Entry<String, PropertyInformation>> sortedEntries = new ArrayList<>(innerMap.entrySet());

            // Sortieren der Einträge absteigend anhand valueOfParameter
            sortedEntries.sort((entry1, entry2) ->
                    Double.compare(entry2.getValue().getValueOfParameter(), entry1.getValue().getValueOfParameter()));

            // Erstellen einer neuen LinkedHashMap mit sortierten Einträgen
            LinkedHashMap<String, PropertyInformation> sortedInnerMap = new LinkedHashMap<>();
            for (Map.Entry<String, PropertyInformation> sortedEntry : sortedEntries) {
                sortedInnerMap.put(sortedEntry.getKey(), sortedEntry.getValue());
            }

            // Ersetzen der ursprünglichen inneren Map durch die sortierte Map
            outerEntry.setValue(sortedInnerMap);
        }

        // Überprüfen des Ergebnisses
        for (Map.Entry<String, Map<String, PropertyInformation>> outerEntry : outerMap.entrySet()) {
            System.out.println("Outer Key: " + outerEntry.getKey());
            System.out.println("Inner Map: " + outerEntry.getValue());
        }

    }

}
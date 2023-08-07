package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.Handler.RessourceHolder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
class LessonTest {

    @Test
    public void checkMapper() {

        Map<String, Double> unsortedMapX = Map.of(
                "Wert3", 15.75,
                "Wert1", 10.5,
                "Wert2", 20.0
        );
        Map<String, Double> unsortedMapY = Map.of(
                "Wert3", 16.75,
                "Wert1", 500.5,
                "Wert2", 21.0
        );

        // Elemente in eine List übertragen und nach den Werten sortieren
        List<Map.Entry<String, Double>> sortedListX = new ArrayList<>(unsortedMapX.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .toList());

        // Die Liste umdrehen
        Collections.reverse(sortedListX);

        // Elemente in eine List übertragen und nach den Werten sortieren
        List<Map.Entry<String, Double>> sortedListY = new ArrayList<>(unsortedMapY.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .toList());

        // Die Liste umdrehen
        Collections.reverse(sortedListY);


        // Ausgabe der sortierten List
        for (Map.Entry<String, Double> entry : sortedListX) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        for (Map.Entry<String, Double> entry : sortedListY) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }



    }

    @Test
    public void newCheckMapper() {
        Map<String, Double> unsortedMap1 = new HashMap<>();
        unsortedMap1.put("Wert3", 15.75);
        unsortedMap1.put("Wert1", 10.5);
        unsortedMap1.put("Wert2", 20.0);

        Map<String, Double> unsortedMap2 = new HashMap<>();
        unsortedMap2.put("Wert5", 8.0);
        unsortedMap2.put("Wert4", 12.0);
        unsortedMap2.put("Wert6", 18.0);

        // Sortiere die Maps
        List<Map.Entry<String, Double>> sortedList1 = unsortedMap1.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toList());

        List<Map.Entry<String, Double>> sortedList2 = unsortedMap2.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toList());
        Collections.reverse(sortedList1);
        Collections.reverse(sortedList2);

        // Erstelle eine Liste von Map-Objekten, jedes Map-Objekt enthält ein Paar aus sortedList1 und sortedList2
        List<Map<String, Double>> combinedList = new ArrayList<>();
        int maxSize = Math.min(sortedList1.size(), sortedList2.size());

        for (int i = 0; i < maxSize; i++) {
            Map<String, Double> combinedMap = new HashMap<>();
            combinedMap.put(sortedList1.get(i).getKey(), sortedList1.get(i).getValue());
            combinedMap.put(sortedList2.get(i).getKey(), sortedList2.get(i).getValue());
            combinedList.add(combinedMap);
        }

        // Füge die Liste von Map-Objekten in die resultierende Liste ein
        List<List<Map<String, Double>>> resultList = new ArrayList<>();
        resultList.add(combinedList);

        for (Map.Entry<String, Double> entry : sortedList1) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        for (Map.Entry<String, Double> entry : sortedList2) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Ausgabe der resultierenden Liste
        for (List<Map<String, Double>> list : resultList) {
            System.out.println(list);
        }
        System.out.println("Erstes Element  " + resultList.get(0));

    }

    @Test
    public void newCheckMapper2() {
        List<RessourceHolder> ressourcenList = new ArrayList<>();
        Map<String, Double> unsortedMapX = new HashMap<>();
        unsortedMapX.put("TV1", 15.75);
        unsortedMapX.put("TV3", 15.5);
        unsortedMapX.put("TV2", 21.0);

        Map<String, Double> unsortedMapY = new HashMap<>();
        unsortedMapY.put("TV2", 99.0);
        unsortedMapY.put("TV3", 1.0);
        unsortedMapY.put("TV1", 14.0);

        Map<String, Double> unsortedMapZ = new HashMap<>();
        unsortedMapZ.put("TV2", 66.0);
        unsortedMapZ.put("TV1", 89.0);
        unsortedMapZ.put("TV3", 1.0);


        List<Map.Entry<String, Double>> sortedListX = new ArrayList<>(unsortedMapX.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());

        Collections.reverse(sortedListX);
        // Elemente in eine List übertragen und nach den Werten sortieren
        List<Map.Entry<String, Double>> sortedListY = new ArrayList<>(unsortedMapY.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());

        // Die Liste umdrehen
        Collections.reverse(sortedListY);
        // Elemente in eine List übertragen und nach den Werten sortieren
        List<Map.Entry<String, Double>> sortedListZ = new ArrayList<>(unsortedMapZ.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .toList());

        // Die Liste umdrehen
        Collections.reverse(sortedListZ);
        System.out.println(sortedListX);
        System.out.println(sortedListY);
        System.out.println(sortedListZ);

        int maxSize1 = Math.min(sortedListX.size(), sortedListY.size());
        int maxSize = Math.min(maxSize1, sortedListZ.size());



        for (int i = 0; i < maxSize; i++) {
            RessourceHolder newRessource = RessourceHolder.builder()
                    .forceZ(new AbstractMap.SimpleEntry<>(sortedListX.get(i).getKey(),sortedListX.get(i).getValue()))
                    .forceX(new AbstractMap.SimpleEntry<>(sortedListY.get(i).getKey(),sortedListY.get(i).getValue()))
                    .forceY(new AbstractMap.SimpleEntry<>(sortedListZ.get(i).getKey(),sortedListZ.get(i).getValue()))
                    .build();

            ressourcenList.add(newRessource);
        }


        System.out.println(ressourcenList);









    }

}
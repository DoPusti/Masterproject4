package com.example.Masterproject4.Mapper;

import com.example.Masterproject4.XMLAttributeHolder.PropertyInformation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RequirementRowTest {

    @Test
    public void testRequirementRow() {
        List<RequirementRow> requirementTable = new ArrayList<>();
        for(int j=0;j<2;j++) {
            for(int i = 0; i < 5; i++) {
                if (requirementTable.size() > i + 1) {
                    System.out.println("Eintrag in Liste ist bereits vorhanden");
                    Map<String, PropertyInformation> requirementRow = requirementTable.get(i).getRequirementAttributes();
                    requirementRow.put("test" + i + j,new PropertyInformation());
                } else {
                    Map<String,PropertyInformation> newRequirmentRow = new HashMap<>();
                    newRequirmentRow.put("test" + i + j,new PropertyInformation());
                    RequirementRow requirementRowForList = RequirementRow.builder()
                            .requirementAttributes(newRequirmentRow)
                            .build();
                    requirementTable.add(requirementRowForList);
                }

            }

        }

        System.out.println(requirementTable);
    }

}
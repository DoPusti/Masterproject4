package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.Mapper.ProductRequirementMapper;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductProcessReferenceTest {

    @Test
    public void checkProductProcessReference() throws JAXBException {
        ProductRequirementMapper productRequirementMapper = new ProductRequirementMapper();
        File file = new File("src\\main\\resources\\ProductRequirementsForTest\\Product RequirementAnqi3.xml");
        ProductRequirementFullObject productRequirementFullObject = productRequirementMapper.mapXMLToClass(file);
        List<ProductProcessReference> listOfProductProcessReference = productRequirementMapper.getAllProductProcessReference(productRequirementFullObject);
        System.out.println(listOfProductProcessReference);
        listOfProductProcessReference.forEach(productProcess -> {
            switch (productProcess.getTvName()) {
                case "TV1_secondary_process", "TV3_secondary_process":
                    assertEquals("assembly_middle_of_housing",productProcess.getPartName());
                    assertEquals(425.49,productProcess.getMass());
                    assertEquals(0.5,productProcess.getMeanRoughness());
                    assertFalse(productProcess.isFerroMagnetic());
                    assertEquals(10,productProcess.getLength());
                    assertEquals(100,productProcess.getWidth());
                    assertEquals(40,productProcess.getHeight());
                    assertEquals(50,productProcess.getCenterOfMassX());
                    assertEquals(50,productProcess.getCenterOfMassY());
                    assertEquals(20,productProcess.getCenterOfMassZ());
                    break;
                case "TV2_secondary_process" :
                    assertEquals("bridging_element",productProcess.getPartName());
                    assertEquals(16.38,productProcess.getMass());
                    assertEquals(0.4,productProcess.getMeanRoughness());
                    assertFalse(productProcess.isFerroMagnetic());
                    assertEquals(20,productProcess.getLength());
                    assertEquals(7,productProcess.getWidth());
                    assertEquals(37.738,productProcess.getHeight());
                    assertEquals(10,productProcess.getCenterOfMassX());
                    assertEquals(3.5,productProcess.getCenterOfMassY());
                    assertEquals(8.972,productProcess.getCenterOfMassZ());
                    break;
                case "TV4_secondary_process" :
                    assertEquals("assembly_top_of_housing",productProcess.getPartName());
                    assertEquals(569.96,productProcess.getMass());
                    assertEquals(0.5,productProcess.getMeanRoughness());
                    assertFalse(productProcess.isFerroMagnetic());
                    assertEquals(100,productProcess.getLength());
                    assertEquals(100,productProcess.getWidth());
                    assertEquals(47.5,productProcess.getHeight());
                    assertEquals(50,productProcess.getCenterOfMassX());
                    assertEquals(50,productProcess.getCenterOfMassY());
                    assertEquals(14.092,productProcess.getCenterOfMassZ());
                    break;
            }

        });

    }

}
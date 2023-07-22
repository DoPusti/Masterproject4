package com.example.Masterproject4.Entity;

import com.example.Masterproject4.Repository.ProductRequirementRepository;
import lombok.experimental.FieldNameConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XMLStructureFullObjectDBClassTest {
    @Autowired
    private ProductRequirementRepository productRequirementRepository;

    @Test
    public void getAllRequirements() {
        List<ProductRequirementDBClass> requirementDBClassList = productRequirementRepository.findAll();
        System.out.println("requirementDBClassList = " + requirementDBClassList);
    }
}
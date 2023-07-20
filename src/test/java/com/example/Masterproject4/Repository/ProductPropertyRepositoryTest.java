package com.example.Masterproject4.Repository;

import com.example.Masterproject4.Entity.ProcessRequirementDBClass;
import com.example.Masterproject4.Entity.ProductPropertyDBClass;
import com.example.Masterproject4.Entity.ProductRequirementDBClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductPropertyRepositoryTest {

    @Autowired
    private ProductPropertyRepository productPropertyRepository;

    @Test
    public void saveProductRequirement() {
        ProductRequirementDBClass productRequirement = ProductRequirementDBClass.builder()
                .assetId("12345")
                .build();
        ProductPropertyDBClass processRequirement = ProductPropertyDBClass.builder()
                .productRequirement(productRequirement)
                .x(1.2)
                .ferroMagnetic(true)
                .build();

        productPropertyRepository.save(processRequirement);

    }

}
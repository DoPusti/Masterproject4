package com.example.Masterproject4.Repository;

import com.example.Masterproject4.Entity.ProductRequirementDBClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRequirementRepositoryTest {

    @Autowired
    private ProductRequirementRepository productRequirementRepository;

    @Test
    public void saveProductRequirements() {
        ProductRequirementDBClass productRequirementDBClass = ProductRequirementDBClass.builder()
                .assetId("1234").build();
        productRequirementRepository.save(productRequirementDBClass);

    }

    @Test
    public void getProductRequirementByAssetId() {
        List<ProductRequirementDBClass> listOfPR = productRequirementRepository.findByAssetId("1234");
        System.out.println(listOfPR);
    }

    @Test
    public void printProductRequirementByAssetId() {
        ProductRequirementDBClass productRequirementDBClass = productRequirementRepository.getProductRequirementDbClassByAssetId("1234");
        System.out.println(productRequirementDBClass);
    }

    @Test
    public void printProductRequirementByAssetIdNative() {
        ProductRequirementDBClass productRequirementDBClass = productRequirementRepository.getProductRequirementDbClassByAssetIdNative("1234");
        System.out.println(productRequirementDBClass);
    }

    @Test
    public void printProductRequirementByAssetIdNativeParam() {
        ProductRequirementDBClass productRequirementDBClass = productRequirementRepository.getProductRequirementDbClassByAssetIdNativeParam("1234");
        System.out.println(productRequirementDBClass);
    }

    @Test
    public void updateProductRequirement() {

        System.out.println(productRequirementRepository.updateProductRequirementByAssetId("12345678","1234"));
    }





}
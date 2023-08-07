package com.example.Masterproject4.Repository;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AssuranceRepositoryTest {

    @Autowired
    private AssuranceRepository assuranceRepository;

    @Test
    public void getAllValues() {
        List<AssuranceFullObject> newList = assuranceRepository.findAll();
        System.out.println(newList);

    }


}
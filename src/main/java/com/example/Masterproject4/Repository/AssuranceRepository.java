package com.example.Masterproject4.Repository;

import com.example.Masterproject4.Entity.AssuranceFullObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssuranceRepository extends JpaRepository<AssuranceFullObject, Long> {

}

package com.example.Masterproject4.Repository;

import com.example.Masterproject4.Entity.ProductPropertyDBClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPropertyRepository  extends JpaRepository<ProductPropertyDBClass,Long> {


}

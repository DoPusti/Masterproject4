package com.example.Masterproject4.Repository;

import com.example.Masterproject4.Entity.ProductRequirementDBClass;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRequirementRepository extends JpaRepository<ProductRequirementDBClass, Long> {

    List<ProductRequirementDBClass> findByAssetId(String assetId);

    //JPQL
    @Query("select s from ProductRequirementDBClass s where s.assetId = ?1")
    ProductRequirementDBClass getProductRequirementDbClassByAssetId(String assetId);

    //Native
    @Query(value = "select * from tbl_product_requirement s where s.asset_id = ?1",
            nativeQuery = true)
    ProductRequirementDBClass getProductRequirementDbClassByAssetIdNative(String assetId);

    //Native Param
    @Query(value = "select * from tbl_product_requirement s where s.asset_id = :assetId",
            nativeQuery = true)
    ProductRequirementDBClass getProductRequirementDbClassByAssetIdNativeParam(@Param("assetId") String assetId);

    @Transactional
    @Modifying
    @Query(value = "update tbl_product_requirement set asset_id = :assetId where asset_id = :assetIdUpdate", nativeQuery = true)
    int updateProductRequirementByAssetId(@Param("assetId") String assetId, @Param("assetIdUpdate") String assetIdToUpdate);


}

package com.example.Masterproject4.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public class ProductRequirementMapper {

    ProductRequirementMapper INSTANCE = Mappers.getMapper(ProductRequirementMapper.class);

}

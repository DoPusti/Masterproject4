package com.example.Masterproject4.Mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeToValue {
    String subProcessId;
    double valueOfParameter;
    boolean stabilityGiven;

}

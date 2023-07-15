package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"preferredName","shortName","unit","dataType","definition"})
public class DataSpecificationIEC61360 {

    @XmlElement(name="preferredName")
    private String preferredName;

    @XmlElement(name="shortName")
    private String shortName;

    @XmlElement(name="unit")
    private String unit;

    @XmlElement(name="dataType")
    private String dataType;

    @XmlElement(name="definition")
    private Definition definition;


}

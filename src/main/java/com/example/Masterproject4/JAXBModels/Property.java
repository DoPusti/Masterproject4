package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"idShort","category","kind","semanticId","qualifier","valueType","value"})
public class Property {

    @XmlElement(name="idShort")
    private String idShort;

    @XmlElement(name="category")
    private String category;

    @XmlElement(name="kind")
    private String kind;

    @XmlElement(name="semanticId")
    private SemanticId semanticId;

    @XmlElement(name="qualifier")
    private String qualifier;

    @XmlElement(name="valueType")
    private String valueType;

    @XmlElement(name="value")
    private String value;

}

package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"property","submodelElementCollection","range"})
public class SubModelElement {
    @XmlElement(name="property", required = false)
    private Property property;

    @XmlElement(name="submodelElementCollection")
    private SubmodelElementCollection submodelElementCollection;

    @XmlElement(name="range")
    private Range range;




}

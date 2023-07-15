package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

import java.util.List;
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = "conceptDescription")
//@XmlType(propOrder = {"idShort","identification","embeddedDataSpecification"})
public class ConceptDescriptions {

    @XmlElement(name = "conceptDescription")
    private List<ConceptDescription> conceptDescription;


}

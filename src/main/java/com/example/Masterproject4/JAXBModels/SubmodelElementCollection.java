package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"idShort","category","kind","semanticId","qualifier","ordered","allowDuplicates","value"})
public class SubmodelElementCollection {
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

    @XmlElement(name="ordered")
    private Boolean ordered;

    @XmlElement(name="allowDuplicates")
    private Boolean allowDuplicates;

    @XmlElement(name="value")
    private Value value;


}

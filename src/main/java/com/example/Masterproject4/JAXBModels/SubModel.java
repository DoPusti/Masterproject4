package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"idShort","identification","kind","semanticId","qualifier","submodelElements"})
public class SubModel {
    @XmlElement(name="idShort")
    private String idShort;

    @XmlElement(name="identification")
    private String identification;

    @XmlElement(name="kind")
    private String kind;

    @XmlElement(name="semanticId")
    private SemanticId semanticId;

    @XmlElement(name="qualifier")
    private String qualifier;

    @XmlElement(name ="submodelElements")
    private SubModelElements submodelElements;

}

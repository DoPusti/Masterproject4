package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"idShort", "identification", "assetRef", "submodelRefs","conceptDictionaries"})
public class AssetAdministrationShell {

    @XmlElement(name ="idShort")
    private String idShort;

    @XmlElement(name ="identification")
    private String identification;

    @XmlElement(name ="assetRef")
    private AssetRef assetRef;

    @XmlElement(name ="submodelRefs")
    private SubModelRefs submodelRefs;

    @XmlElement(name ="conceptDictionaries")
    private String conceptDictionaries;
}

package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"assetAdministrationShell"})
//@XmlType(propOrder = {"idShort", "identification", "assetRef", "submodelRefs","conceptDictionaries"})
public class AssetAdministrationShells {

    @XmlElement(name = "assetAdministrationShell")
    private AssetAdministrationShell assetAdministrationShell;


}

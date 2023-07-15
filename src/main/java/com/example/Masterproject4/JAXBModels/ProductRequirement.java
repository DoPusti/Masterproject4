package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlRootElement(name ="aasenv")
@XmlType(propOrder = {"assetAdministrationShells","assets","submodels","conceptDescriptions"})
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRequirement {
    @XmlElement(name="assetAdministrationShells")
    private AssetAdministrationShells assetAdministrationShells;

    @XmlElement(name="assets")
    private Assets assets;

    @XmlElement(name="submodels")
    private SubModels submodels;

    @XmlElement(name="conceptDescriptions")
    private ConceptDescriptions conceptDescriptions;
}

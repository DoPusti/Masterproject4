package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"asset"})
public class Assets {

    @XmlElement(name = "asset")
    private Asset asset;


}

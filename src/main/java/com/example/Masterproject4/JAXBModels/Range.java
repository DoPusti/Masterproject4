package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"idShort","min","max"})
public class Range {

    @XmlElement(name = "idShort")
    private String idShort;

    @XmlElement(name = "min")
    private String min;

    @XmlElement(name = "max")
    private String max;
}

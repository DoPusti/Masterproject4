package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"dataSpecificationIEC61360"})
public class DataSpecification {
    @XmlElement(name="dataSpecificationIEC61360")
    private DataSpecificationIEC61360 dataSpecificationIEC61360;
}

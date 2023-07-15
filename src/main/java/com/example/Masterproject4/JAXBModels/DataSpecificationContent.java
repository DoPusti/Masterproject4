package com.example.Masterproject4.JAXBModels;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlType(propOrder = {"dataSpecificationIEC61360"})
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSpecificationContent {
    @XmlElement(name = "dataSpecificationIEC61360")
    private DataSpecificationIEC61360 dataSpecificationIEC61360;
}

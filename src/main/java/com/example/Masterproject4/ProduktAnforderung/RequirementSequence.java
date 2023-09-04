package com.example.Masterproject4.ProduktAnforderung;

import com.example.Masterproject4.Mapper.AttributeToValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequirementSequence {

    List<Map<String, AttributeToValue>> parameter;

}

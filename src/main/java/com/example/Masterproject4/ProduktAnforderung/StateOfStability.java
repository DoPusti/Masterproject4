package com.example.Masterproject4.ProduktAnforderung;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateOfStability {
    int idOfSubProcess;
    boolean givenStability;
}

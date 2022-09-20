package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nomenclature {
    private int nomenclatureId;
    private String title;
    private String description;
    private double price;
}

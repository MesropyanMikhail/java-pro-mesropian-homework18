package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabularPart {
    private int tabularPartId;
    private List<Nomenclature> nomenclatureId;
    private double quantity;

    public TabularPart(int tabularPartId, double quantity) {
        this.tabularPartId = tabularPartId;
        this.quantity = quantity;
    }
}

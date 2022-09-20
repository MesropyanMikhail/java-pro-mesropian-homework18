package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabularPart {
    private int tabular_part_id;
    private List<Nomenclature> nomenclature_id;
    private double quantity;

    public TabularPart(int tabular_part_id, double quantity) {
        this.tabular_part_id = tabular_part_id;
        this.quantity = quantity;
    }
}

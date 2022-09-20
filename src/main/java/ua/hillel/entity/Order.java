package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int order_id;
    private List<TabularPart> tabular_part_id;
    private Date date_receipt;

    public Order(int order_id, Date date_receipt) {
        this.order_id = order_id;
        this.date_receipt = date_receipt;
    }
}

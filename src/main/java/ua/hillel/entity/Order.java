package ua.hillel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderId;
    private List<TabularPart> tabularPartId;
    private LocalDate dateReceipt;

    public Order(int orderId, LocalDate dateReceipt) {
        this.orderId = orderId;
        this.dateReceipt = dateReceipt;
    }
}

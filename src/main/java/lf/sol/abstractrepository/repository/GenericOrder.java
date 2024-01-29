package lf.sol.abstractrepository.repository;

import lombok.Getter;

@Getter
public class GenericOrder {

    private final boolean ascending;
    private final String field;

    private GenericOrder(boolean ascending, String field) {
        this.ascending = ascending;
        this.field = field;
    }

    public static GenericOrder getOrder(String order){
        return order.contains("-") ? new GenericOrder(false, order.substring(1))
                : new GenericOrder(true, order);
    }
}

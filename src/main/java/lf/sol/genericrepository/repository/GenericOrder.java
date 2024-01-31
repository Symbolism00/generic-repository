package lf.sol.genericrepository.repository;

import lombok.Getter;

@Getter
public class GenericOrder {

    private final boolean ascending;
    private final String field;
    private static final String DASH_ORDER = "-";
    private static final int DASH_ORDER_INDEX = 1;

    private GenericOrder(boolean ascending, String field) {
        this.ascending = ascending;
        this.field = field;
    }

    /**
     * Method that generates the generic order
     * To generate an asc order the value just needs to be the entity property name, for example "designation"
     * To generate a desc order the value needs to have a dash followed by the property name of the entity,
     * for example "-designation"
     * @param order the value to generate a generic order
     * @return the generic order
     */
    public static GenericOrder getOrder(String order){
        return order.contains(DASH_ORDER) ? new GenericOrder(false, order.substring(DASH_ORDER_INDEX))
                : new GenericOrder(true, order);
    }

    /**
     * Method that generates generic orders
     * To generate an asc order the value just needs to be the entity property name, for example "designation"
     * To generate a desc order the value needs to have a dash followed by the property name of the entity,
     * for example "-designation"
     * @param orders the values to generate a generic orders
     * @return the generic orders
     */
    public static GenericOrder[] getOrders(String... orders){
        GenericOrder[] genericOrders = new GenericOrder[orders.length];
        // gets each order
        int i = 0;
        for(String order : orders){
            genericOrders[i] = getOrder(order);
            i++;
        }
        return genericOrders;
    }
}

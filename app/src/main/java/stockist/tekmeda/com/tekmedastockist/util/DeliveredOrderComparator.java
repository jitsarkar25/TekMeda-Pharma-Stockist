package stockist.tekmeda.com.tekmedastockist.util;

import java.util.Comparator;

import stockist.tekmeda.com.tekmedastockist.bean.Orders;
public class DeliveredOrderComparator implements Comparator<Orders> {

    @Override
    public int compare(Orders orders, Orders o2) {
        Long l1 = Long.parseLong(orders.getTime());
        Long l2 = Long.parseLong(o2.getTime());
        if(l1.longValue() < l2.longValue())
            return -1;
        else if(l1.longValue() > l2.longValue())
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }
}

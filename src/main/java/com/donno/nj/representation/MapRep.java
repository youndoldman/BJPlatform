package com.donno.nj.representation;

import java.util.Map;

public class MapRep {

    private Integer total;
    private Map items;

    public static MapRep assemble(Map items, Integer total) {
        MapRep rep = new MapRep();
        rep.setItems(items);

        rep.setTotal(total != null ? total : items.size());
        return rep;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Map getItems() {
        return items;
    }

    public void setItems(Map items) {
        this.items = items;
    }
}

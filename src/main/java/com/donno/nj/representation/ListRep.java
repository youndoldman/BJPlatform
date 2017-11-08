package com.donno.nj.representation;

import java.util.List;

public class ListRep {

    private Integer total;
    private List items;

    public static ListRep assemble(List items, Integer total) {
        ListRep rep = new ListRep();
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

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}

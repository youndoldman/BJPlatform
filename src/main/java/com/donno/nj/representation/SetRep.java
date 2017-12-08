package com.donno.nj.representation;

import java.util.Set;

public class SetRep {

    private Integer total;
    private Set items;

    public static SetRep assemble(Set items, Integer total) {
        SetRep rep = new SetRep();
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

    public Set getItems() {
        return items;
    }

    public void setItems(Set items) {
        this.items = items;
    }
}

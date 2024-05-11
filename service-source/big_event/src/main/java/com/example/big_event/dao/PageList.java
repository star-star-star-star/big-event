package com.example.big_event.dao;

public class PageList {
    private Number num;    //该页所含有的数据记录条数
    private Object records; //该页所含有的数据记录

    public PageList(Number num, Object records) {
        this.num = num;
        this.records = records;
    }

    public Number getNum() {
        return num;
    }

    public void setNum(Number num) {
        this.num = num;
    }

    public Object getRecords() {
        return records;
    }

    public void setRecords(Object records) {
        this.records = records;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageList{");
        sb.append("num=").append(num);
        sb.append(", records=").append(records);
        sb.append('}');
        return sb.toString();
    }
}

package com.kingcore.framework.base.view;

/**
 *
 * @author：zewen.wu
 */
import java.util.List;

public class Page {
    private int start;
    private int end;
    private int count;
    private int pageSize;
    private int pageCount;
    private List datas;
public Page(List datas, int pageSize, int start, int end, int count) {
    this.start = start;
    this.end = end;
    this.pageSize = pageSize;
    this.count = count;
    this.datas = datas;
    this.pageCount = (count - 1) / pageSize + 1;
}
    public int getCount()
    {
        return count;
    }
public List getDatas() {
    return datas;
}
public int getPageCount() {
    return pageCount;
}
    public void setCount(int count) {
        this.count = count;
    }
    public void setDatas(List datas) {
        this.datas = datas;
    }
}

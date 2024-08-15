package com.ovo307000.bigevent.response;

import java.util.List;

public class Page<T>
{
    private Long    pageNumber;
    private List<T> elements;

    public Page(Long pageNumber, List<T> elements)
    {
        this.pageNumber = pageNumber;
        this.elements   = elements;
    }

    public Page()
    {
    }

    public Long getPageNumber()
    {
        return this.pageNumber;
    }

    public void setPageNumber(Long pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public List<T> getElements()
    {
        return this.elements;
    }

    public void setElements(List<T> elements)
    {
        this.elements = elements;
    }
}

package com.olegrum;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "settings")
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings {
    private page page;
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    private List<column> columns;

    public page getPage() {
        return page;
    }

    public void setPage(page page) {
        this.page = page;
    }

    public List<column> getColumns() {
        return columns;
    }

    public void setColumns(List<column> columns) {
        this.columns = columns;
    }
}

class page {
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}

class column {
    private String title;
    private int width;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
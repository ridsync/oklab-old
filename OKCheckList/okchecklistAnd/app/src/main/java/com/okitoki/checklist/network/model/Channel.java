package com.okitoki.checklist.network.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class Channel extends BaseModel{

    @Attribute(required = false)
    String version;

    @Element(name = "title")
    String title;

    @Element(name = "link")
    String link;

    @Element(name = "description")
    String description;

    @Element(name = "lastBuildDate")
    String lastBuildDate;

    @Element(name = "total")
    long total;

    @Element(name = "start")
    int start;

    @Element(name = "display")
    int display;

    @ElementList(required = false, inline = true, entry="item" )
    List<ProductItem> item;

    public Channel() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public List<ProductItem> getItem() {
        return item;
    }

    public void setItem(List<ProductItem> item) {
        this.item = item;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
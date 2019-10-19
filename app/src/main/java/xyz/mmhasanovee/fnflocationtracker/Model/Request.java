package xyz.mmhasanovee.fnflocationtracker.Model;

import java.util.Map;

public class Request {

    public String to;
    public Map<String,String> data;

    public Request() {

    }

    public Request(String to, Map<String, String> data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}

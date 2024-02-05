package com.kelloggs.promotions.lib.model;
/**
* Add UserGeneratedContent for the user_generated_content Entity
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 30th January 2024
*/
public class GetUGCResponse {

    private Integer ugcId;

    private String name;

    private String start;

    private String end;

    private Integer count;

    public GetUGCResponse() {
    }

    public GetUGCResponse(Integer ugcId, String name, String start, String end, Integer count) {
        this.ugcId = ugcId;
        this.name = name;
        this.start = start;
        this.end = end;
        this.count = count;
    }

    public Integer getUgcId() {
        return ugcId;
    }

    public void setUgcId(Integer ugcId) {
        this.ugcId = ugcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    
}

package com.kelloggs.promotions.lib.model;
/**
* Add CreateUGCRequest for the user_generated_content Request
* 
* @author NARASIMHARAO MANNEPALLI (10700939)
* @since 29th January 2024
*/
public class CreateUGCRequest {    

    private String name;

    private String start;

    private String end;
    
    public CreateUGCRequest() {
    }
    public CreateUGCRequest(String name, String start, String end) {
        this.name = name;
        this.start = start;
        this.end = end;
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

    
}

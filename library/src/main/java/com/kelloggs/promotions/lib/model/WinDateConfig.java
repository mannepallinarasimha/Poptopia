package com.kelloggs.promotions.lib.model;

public class WinDateConfig {

	private  String start;
	
	private String end;
	
	private Integer winCount;
	
	public WinDateConfig() {
      //default
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

	public Integer getWinCount() {
		return winCount;
	}

	public void setWinCount(Integer winCount) {
		this.winCount = winCount;
	}

	@Override
	public String toString() {
		return "WinDateConfig [start=" + start + ", end=" + end + ", winCount=" + winCount + "]";
	}

	
	
}

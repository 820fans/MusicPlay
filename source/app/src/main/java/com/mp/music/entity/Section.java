package com.mp.music.entity;

public class Section {

	private int id;
	private int img;
	private String section;

	public Section(int id,int img,String section){
		this.id=id;
		this.img=img;
		this.section=section;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
}

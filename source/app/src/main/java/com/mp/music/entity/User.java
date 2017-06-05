package com.mp.music.entity;


import com.mp.music.base.BaseEntity;

public class User extends BaseEntity {

	private int id;
	private String account;
	private String introduce;
	private String avatar;
	private String brand;
	private int exp;
	private int needexp;
	private int levelexp;
	private int level;

	public User(int id,String account,String avatar,String introduce,String brand,
				int exp,int needexp,int levelexp,int level){

		this.id=id;
		this.account=account;
		this.introduce=introduce;
		this.avatar=avatar;
		this.brand=brand;
		this.exp=exp;
		this.needexp=needexp;
		this.levelexp=levelexp;
		this.level=level;

	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getNeedexp() {
		return needexp;
	}

	public void setNeedexp(int needexp) {
		this.needexp = needexp;
	}

	public int getLevelexp() {
		return levelexp;
	}

	public void setLevelexp(int levelexp) {
		this.levelexp = levelexp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
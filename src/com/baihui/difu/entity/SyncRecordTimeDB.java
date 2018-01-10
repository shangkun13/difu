package com.baihui.difu.entity;

import java.util.Date;

public class SyncRecordTimeDB {

	/**编号*/
	private int id;
	
	/**模块名称*/
	private String module;
	
	/**开始同步时间*/
	private Date thisSyncTime;
	
	/**结束同步时间*/
	private Date lastSyncTime;
	
	/**创建时间*/
	private Date createdTime;
	
	/**修改时间*/
	private Date modifiedTime;
	
	/**描述*/
	private String description;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setThisSyncTime(Date thisSyncTime) {
		this.thisSyncTime = thisSyncTime;
	}

	public Date getThisSyncTime() {
		return thisSyncTime;
	}

	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
	
}

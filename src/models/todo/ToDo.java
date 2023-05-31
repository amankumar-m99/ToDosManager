package models.todo;

import java.sql.Timestamp;

public class ToDo{
	private int id;
	private String title;
	private String description;
	private ToDoStatus status;
	private Timestamp creationTimestamp;
	private Timestamp modificationTimestamp;
	public ToDo(int id, String title, String description, ToDoStatus status, Timestamp creationTimestamp, Timestamp modificationTimestamp) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.status = status;
		this.creationTimestamp = creationTimestamp;
		this.modificationTimestamp = modificationTimestamp;
	}
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ToDoStatus getStatus() {
		return status;
	}
	public String getStatusAsString() {
		return status.toString();
	}
	public void setStatus(ToDoStatus toDoStatus) {
		this.status = toDoStatus;
	}
	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}
	@SuppressWarnings("deprecation")
	public String getCreationTimestampAsString() {
		return creationTimestamp.toLocaleString();
	}
	public void setModificationTimestamp(Timestamp timestamp) {
		this.modificationTimestamp = timestamp;
	}
	public Timestamp getModificationTimestamp() {
		return modificationTimestamp;
	}
	@SuppressWarnings("deprecation")
	public String getModificationTimestampAsString() {
		return modificationTimestamp.toLocaleString();
	}
}
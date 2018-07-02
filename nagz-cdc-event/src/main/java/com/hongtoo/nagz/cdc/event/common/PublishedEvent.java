package com.hongtoo.nagz.cdc.event.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An event that is published to Kafka
 */
public class PublishedEvent {

	// 数据库(NAGZ_****)
	private String database;
	// 表名(APP_***)
	private String table;
	// 实例ID(id）
	private String entityID;
	// 事件类型(insert/update/delete)
	private Operation operationType;

	public PublishedEvent() {

	}

	public PublishedEvent(String db, String table, String entityID,
			Operation type) {
		this.database = db;
		this.table = table;
		this.entityID = entityID;
		this.operationType = type;
	}

	@JsonIgnore  
	public String getKey() {
		return entityID + "_" + operationType.code;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getEntityID() {
		return entityID;
	}

	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}

	public Operation getOperationType() {
		return operationType;
	}

	public void setOperationType(Operation operationType) {
		this.operationType = operationType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((database == null) ? 0 : database.hashCode());
		result = prime * result
				+ ((entityID == null) ? 0 : entityID.hashCode());
		result = prime * result
				+ ((operationType == null) ? 0 : operationType.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PublishedEvent other = (PublishedEvent) obj;
		if (database == null) {
			if (other.database != null)
				return false;
		} else if (!database.equals(other.database))
			return false;
		if (entityID == null) {
			if (other.entityID != null)
				return false;
		} else if (!entityID.equals(other.entityID))
			return false;
		if (operationType != other.operationType)
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PublishedEvent [database=" + database + ", table=" + table
				+ ", entityID=" + entityID + ", operationType=" + operationType
				+ "]";
	}

	/**
	 * The constants for the values for the {@link FieldName#OPERATION
	 * operation} field in the message envelope.
	 */
	public static enum Operation {
		/**
		 * The operation that read the current state of a record, most typically
		 * during snapshots.
		 */
		READ("r"),
		/**
		 * An operation that resulted in a new record being created in the
		 * source.
		 */
		CREATE("c"),
		/**
		 * An operation that resulted in an existing record being updated in the
		 * source.
		 */
		UPDATE("u"),
		/**
		 * An operation that resulted in an existing record being removed from
		 * or deleted in the source.
		 */
		DELETE("d");
		private final String code;

		private Operation(String code) {
			this.code = code;
		}

		public static Operation forCode(String code) {
			for (Operation op : Operation.values()) {
				if (op.code().equalsIgnoreCase(code))
					return op;
			}
			return null;
		}

		public String code() {
			return code;
		}
	}
}

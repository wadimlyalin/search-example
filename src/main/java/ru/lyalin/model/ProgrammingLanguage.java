package ru.lyalin.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Programming language
 * @author Vadim Lyalin
 */
public class ProgrammingLanguage implements Serializable {
	/** name */
	@JsonProperty("Name")
	private String name;
	/** type */
	@JsonProperty("Type")
	private String type;
	/** designed by */
	@JsonProperty("Designed by")
	private String designedBy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesignedBy() {
		return designedBy;
	}

	public void setDesignedBy(String designedBy) {
		this.designedBy = designedBy;
	}
}

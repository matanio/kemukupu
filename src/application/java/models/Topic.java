package application.java.models;


/**
 * This class is used to dynamically load topic buttons, where each txt file corresponding
 * to a word list can be described as a Topic. 
 *
 */
public class Topic {

	private String name = "Default Topic Name";
	private String description = "This is the default for a new topic created";
	private String iconSrc = "default.png"; 

	// Constructors
	public Topic(String name, String description, String iconSrc) {
		this.name = name;
		this.iconSrc = iconSrc;
		this.description = description;
	}

	public Topic(String name, String iconSrc) {
		this.name = name;
		this.iconSrc = iconSrc;
	}

	public Topic(String name) {
		this.name = name;
	}

	// Getters and Setters (auto-generated)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconSrc() {
		return iconSrc;
	}

	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
	}


}

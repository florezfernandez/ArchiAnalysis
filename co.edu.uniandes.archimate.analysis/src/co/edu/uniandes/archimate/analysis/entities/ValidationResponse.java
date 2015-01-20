package co.edu.uniandes.archimate.analysis.entities;

import java.util.ArrayList;
import java.util.List;

import com.archimatetool.model.IArchimateElement;

public class ValidationResponse {

	private String title;
	private List<ValidationMessage> messages;

	public boolean isValid() {
		for (ValidationMessage message : messages) {
			if(message.getType().equals(ValidationMessageType.ERROR))
				return false;
		}
		return true;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ValidationMessage> getMessages() {
		return messages;
	}

	public void addMessage(ValidationMessage message) {
		this.messages.add(message);
	}

	public void addMessage(ValidationMessageType type, String message, IArchimateElement element) {
		this.messages.add(new ValidationMessage(type,element,message));
	}

	public void addInfo(String message, IArchimateElement element) {
		this.messages.add(new ValidationMessage(ValidationMessageType.INFORMATION,element,message));
	}
	
	public void addInfo(String message) {
		this.messages.add(new ValidationMessage(ValidationMessageType.INFORMATION,null,message));
	}

	public void addWarning(String message, IArchimateElement element) {
		this.messages.add(new ValidationMessage(ValidationMessageType.WARNING,element,message));
	}
	
	public void addWarning(String message) {
		this.messages.add(new ValidationMessage(ValidationMessageType.WARNING,null,message));
	}

	public void addError(String message, IArchimateElement element) {
		this.messages.add(new ValidationMessage(ValidationMessageType.ERROR,element,message));
	}

	public void addError(String message) {
		this.messages.add(new ValidationMessage(ValidationMessageType.ERROR,null,message));
	}
	
	public ValidationResponse() {
		this.title = "";
		this.messages = new ArrayList<ValidationMessage>();
	}

	public ValidationResponse(String title) {
		super();
		this.title = title;
		this.messages = new ArrayList<ValidationMessage>();
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("-----------------------------------").append(System.getProperty("line.separator"));
		sb.append("-----------------------------------").append(System.getProperty("line.separator"));
		sb.append(this.title).append(System.getProperty("line.separator"));
		sb.append("-----------------------------------").append(System.getProperty("line.separator"));

		sb.append("Information messages:").append(System.getProperty("line.separator"));
		for (ValidationMessage message : messages) {
			if(message.getType().equals(ValidationMessageType.INFORMATION))
				sb.append("   - ").append(message.toString()).append(System.getProperty("line.separator"));
		}

		sb.append(System.getProperty("line.separator"));
		sb.append("Warnings:").append(System.getProperty("line.separator"));
		for (ValidationMessage message : messages) {
			if(message.getType().equals(ValidationMessageType.WARNING))
				sb.append("   - ").append(message.toString()).append(System.getProperty("line.separator"));
		}

		sb.append(System.getProperty("line.separator"));
		sb.append("Errors:").append(System.getProperty("line.separator"));
		for (ValidationMessage message : messages) {
			if(message.getType().equals(ValidationMessageType.ERROR))
				sb.append("   - ").append(message.toString()).append(System.getProperty("line.separator"));
		}

		sb.append("-----------------------------------").append(System.getProperty("line.separator"));
		return sb.toString();
	}

}

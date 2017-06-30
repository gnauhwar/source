package com.integra.Mhat.ReadData.bean;


public class NotesBean {
	private int patientId;
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	private String date;
	private String clinician;
	private String Questionaire;
	private String attachment;
	private String notes;
	private String diagnosis;
	private String disorder;
	private String no;
	private String patientNumber;
	private String patientForeName;
	private String patientSurname;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getClinician() {
		return clinician;
	}
	public void setClinician(String clinician) {
		this.clinician = clinician;
	}
	public String getQuestionaire() {
		return Questionaire;
	}
	public void setQuestionaire(String questionaire) {
		Questionaire = questionaire;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getDisorder() {
		return disorder;
	}
	public void setDisorder(String disorder) {
		this.disorder = disorder;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getPatientNumber() {
		return patientNumber;
	}
	public void setPatientNumber(String patientNumber) {
		this.patientNumber = patientNumber;
	}
	public String getPatientForeName() {
		return patientForeName;
	}
	public void setPatientForeName(String patientForeName) {
		this.patientForeName = patientForeName;
	}
	public String getPatientSurname() {
		return patientSurname;
	}
	public void setPatientSurname(String patientSurname) {
		this.patientSurname = patientSurname;
	}
}
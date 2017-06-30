package com.integra.Mhat.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//import com.Integra.Mhat.ReadData.NotesRead;
//import com.Integra.Mhat.ReadData.PatientDetailsRead;
//import com.Integra.Mhat.ReadData.bean.NotesBean;
//import com.Integra.Mhat.ReadData.bean.PatientDetailsBean;

public class PatientDetailsDao {
	String url;
	Connection conn;

	public PatientDetailsDao() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		url = "jdbc:mysql://localhost:3306/test";
		conn = DriverManager.getConnection(url, "root", "admin123");
	}

	//@SuppressWarnings("unchecked")
	/*public void insertPatientDetails(String fileName) throws Exception {
  
		PatientDetailsBean pd = new PatientDetailsBean();
		//PatientDetailsRead t = new PatientDetailsRead();
		Map<String,String> map = PatientDetailsRead1.patientDetails(fileName);
		pd.setPatientNumber(map.get("Patient Number"));
		pd.setPatientForeName(map.get("Patient ForeName"));
		pd.setPatientSurname(map.get("Patient SurName"));
		String dob=map.get("Date Of Birth");
		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Date patientDOB= (Date)inputFormat.parse(dob);
		System.out.println("patientDOB:::"+patientDOB);
		String date=outputFormat.format(patientDOB);
		System.out.println("date:::"+date);
		pd.setDob(PatientDetailsRead.dateFormat(dob));
		pd.setGender(map.get("Gender"));
		System.out.println("Religion: "+map.get("Religion"));
		pd.setReligion(map.get("Religion"));
		pd.setAddress(map.get("Address"));
		pd.setPlace(map.get("Place"));
		pd.setPin(map.get("PIN"));
		pd.setPhoneNumber(map.get("Telephone number"));
		pd.setMobileNumber(map.get("Mobile Number"));
		pd.setEmail(map.get("Email Address"));
		pd.setCareTakerForeName(map.get("CareTakerForeName"));
		pd.setCareTakerSurname(map.get("careTakerSurName"));
		pd.setCareTakerAddress(map.get("careTakerAddress"));
		pd.setCareTakerPlace(map.get("careTakerPlace"));
		pd.setCareTakerPin(map.get("careTakerPIN"));
		pd.setCareTakerTelephone(map.get("CareTakerTelephone"));
		pd.setCareTakerMobile(map.get("CareTakerMobile"));
		pd.setCareTakerEmail(map.get("careTakerEmail"));
		System.out.println(pd.getPatientNumber()+", "+pd.getPatientForeName()+","+ pd.getPatientSurname()+","+ pd.getDob()+","+pd.getGender()+","+pd.getReligion()+","+pd.getAddress()+","+pd.getPlace()+","+pd.getPin()+","+pd.getPhoneNumber()+","+pd.getMobileNumber()+","+pd.getEmail()+","+pd.getCareTakerForeName()+","+pd.getCareTakerSurname()+","+pd.getCareTakerAddress()+","+pd.getCareTakerPlace()+","+pd.getCareTakerPin()+","+pd.getCareTakerTelephone()+","+pd.getCareTakerMobile()+","+pd.getCareTakerEmail()+")");
	//	'"+user+"','"+age+"','"+school+"','"+password+"'
		String sql = "INSERT INTO patientDetails(patientNumber,PatientForeName,PatientSurname,dob,Gender,Religion,Address,Place,PIN,PhoneNumber,MobileNumber,Email,careTakerForeName,careTakerSurname,careTakerAddress,careTakerPlace,careTakerPin,careTakerTelephone,careTakerMobile,careTakerEmail) "
				+ "VALUES ('"+pd.getPatientNumber()+"','"+pd.getPatientForeName()+"','"+ pd.getPatientSurname()+"','"+ pd.getDob()+"','"+pd.getGender()+"','"+pd.getReligion()+"','"+pd.getAddress()+"','"+pd.getPlace()+"','"+pd.getPin()+"','"+pd.getPhoneNumber()+"','"+pd.getMobileNumber()+"','"+pd.getEmail()+"','"+pd.getCareTakerForeName()+"','"+pd.getCareTakerSurname()+"','"+pd.getCareTakerAddress()+"','"+pd.getCareTakerPlace()+"','"+pd.getCareTakerPin()+"','"+pd.getCareTakerTelephone()+"','"+pd.getCareTakerMobile()+"','"+pd.getCareTakerEmail()+"')";
		Statement st = conn.createStatement();
		int i = st.executeUpdate(sql);
		System.out.println(i);

	}
	
	public void insertClinicDetails(String fileName) throws Exception {
		
		NotesBean cd = new NotesBean();
		//NotesRead notes = new NotesRead();
		Map <String,String>map = NotesRead.read(fileName);	
		cd.setPatientId(Integer.valueOf(map.get("Patient Number")));
		String patientDate = map.get("Date");
		System.out.println("patientDate:::"+patientDate);
		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Date patientComingDate= (Date)inputFormat.parse(patientDate);
		System.out.println("patientComingDate:::"+patientComingDate);
		String date=outputFormat.format(patientComingDate);
		System.out.println("Date:::"+date);
		cd.setDate(PatientDetailsRead.dateFormat(patientDate));
		cd.setClinician(map.get("Clinician"));
		cd.setAttachment(map.get("Attachment"));
		cd.setNotes(map.get("Notes"));
		cd.setQuestionaire(map.get("Questionnaire"));
		cd.setDiagnosis(map.get("Diagnois"));
	    //cd.setDisorder(map.get("Disorder"));
	    cd.setPatientNumber(map.get("Patient Number"));
	    cd.setPatientForeName(map.get("Patient Forename"));
	    cd.setPatientSurname(map.get("Patient Surname"));
	    String sql = "INSERT INTO clinicDetails(patientId,date,clinician,Questionaire,attachment,notes,diagnosis,disorder,no,patientNumber,patientForeName,patientSurname) "
				+ "VALUES ('"+cd.getPatientId()+"','"+cd.getDate()+"','"+ cd.getClinician()+"','"+ cd.getQuestionaire()+"','"+cd.getAttachment()+"','"+cd.getNotes()+"','"+cd.getDiagnosis()+"','"+cd.getDisorder()+"','"+cd.getNo()+"','"+cd.getPatientNumber()+"','"+cd.getPatientForeName()+"','"+cd.getPatientSurname()+"')";
	    
	    String sql = "INSERT INTO clinicDetails(patientId,date,clinician,Questionaire,attachment,notes,diagnosis,no,patientNumber,patientForeName,patientSurname) "
				+ "VALUES ('"+cd.getPatientId()+"','"+cd.getDate()+"','"+ cd.getClinician()+"','"+ cd.getQuestionaire()+"','"+cd.getAttachment()+"','"+cd.getNotes()+"','"+cd.getDiagnosis()+"','"+cd.getNo()+"','"+cd.getPatientNumber()+"','"+cd.getPatientForeName()+"','"+cd.getPatientSurname()+"')";
	    Statement st = conn.createStatement();
		int i = st.executeUpdate(sql);
	}*/
}

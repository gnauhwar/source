package com.integra.Mhat.ReadData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.independentsoft.office.odf.Cell;
import com.independentsoft.office.odf.IContentElement;
import com.independentsoft.office.odf.Row;
import com.independentsoft.office.odf.Table;
import com.independentsoft.office.odf.Text;
import com.independentsoft.office.odf.TextDocument;
import com.independentsoft.office.odf.fields.TextInputField;
import com.integra.Mhat.Dao.MySqlConnect;

public class ReadODT_Form {

	private static String PERSON_DETAILS_TBL = "patient_details";
	private static String PERSON_DETAILS_PK_COLNAME = "patientNumber";
	private static String PERSON_NOTES_TBL = "patient_notes";
	private static String PERSON_NOTES_DETAILS_TBL = "patient_notes_details";


	static OpenOfficeConnection connection = null;
	
	public static void openConnectionToOpenOffice() throws Exception{

		connection = new SocketOpenOfficeConnection(8100);
		connection.connect();
	}
	
	public static void closeConnectionToOpenOffice(){
		if(connection != null)
			connection.disconnect();
	}
	
	public static void convertDocToOdt(File inputDemoFile, File inputNotesFile,FileOutputStream logFile) throws IOException{
		MySqlConnect msc = null;
		String strMessage = null;
		try {
			
			if(connection == null){
				openConnectionToOpenOffice();
			}

			msc = new MySqlConnect();
			// connect to an OpenOffice.org instance running on port 8100

			if (inputDemoFile.isFile() && (inputDemoFile.getName().endsWith(".doc") || inputDemoFile.getName().endsWith(".docx"))) {
										
				File outputDemoFile = new File(inputDemoFile.getParent() + "\\" + inputDemoFile.getName().substring(0, inputDemoFile.getName().indexOf(".")).trim() + ".odt");
				File outputNotesFile = new File(inputNotesFile.getParent() + "\\" + inputNotesFile.getName().substring(0, inputNotesFile.getName().indexOf(".")).trim() + ".odt");

				// convert
				DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
				converter.convert(inputDemoFile, outputDemoFile);
				converter.convert(inputNotesFile, outputNotesFile);

				//System.out.println("Converted successfully: " + outputFile.getPath());
				
				
					ReadODT_Form.readDocFile(outputDemoFile.getPath(),msc);
					ReadODT_Form.readNoteFile(outputNotesFile.getPath(),msc);
				
				msc.commit();
				

				outputDemoFile.delete();
				outputNotesFile.delete();
				
				strMessage = inputDemoFile.getName() +" : Success ";
			} else {
				strMessage = inputDemoFile.getName() +" : Failure (Not .doc file) ";
			}
		

		} catch (Exception  e) {
			//e.printStackTrace();
			msc.rollback();
			strMessage = inputDemoFile.getName() +" : Failure ("+e.getMessage()+")";

		} finally{

			if(msc != null)
				msc.stopConnection();

			
			System.out.println(strMessage);
			logFile.write(strMessage.getBytes());
			logFile.write("\n".getBytes());
		}
	}


	public static void readDocFile(String path, MySqlConnect msc ) throws Exception{
		Statement stmt = msc.getSqlStatement();
		
		LinkedHashMap<String, String> patientDetailsArr = null;

		try
		{
			TextDocument doc = new TextDocument(path);

			List<Table> tbls = doc.getTables();
			int colCount = 0;
			String key = "", value = "";
			boolean insert = false;

			if(tbls.size() > 0){
				patientDetailsArr = new LinkedHashMap<>();
			}

			for(Table tbl : tbls){
				List<Row> tbrl = tbl.getRows();

				for(Row tbr : tbrl){
					List<Cell> tbcl = tbr.getCells();
					colCount = tbcl.size();
					key = "";
					value = "";

					for(int i = 0; i < tbcl.size(); i++){            				
						List<IContentElement> itemElements = tbcl.get(i).getContentElements();

						for (IContentElement itemElement : itemElements)
						{
							if(colCount > 2){
								if (itemElement instanceof Text)
								{
									Text txt = (Text)itemElement;
									key = txt.getValue();
									insert = true;
									colCount--;	
								}
							}else if(colCount == 2){
								if (itemElement instanceof Text)
								{
									Text txt = (Text)itemElement;
									key = txt.getValue();
									insert = false;
									colCount--;
								}
							}else if(colCount == 1){
								if (itemElement instanceof Text)
								{
									Text txt = (Text)itemElement;
									value = txt.getValue();
									insert = true;
									colCount--;	
								}
								if(itemElement instanceof TextInputField){
									TextInputField tnf = (TextInputField)itemElement;
									value = tnf.getValue();
									insert = true;
									colCount--;
								}
							}

							if(insert){

								if(patientDetailsArr.get(key) != null && patientDetailsArr.get(key).length() > 0)
									patientDetailsArr.put(key.trim() + "2", value);
								else
									patientDetailsArr.put(key.trim(), value);

								insert = false;
								
								//System.out.println(key + " : " + value);
							}
						}

						if(colCount > 2)
							colCount--;
					}
				}
			}
			
			if(patientDetailsArr != null && patientDetailsArr.size() > 0){

				String create_Demo_table = "CREATE TABLE IF NOT EXISTS " + PERSON_DETAILS_TBL + " (  "
						+ PERSON_DETAILS_PK_COLNAME + " varchar(80) primary key ,  "
						+ "patientForeName  varchar(80)  ,  patientSurname  varchar(80)  ,  "
						+ "dob  varchar(20)  ,  gender  varchar(20)  ,  religion  varchar(40)  ,  "
						+ "address  varchar(80)  ,  place  varchar(40)  ,  pin  varchar(20)  ,  "
						+ "phoneNumber  varchar(20)  ,  mobileNumber  varchar(20)  ,  email  varchar(80)  ,  "
						+ "careTakerForeName  varchar(80)  ,  careTakerSurname  varchar(80)  ,  "
						+ "careTakerAddress  varchar(80)  ,  careTakerPlace  varchar(80)  ,  careTakerPin  varchar(20)  ,  careTakerTelephone  varchar(20)  ,  "
						+ "careTakerMobile  varchar(20)  ,  careTakerEmail  varchar(80)  ) ENGINE=INNODB ;";


				String startIns = "Insert into " + PERSON_DETAILS_TBL + " values ( ";
				String endIns = " );";

				int ins_stmt_col_cnt = 0;
				int loopCnt = 0;
				for(Map.Entry<String, String> entry : patientDetailsArr.entrySet()){
					if(loopCnt == patientDetailsArr.size() - 1){
						loopCnt++;
						if(entry.getValue().length() > 0){
							String str = "";

							if(entry.getValue() != null && entry.getValue().length() == 5){
								if(StringUtils.countMatches(entry.getValue(), entry.getValue().charAt(0) + "") == entry.getValue().length())
									str = "";
								else
									str = entry.getValue();
							}
							else
								str = entry.getValue();

							startIns = startIns + "\"" + str + "\"";
							ins_stmt_col_cnt++;

						}else
							continue;
					}else{
						loopCnt++;
						if(entry.getValue().length() > 0){
							String str = "";

							if(entry.getValue() != null && entry.getValue().length() == 5){
								if(StringUtils.countMatches(entry.getValue(), entry.getValue().charAt(0) + "") == entry.getValue().length())
									str = "";
								else
									str = entry.getValue();
							}
							else
								str = entry.getValue();

							startIns = startIns + "\"" + str + "\",";
							ins_stmt_col_cnt++;

						}else
							continue;
					}


				}

				if(stmt != null){
					stmt.execute(create_Demo_table);
					ResultSet rs = stmt.executeQuery("Select * from " + PERSON_DETAILS_TBL);
					ResultSetMetaData rsmd = rs.getMetaData();

					int colCnt = rsmd.getColumnCount();
					if(colCnt == ins_stmt_col_cnt)
						stmt.execute(startIns + endIns);
				}

			}
			
//			if(patientDetailsArr != null && patientDetailsArr.size() > 0)
//			for(Map.Entry<String, String> entry : patientDetailsArr.entrySet())
//				System.out.println("\n\n" + entry.getKey() + " : " + entry.getValue());

		} finally {
			
		}
	}
	
	
	public static void readNoteFile(String path, MySqlConnect msc ) throws Exception{
		Statement stmt = msc.getSqlStatement();
		
		LinkedHashMap<String, String> patientNotesArr = null;
		
		try
		{
			TextDocument doc = new TextDocument(path);

			List<Table> tbls = doc.getTables();
			String key = "", value = "";

			if(tbls.size() > 0){
				patientNotesArr = new LinkedHashMap<>();
			}


			for(int k = 0; k < tbls.size(); k++){
				List<Row> tbrl = tbls.get(k).getRows();

				for(int i = 0; i < tbrl.size(); i++){  
					List<Cell> tbcl = tbrl.get(i).getCells();

					for(int j = 0; j < tbcl.size(); j++){

						List<IContentElement> itemElements = tbcl.get(j).getContentElements();
						for (IContentElement itemElement : itemElements){
							if (itemElement instanceof Text)
							{
								Text txt = (Text)itemElement;
								if(key.trim().length() == 0){
									key = txt.getValue().length() > 0 ? txt.getValue().trim() : "";
								}else{
									if(txt.getValue().trim().length() > 0)
										value = txt.getValue().trim();
								}

								if(key.trim().length() > 0 && ((i == 0 && (j == (tbcl.size() - 3) || j == (tbcl.size() - 1))) || value.trim().length() > 0)){

									if(patientNotesArr.size() == 0 && key != null && key.length() > 0)
										patientNotesArr.put(key, value);
									else{
										boolean temp = false;
										for(Map.Entry<String, String> entry : patientNotesArr.entrySet()){
											if(key != null && entry.getKey().equalsIgnoreCase(key))
												temp = true;
										}

										if(temp)
											patientNotesArr.put(key.trim() + "2", value);
										else if(key != null && key.length() > 0)
											patientNotesArr.put(key.trim(), value);
									}

									key = "";
									value = "";
								}

							}
							
							if(itemElement instanceof TextInputField){
								TextInputField tnf = (TextInputField)itemElement;
								
								if(tnf.getValue().trim().length() > 0)
									value = tnf.getValue().trim();

								if(key.trim().length() > 0 && value.trim().length() > 0){

									if(patientNotesArr.size() == 0 && key != null && key.length() > 0)
										patientNotesArr.put(key, value);
									else{
										boolean temp = false;
										for(Map.Entry<String, String> entry : patientNotesArr.entrySet()){
											if(key != null && entry.getKey().equalsIgnoreCase(key))
												temp = true;
										}

										if(temp)
											patientNotesArr.put(key.trim() + "2", value);
										else if(key != null && key.length() > 0)
											patientNotesArr.put(key.trim(), value);
									}

									key = "";
									value = "";
								}

							}

						}
						
						if(key.trim().length() > 0 && value.trim().length() == 0 && i == 0 && k == 0){
							if(patientNotesArr.size() == 0 && key != null && key.length() > 0)
								patientNotesArr.put(key, value);
							else{
								boolean temp = false;
								for(Map.Entry<String, String> entry : patientNotesArr.entrySet()){
									if(key != null && entry.getKey().equalsIgnoreCase(key))
										temp = true;
								}

								if(temp)
									patientNotesArr.put(key.trim() + "2", value);
								else if(key != null && key.length() > 0)
									patientNotesArr.put(key.trim(), value);
							}

							key = "";
							value = "";
						}else if(key.trim().length() > 0 && value.trim().length() > 0){

							if(patientNotesArr.size() == 0 && key != null && key.length() > 0)
								patientNotesArr.put(key, value);
							else{
								boolean temp = false;
								for(Map.Entry<String, String> entry : patientNotesArr.entrySet()){
									if(key != null && entry.getKey().equalsIgnoreCase(key))
										temp = true;
								}

								if(temp)
									patientNotesArr.put(key.trim() + "2", value);
								else if(key != null && key.length() > 0)
									patientNotesArr.put(key.trim(), value);
							}

							key = "";
							value = "";
						}
						
					}

				}
					
				if(key.trim().length() > 0 && value.trim().length() > 0){

					if(patientNotesArr.size() == 0 && key != null && key.length() > 0)
						patientNotesArr.put(key, value);
					else{
						boolean temp = false;
						for(Map.Entry<String, String> entry : patientNotesArr.entrySet()){
							if(key != null && entry.getKey().equalsIgnoreCase(key))
								temp = true;
						}

						if(temp)
							patientNotesArr.put(key.trim() + "2", value);
						else if(key != null && key.length() > 0)
							patientNotesArr.put(key.trim(), value);
					}

					key = "";
					value = "";
				}
				
			}
			
			if(patientNotesArr != null && patientNotesArr.size() > 0){

				String create_Notes_table = "CREATE TABLE IF NOT EXISTS " + PERSON_NOTES_TBL + " (  id bigint(10) NOT NULL auto_increment PRIMARY KEY ,"
						+ "date varchar(20), clinician varchar(80), "
						+ "Questionaire varchar(20), attachment varchar(20) , "
						+ "notes  varchar(3000), diagnosis varchar(80), no varchar(20), "
						+ "3_Month_Clinical_Outcome varchar(80), Medication varchar(80), "
						+ "patientNumber varchar(20), PatientForename varchar(20), patientSurname varchar(20), "
						+ "CONSTRAINT fk_patientNumber FOREIGN KEY (patientNumber) "
						+ "REFERENCES " + PERSON_DETAILS_TBL + "( " + PERSON_DETAILS_PK_COLNAME + " )  ) ENGINE=INNODB ;";

				String create_Notes_Details_table = "CREATE TABLE IF NOT EXISTS "+PERSON_NOTES_DETAILS_TBL+" ( "
						+ "notes_id int(10),  notes  varchar(3000), date varchar(20) ) ENGINE=INNODB ;  ";
				
				String startIns = "Insert into " + PERSON_NOTES_TBL + " ( "
						+ "date , clinician ,  Questionaire , attachment  , "
						+ "diagnosis , no ,  3_Month_Clinical_Outcome , Medication , "
						+ "patientNumber , PatientForename , patientSurname )  values ( ";
				
				String endIns = " );";

				String sqlInsNoteDet = "INSERT INTO "+PERSON_NOTES_DETAILS_TBL+"(notes_id, notes, date) VALUES ";
				
				int loopCnt = 0; //because id is added
				String str = "";
				
				/************************************************
				 * Variables for parsing notes field
				 ************************************************/
				Pattern p = Pattern.compile("(\\d+/\\d+/\\d+)");
				Matcher m = null ;
				List<String> tokens = new LinkedList<String>();
				int currentIndex = 0;
				String date ="";
				String token = null;
				/************************************************/
				
				for(Map.Entry<String, String> entry : patientNotesArr.entrySet()){
					str = "";
					
					if(entry.getValue() != null && entry.getValue().length() == 5){
						if(StringUtils.countMatches(entry.getValue(), entry.getValue().charAt(0) + "") == entry.getValue().length())
							str = "";
						else
							str = entry.getValue();
					}
					else
						str = entry.getValue();
					
					
					if("Notes".equalsIgnoreCase(entry.getKey())){
						
						m = p.matcher(str);
						
						while(m.find())
						{
						  token = m.group( 1 ); //group 0 is always the entire match   
						  tokens.add(token);
						  //System.out.println(date+" : " +str.substring(currentIndex,str.indexOf(token)));
						  sqlInsNoteDet +=  "(__NOTES_ID__, \"" + str.substring(currentIndex,str.indexOf(token)) + "\" , \""+date+"\" ), ";
						  currentIndex = str.indexOf(token) + token.length();
						  date = token;
						}
						sqlInsNoteDet +=  "(__NOTES_ID__, \"" + str.substring(currentIndex) + "\" , \""+date+"\" )";
						//System.out.println(date+" : " +str.substring(currentIndex));
						
						
						
						
					} else {						
						if(loopCnt == patientNotesArr.size() - 1){
							startIns = startIns + "\"" + str + "\"";
						}else{
							startIns = startIns + "\"" + str + "\", ";
						}
					}
					loopCnt++;
				}

				if(stmt != null){
					stmt.execute(create_Notes_table);
					stmt.execute(create_Notes_Details_table);
					
					ResultSet rs = stmt.executeQuery("Select * from " + PERSON_NOTES_TBL);
					ResultSetMetaData rsmd = rs.getMetaData();

					int colCnt = rsmd.getColumnCount();
					if(colCnt == loopCnt+1){
						//System.out.println("QUERY : "+startIns + endIns);
						stmt.execute(startIns + endIns);
						
						ResultSet rs2 = stmt.executeQuery("SELECT LAST_INSERT_ID()");
						if(rs2.next()){
							sqlInsNoteDet = sqlInsNoteDet.replaceAll("__NOTES_ID__", String.valueOf(rs2.getInt(1)));
							stmt.execute(sqlInsNoteDet);
						}
					}
						

				}

			}

//			if(patientNotesArr != null && patientNotesArr.size() > 0)
//				for(Map.Entry<String, String> entry : patientNotesArr.entrySet())
//					System.out.println("\n\n" + entry.getKey() + " : " + entry.getValue());

		}finally {
			
		}
		
	}
}

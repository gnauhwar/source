package com.integra.Mhat.ReadFiles;

import java.io.File;
import java.io.FileOutputStream;

import com.integra.Mhat.ReadData.ReadODT_Form;

public class Client {
	public static final String folder =
			 "C:\\Users\\Geetika\\Desktop\\MHAT_extraction\\patients\\Alappuzha patients\\Greeshma Clinic\\";
			//"C:\\Mhat_data_extraction\\patients\\";

	
	public static void main (String args[]) {
		
		File directory = new File(folder);
		
		try{
			
			//ReadODT_Form odt_Form = new ReadODT_Form();
			Client.listFilesForFolder(directory);
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
	}
	
	public static void listFilesForFolder(final File folder) throws Exception{
	
		FileOutputStream fo = new FileOutputStream(new File(Client.folder+"Process.log"));
		
		//Open the connection once to open office for processing the entire set.
		ReadODT_Form.openConnectionToOpenOffice();
			
			for (final File file : folder.listFiles()) {
				if (file.isDirectory()) {
					listFilesForFolder(file);
				} else {
					if (file.getName().contains("demo")){ 
						//send only the demo file, inside the method it will pick the notes file automatically
						//System.out.println(Client.folder+file.getName().replace("demo", "notes"));
						if(new File(Client.folder+file.getName().replace("demo", "notes")).exists()){
							ReadODT_Form.convertDocToOdt(file, new File(Client.folder+file.getName().replace("demo", "notes")),fo);
						} else {
							ReadODT_Form.convertDocToOdt(file, new File(Client.folder+file.getName().replace("demo", "notes").replaceAll("doc", "docx")),fo);
						}
						 
		    		}
				}
			}
			
			ReadODT_Form.closeConnectionToOpenOffice();
			
			fo.flush();
			fo.close();
	}


}

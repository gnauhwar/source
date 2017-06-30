package com.integra.Mhat.ReadFiles;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestNotesSplit {

	
	public static void main(String args[]){
		String str = "50 yrs old married muslim male coming with ? episodic psychiatric illness for last 17 yrs with poor interepisodic recovery. episodes suggestive of affective. plan: start Syp Risp 4 mg hs od, T THP 2 mg am od; r/v 1 wk."
				+" 10/10/11: No change. Plan; Incr T Risp 8 mg hs; incr T THP 2 mg bd; start T CBZ 100 mg bd; r/v 2 wks."
						+" 31/10/11: C/o tiredness. Plan: Stop T CPZ; r/v 2 wks."
						+" 21/11/11: Ran out of meds and tiredness. Plan: red T Risp 6 mg hs; incr T CBZ 200 mg bd; incr T THP 2 mg tds; r/v 1 week."
						+" 28/11/11: Better. Plan; incr T Risp 8 mg hs; r/v 2 wks."
						+" 12/12/11: Sleep cycle reversed. Irritable. Plan: Add T Clozapine 25 mg bd; r/v 1 week."
						+" 19/12/11: Worse. not taking medicines. plan: stop T CBZ, Cloz, Change syp Risp 8 mg, add T CPZ 100 mg hs od; r/v 2 wks." 
						+" 02/01/12: Still problematic. Plan: Incr T CPZ 200 mghs; r/v 2 wks."
						+" 16/01/12: Same. irreg medication; r/v 2 wk.";
		
		Pattern p = Pattern.compile("(\\d+/\\d+/\\d+)");
		Matcher m = p.matcher(str);
		List<String> tokens = new LinkedList<String>();
		int currentIndex = 0;
		String date ="";
		String token = null;
		while(m.find())
		{
		  token = m.group( 1 ); //group 0 is always the entire match   
		  tokens.add(token);
		  System.out.println(date+" : " +str.substring(currentIndex,str.indexOf(token)));
		  currentIndex = str.indexOf(token) + token.length();
		  date = token;
		}
		System.out.println(date+" : " +str.substring(currentIndex));
	}
	
}

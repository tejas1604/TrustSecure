package com.sec.trustsecure.nslookup;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class nsLookUp {
	
	public static StringBuilder getnsLookUp(String dns) {
		String line;
		StringBuilder sb = new StringBuilder();

		try {

			Process process = Runtime.getRuntime().exec("nslookup "+ dns);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			while((line = br.readLine())!= null) {
				sb.append(line).append("\n");
			}
			
		}
		
		catch(Exception ex) {
			System.out.println(ex+" error");

		}
		return sb;
	}
}

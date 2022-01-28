package net.awewsomegaming.TwitchNameScraper.main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main {
	private String t = "Bearer token";
	private String cid = "Client ID";
//	private int index = 610000000;
//	private int end = 753100000;
	private int rc = 0;
	
	public void get_data(int from, int to, String name, String file_path) {
		String csv = "iteration,id,name,type,created_at,view_count\n";
		
		try {
			FileWriter fw = new FileWriter(new File(file_path+name+".csv"));
			int counter = 0;
			
			for (int index = from; index < to; index+=100) {
				int s = index+1;
				int e = index+100;
				String ids = String.valueOf(s);
				
				for (int i = s+1; i <= e; i++) {
					ids+="&id="+String.valueOf(i);
				}
				
				URL api = new URL("https://api.twitch.tv/helix/users?id="+ids);
				HttpURLConnection connection = (HttpURLConnection)api.openConnection();
				
				connection.setRequestProperty("Authorization", "Bearer "+t);
				connection.setRequestProperty("Client-ID", cid);
	
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = reader.readLine();
				
				JSONObject result = (JSONObject) JSONValue.parse(line);
				JSONArray arr = (JSONArray) result.get("data");
				
				for (int i = 0; i < arr.size(); i++) {
					JSONObject arr2 = (JSONObject) arr.get(i);
					csv += String.valueOf(counter)+","+String.valueOf(arr2.get("id"))+","+String.valueOf(arr2.get("display_name"))+","+String.valueOf(arr2.get("type"))+","+String.valueOf(arr2.get("created_at"))+","+String.valueOf(arr2.get("view_count"))+"\n";
				}

				counter += 100;
				
				if (counter >= 10000) {
					fw.append(csv);
					fw.flush();
					
					counter = 0;
					csv = "";
					
					System.out.println("Reset: "+rc);
					rc++;
					
					Thread.sleep(5000);
				}
			}

			fw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void get_data_no_end(int from, String name, String file_path) {
		String csv = "iteration,id,name,type,created_at,view_count\n";
		
		try {
			FileWriter fw = new FileWriter(new File(file_path+"/"+name+".csv"));
			int counter = 0;
			int index = from;
			
			while(true) {
				int s = index+1;
				int e = index+100;
				String ids = String.valueOf(s);
				
				for (int i = s+1; i <= e; i++) {
					ids+="&id="+String.valueOf(i);
				}
				
				URL api = new URL("https://api.twitch.tv/helix/users?id="+ids);
				HttpURLConnection connection = (HttpURLConnection)api.openConnection();
				
				connection.setRequestProperty("Authorization", "Bearer "+t);
				connection.setRequestProperty("Client-ID", cid);
	
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = reader.readLine();
				
				JSONObject result = (JSONObject) JSONValue.parse(line);
				JSONArray arr = (JSONArray) result.get("data");
				
				for (int i = 0; i < arr.size(); i++) {
					JSONObject arr2 = (JSONObject) arr.get(i);
					csv += String.valueOf(counter)+","+String.valueOf(arr2.get("id"))+","+String.valueOf(arr2.get("display_name"))+","+String.valueOf(arr2.get("type"))+","+String.valueOf(arr2.get("created_at"))+","+String.valueOf(arr2.get("view_count"))+"\n";
				}

				counter += 100;
				index += 100;
				
				if (counter >= 10000) {
					fw.append(csv);
					fw.flush();
					
					counter = 0;
					csv = "";
					
					System.out.println("Reset: "+rc);
					rc++;
					
					Thread.sleep(5000);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void replace_line(int line, String to, String name, String file_path) {
		try {
			int lp = 0;
			
			BufferedReader br = new BufferedReader(new FileReader(new File(file_path+"/"+name+".csv")));
			FileWriter fw = new FileWriter(file_path+"/"+name+".csv");

			String result = "";
			String sline;
			
			while ((sline = br.readLine()) != null) {
				if (lp == line)
					result += to;
				else 
					result += sline;
				
				result += "\n";
				lp++;
				
				if (lp >= 10000) {
					fw.append(result);
					fw.flush();
					
					result = "";
				}
				
				System.out.println(lp);
			}
			
			fw.close();
			
//			Path fp = Paths.get(file_path+"/"+name+".csv");
//			List<String> lines = Files.readAllLines(fp);
//			lines.set(line, to);
//			Files.write(fp, lines);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void display(String filename, String open_url) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			
			String line = "";
			
			while ((line = reader.readLine()) != null) {
				Desktop.getDesktop().browse(new URI(open_url+line));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Main() {
		// Enter methods to preform
	}
	
	public static void main(String args[]) {
		new Main();
	}
}

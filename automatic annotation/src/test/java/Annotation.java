package test.java;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import edu.stanford.nlp.pipeline.Annotation;
import org.apache.jena.atlas.json.JSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.json.simple.parser.JSONParser;
import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import eu.fbk.dh.tint.runner.TintPipeline;
import eu.fbk.dh.tint.runner.TintRunner;

public class Annotation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Annotation.class);

    public static boolean validateJavaDate(String strDate) {
    	
	 	if (strDate.trim().equals("")) {
	 	    return false;
	 	}
	 	
 	    SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
 	    sdfrmt.setLenient(false);
 	    
 	    try {
 	        Date javaDate = sdfrmt.parse(strDate);
 	    }
 	    catch (ParseException e) {
 	        return false;
 	    }
 	    
 	    return true;
    }
    
    public static boolean validateJavaDateWithoutDay(String strDate) {
    	
	 	if (strDate.trim().equals("")) {
	 	    return false;
	 	}
	 	
 	    SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM");
 	    sdfrmt.setLenient(false);
 	    
 	    try {
 	        Date javaDate = sdfrmt.parse(strDate);
 	    }
 	    catch (ParseException e) {
 	        return false;
 	    }
 	    
 	    return true;
    }
    
    public static boolean validateJavaDateWithTime(String strDate) {
    	
	 	if (strDate.trim().equals("")) {
	 	    return false;
	 	}
	 	
 	    SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy.MM.dd'T'HH:MM");
 	    sdfrmt.setLenient(false);
 	    
 	    try {
 	        Date javaDate = sdfrmt.parse(strDate);
 	    }
 	    catch (ParseException e) {
 	        return false;
 	    }
 	    
 	    return true;
    }
 	
    public static boolean validateJavaDateWithTimeWithoutDay(String strDate) {
    	
	 	if (strDate.trim().equals("")) {
	 	    return false;
	 	}
	 	
 	    SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy.MM'T'HH:MM");
 	    sdfrmt.setLenient(false);
 	    
 	    try {
 	        Date javaDate = sdfrmt.parse(strDate);
 	    }
 	    catch (ParseException e) {
 	        return false;
 	    }
 	    
 	    return true;
    }

    private static boolean isBeforeOrEqual(String before, String after) throws ParseException {
		//controllo che before sia una data precedente o uguale a after
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date before_date = sdf.parse(before);
    	Date after_date = sdf.parse(after);
    	long before_millis = before_date.getTime();
    	long after_millis = after_date.getTime();
    	
    	if(before_millis <= after_millis)
    		return true;
		return false;
	}
    
    private static long dayDistance(String date1, String date2) throws ParseException {
		
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Date date1_parsed = sdf.parse(date1);
    	Date date2_parsed = sdf.parse(date2);
    	
    	long diffInMillies = Math.abs(date1_parsed.getTime() - date2_parsed.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        
		return diff;
	}
    
    public static void main(String[] args) throws Exception {
    	
    	FileWriter myWriter = new FileWriter("execution.log");
    	FileWriter jsonl = new FileWriter("dataset.jsonl");
		FileWriter errorWriter = new FileWriter("error.log");

		int id_nums=153372;
        
        /*****
		 *
		 * Use this code to download data from database

        Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5532/database_name?user=username&password=********";
		Connection conn = DriverManager.getConnection(url);
    	Statement st = conn.createStatement();
    	String query_string = "SELECT url, text, date "
				+ "FROM crime_news.news "
				+ "WHERE url in (select url from crime_news.italian_crime_news) "
				+ "AND length(text) > 2 "
				+ "order by date ";
    	ResultSet rs = st.executeQuery(query_string);
    	myWriter.write("\n" + query_string + "\n");

		 *****/


		int cont = 0;
		int found = 0;
		int not_found = 0;


		try (CSVReader reader = new CSVReaderBuilder(new FileReader("italian_crime_news.csv")).withSkipLines(1).build()) {
			String[] csvLines;
			while ((csvLines = reader.readNext()) != null) {
				String id = csvLines[1];
				String text = csvLines[4];
				String date_string = csvLines[8];

				myWriter.write("-------------------------------------\n");
				myWriter.write("cont: " + cont + "\n");
				myWriter.write("ID: " + id + "\n");
				myWriter.write("TEXT: " + text + "\n");
				myWriter.write("DATE: " + date_string + "\n");

				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse(date_string);
					long millis = date.getTime();

					HashMap<String, Object> identifier = new HashMap<String, Object>();
					identifier.put("url", id);

					/********** HEIDELTIME **********/
					try {
						HeidelTimeStandalone heidelTimeStandalone = new HeidelTimeStandalone(Language.ITALIAN, DocumentType.NEWS,
								OutputType.XMI, "config.props");

						String process = heidelTimeStandalone.process(text, new Date(millis));
						myWriter.write("Processing time...\n");
						myWriter.write(process + "\n");

						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						InputSource inputFile = new InputSource(new StringReader(process));
						Document doc = dBuilder.parse(inputFile);
						doc.getDocumentElement().normalize();
						NodeList nList = doc.getElementsByTagName("heideltime:Timex3");

						HashMap<String, Integer> hmap = new HashMap<String, Integer>();


						JSONArray array = new JSONArray();

						for (int temp = 0; temp < nList.getLength(); temp++) {
							Node nNode = nList.item(temp);
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement = (Element) nNode;
								String tmp = eElement.getAttribute("timexValue");
								if (eElement.getAttribute("timexType").equals("DATE")) {
									if (validateJavaDate(eElement.getAttribute("timexValue")) &&
											isBeforeOrEqual(eElement.getAttribute("timexValue"), date_string)) {
										int begin = Integer.parseInt(eElement.getAttribute("begin"));
										int end = Integer.parseInt(eElement.getAttribute("end"));
										JSONObject item = new JSONObject();
										item.put("id", id_nums++);
										item.put("start_offset", begin);
										item.put("end_offset", end);
										item.put("text", text.substring(begin, end));
										array.add(item);
										String word = text.substring(begin, end).toLowerCase();
										if (hmap.get(eElement.getAttribute("timexValue")) != null) {
											int n = hmap.get(eElement.getAttribute("timexValue"));
											hmap.put(eElement.getAttribute("timexValue"), ++n);
										} else
											hmap.put(eElement.getAttribute("timexValue"), 1);

										myWriter.write(word + "\n");
										myWriter.write("timexValue : " + eElement.getAttribute("timexValue") + "\n");
									} else if (validateJavaDateWithoutDay(eElement.getAttribute("timexValue"))) {
										String new_date = eElement.getAttribute("timexValue") + "-01";
										if (isBeforeOrEqual(new_date, date_string)) {
											int begin = Integer.parseInt(eElement.getAttribute("begin"));
											int end = Integer.parseInt(eElement.getAttribute("end"));
											JSONObject item = new JSONObject();
											item.put("id", id_nums++);
											item.put("start_offset", begin);
											item.put("end_offset", end);
											item.put("text", text.substring(begin, end));
											array.add(item);
											String word = text.substring(begin, end).toLowerCase();
											if (hmap.get(new_date) != null) {
												int n = hmap.get(new_date);
												hmap.put(new_date, ++n);
											} else
												hmap.put(new_date, 1);

											myWriter.write(word + "\n");
											myWriter.write("timexValue : " + new_date + "\n");
										}
									} else if (validateJavaDateWithTime(eElement.getAttribute("timexValue"))) {
										String new_date = eElement.getAttribute("timexValue").split("T")[0].replace(".", "-");
										if (isBeforeOrEqual(new_date, date_string)) {
											int begin = Integer.parseInt(eElement.getAttribute("begin"));
											int end = Integer.parseInt(eElement.getAttribute("end"));
											JSONObject item = new JSONObject();
											item.put("id", id_nums++);
											item.put("start_offset", begin);
											item.put("end_offset", end);
											item.put("text", text.substring(begin, end));
											array.add(item);
											String word = text.substring(begin, end).toLowerCase();
											if (hmap.get(new_date) != null) {
												int n = hmap.get(new_date);
												hmap.put(new_date, ++n);
											} else
												hmap.put(new_date, 1);

											myWriter.write(word + "\n");
											myWriter.write("timexValue : " + new_date + "\n");
										}
									} else if (validateJavaDateWithTimeWithoutDay(eElement.getAttribute("timexValue"))) {
										String new_date = eElement.getAttribute("timexValue").split("T")[0].replace(".", "-") + "-01";
										if (isBeforeOrEqual(new_date, date_string)) {
											int begin = Integer.parseInt(eElement.getAttribute("begin"));
											int end = Integer.parseInt(eElement.getAttribute("end"));
											JSONObject item = new JSONObject();
											item.put("id", id_nums++);
											item.put("start_offset", begin);
											item.put("end_offset", end);
											item.put("text", text.substring(begin, end));
											array.add(item);
											String word = text.substring(begin, end).toLowerCase();
											if (hmap.get(new_date) != null) {
												int n = hmap.get(new_date);
												hmap.put(new_date, ++n);
											} else
												hmap.put(new_date, 1);

											myWriter.write(word + "\n");
											myWriter.write("timexValue : " + new_date + "\n");
										}
									} else if (eElement.getAttribute("timexValue").equals("PRESENT_REF")) {
										String new_date = date_string;
										int begin = Integer.parseInt(eElement.getAttribute("begin"));
										int end = Integer.parseInt(eElement.getAttribute("end"));
										JSONObject item = new JSONObject();
										item.put("id", id_nums++);
										item.put("start_offset", begin);
										item.put("end_offset", end);
										item.put("text", text.substring(begin, end));
										array.add(item);
										String word = text.substring(begin, end).toLowerCase();
										if (hmap.get(new_date) != null) {
											int n = hmap.get(new_date);
											hmap.put(new_date, ++n);
										} else
											hmap.put(new_date, 1);

										myWriter.write(word + "\n");
										myWriter.write("timexValue : " + new_date + "\n");
									}

								} else if (eElement.getAttribute("timexType").equals("TIME") &&
										validateJavaDate(eElement.getAttribute("timexValue").substring(0, eElement.getAttribute("timexValue").length() - 3)) &&
										isBeforeOrEqual(eElement.getAttribute("timexValue").substring(0, eElement.getAttribute("timexValue").length() - 3), date_string)) {
									int begin = Integer.parseInt(eElement.getAttribute("begin"));
									int end = Integer.parseInt(eElement.getAttribute("end"));
									JSONObject item = new JSONObject();
									item.put("id", id_nums++);
									item.put("start_offset", begin);
									item.put("end_offset", end);
									item.put("text", text.substring(begin, end));
									array.add(item);
									String word = text.substring(begin, end).toLowerCase();
									if (hmap.get(eElement.getAttribute("timexValue").substring(0, eElement.getAttribute("timexValue").length() - 3)) != null) {
										int n = hmap.get(eElement.getAttribute("timexValue").substring(0, eElement.getAttribute("timexValue").length() - 3));
										hmap.put(eElement.getAttribute("timexValue").substring(0, eElement.getAttribute("timexValue").length() - 3), ++n);
									} else
										hmap.put(eElement.getAttribute("timexValue").substring(0, eElement.getAttribute("timexValue").length() - 3), 1);

									myWriter.write(word + "\n");
									myWriter.write("timexValue : " + eElement.getAttribute("timexValue").substring(0, eElement.getAttribute("timexValue").length() - 3) + "\n");
								}
							}
						}

						identifier.put("time_expressions", array);

						myWriter.write("Checking results time...\n");

						int max = 0;
						//String max_date = "";
						Set set = hmap.entrySet();
						Iterator iterator = set.iterator();
						while (iterator.hasNext()) {
							Map.Entry mentry = (Map.Entry) iterator.next();
							//myWriter.write("key is: "+ mentry.getKey() + " & Value is: " + mentry.getValue().toString() + "\n");
							if (Integer.parseInt(mentry.getValue().toString()) > max) {
								max = Integer.parseInt(mentry.getValue().toString());
								//max_date = mentry.getKey().toString();
							}
						}

						List<String> list_max = new ArrayList<String>();
						Set set_max = hmap.entrySet();
						Iterator iterator_max = set_max.iterator();
						while (iterator_max.hasNext()) {
							Map.Entry mentry_max = (Map.Entry) iterator_max.next();
							if (Integer.parseInt(mentry_max.getValue().toString()) == max) {
								list_max.add(mentry_max.getKey().toString());
							}
						}

						long min_days = 100;
						String min = "";
						for (String l : list_max) {
							//days distance from date_string
							long days = dayDistance(date_string, l);
							if (days < min_days) {
								min_days = days;
								min = l;
							}
						}

						if (!min.equals("")) {
							found++;
							Date min_date = sdf.parse(min);
							java.sql.Date min_date_sql = new java.sql.Date(min_date.getTime());
							myWriter.write("Max probability and most near date: " + min_date_sql.toString() + "\n");
							/* String query = "update crime_news.news set date_event = ? where url = ?";

							int affectedrows = 0;
							PreparedStatement pstmt = conn.prepareStatement(query);
							pstmt.setDate(1, min_date_sql);
							pstmt.setString(2, id);
							affectedrows = pstmt.executeUpdate();
							if(affectedrows!=1) {
								myWriter.write("ERROR: update do nothing\n");
							}*/
						} else {
							not_found++;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					/********** TINT **********/
					JSONArray array = new JSONArray();

					List<JSONObject> entities = new ArrayList<>();
					List<JSONObject> new_entities = new ArrayList<>();

					TintPipeline pipeline = new TintPipeline();
					pipeline.loadDefaultProperties();
					pipeline.load();
					Annotation stanfordAnnotation = pipeline.runRaw(text);
					File file = new File("tmp_ner.json");
					OutputStream outputstream = new FileOutputStream(file);
					InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
					Annotation annotation = pipeline.run(stream, outputstream, TintRunner.OutputFormat.JSON);
					outputstream.close();
					Object obj = new JSONParser().parse(new FileReader("tmp_ner.json"));
					JSONObject jo = (JSONObject) obj;
					String ind = "";

					JSONArray ja = (JSONArray) jo.get("sentences");
					Iterator itr = ja.iterator();
					while (itr.hasNext()) {
						Iterator<Map.Entry> itr1 = ((Map) itr.next()).entrySet().iterator();
						while (itr1.hasNext()) {
							Map.Entry pair = itr1.next();
							if (pair.getKey().equals("tokens")) {
								JSONArray tokens_array = (JSONArray) pair.getValue();
								Iterator itr2 = tokens_array.iterator();
								while (itr2.hasNext()) {
									Iterator<Map.Entry> itr3 = ((Map) itr2.next()).entrySet().iterator();
									String ner = new String();
									String word = new String();
									int begin = 0;
									int end = 0;
									while (itr3.hasNext()) {
										Map.Entry pair_tokens = itr3.next();
										if (pair_tokens.getKey().equals("characterOffsetBegin"))
											begin = Integer.parseInt(pair_tokens.getValue().toString());
										if (pair_tokens.getKey().equals("characterOffsetEnd"))
											end = Integer.parseInt(pair_tokens.getValue().toString());
										if (pair_tokens.getKey().equals("ner")/*&& pair_tokens.getValue().equals("LOC")*/)
											ner = pair_tokens.getValue().toString();
										if (pair_tokens.getKey().equals("word"))
											word = pair_tokens.getValue().toString();
									}

									if (!ner.equals("O")) {
										JSONObject item = new JSONObject();
										item.put("start_offset", begin);
										item.put("end_offset", end);
										item.put("text", word);
										item.put("label", ner);

										if (!entities.isEmpty()) {

											String previous_ner = entities.get(entities.size() - 1).get("label").toString();
											int previous_end = (int) entities.get(entities.size() - 1).get("end_offset");

											if ((ner.equals(previous_ner)) && (previous_end == begin - 1)) {
												// multi word entity
												entities.add(item);
											} else {
												String concatenation = new String();
												for (JSONObject entity : entities) {
													concatenation += " " + entity.get("text");
												}
												int new_start_offset = (int) entities.get(0).get("start_offset");
												int new_end_offset = (int) entities.get(entities.size() - 1).get("end_offset");

												JSONObject new_item = new JSONObject();
												new_item.put("id", id_nums++);
												new_item.put("start_offset", new_start_offset);
												new_item.put("end_offset", new_end_offset);
												new_item.put("text", concatenation.trim());
												new_item.put("label", entities.get(0).get("label"));
												new_entities.add(new_item);

												entities.clear();

												entities.add(item);
											}

										} else {
											entities.add(item);
										}
									}
								}
							}
						}
						if (entities.size() > 0) {
							String concatenation = new String();
							for (JSONObject entity : entities) {
								concatenation += " " + entity.get("text");
							}
							int new_start_offset = (int) entities.get(0).get("start_offset");
							int new_end_offset = (int) entities.get(entities.size() - 1).get("end_offset");

							JSONObject new_item = new JSONObject();
							new_item.put("id", id_nums++);
							new_item.put("start_offset", new_start_offset);
							new_item.put("end_offset", new_end_offset);
							new_item.put("text", concatenation.trim());
							new_item.put("label", entities.get(0).get("label"));
							new_entities.add(new_item);

							entities.clear();
						}
					}

					identifier.put("ner", new_entities);


					/********* DBpedia *********/
					array = new JSONArray();

					org.jsoup.select.Elements links;
					org.jsoup.nodes.Document document;

					List<String> DBPLinks = new ArrayList<String>();
					List<String> DBPText = new ArrayList<String>();

					String newsTextEncoded = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
					String searchUrl = "http://localhost:2230/rest/annotate?confidence=0.8&text=" + newsTextEncoded;

					// "annotate" non funziona con notizie troppo lunghe
					if (searchUrl.length() > 2047) {
						searchUrl = searchUrl.substring(0, 2047);
					}

					URL url = new URL(searchUrl);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					con.setRequestProperty("Accept", "application/json");
					con.setDoOutput(true);

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					JSONParser parser = new JSONParser();
					JSONObject json = (JSONObject) parser.parse(response.toString());

					JSONArray jarr = json.get("Resources")!=null ? (JSONArray) json.get("Resources") : new JSONArray();
					for (Object object : jarr) {
						JSONObject jobj = (JSONObject) object;
						String uri = jobj.get("@URI").toString().replace("http://it.", "http://");
						String entity = jobj.get("@surfaceForm").toString();
						int begin = Integer.parseInt(jobj.get("@offset").toString());
						int end = begin + entity.length();

						JSONObject item = new JSONObject();
						item.put("id", id_nums++);
						item.put("start_offset", begin);
						item.put("end_offset", end);
						item.put("text", entity);
						item.put("uri", uri);
						array.add(item);
					}

					identifier.put("dbpedia", array);

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date_now = new Date();
					identifier.put("timestamp", formatter.format(date_now).toString());

					JSONObject jsonobj = new JSONObject(identifier);
					jsonl.write(jsonobj.toString() + "\n");
					jsonl.flush();

					cont++;

				} catch (Exception e) {
					errorWriter.write(id + "\n");
				}

			}
		}

    	myWriter.write("Number of documents: " + cont + "\n");
    	// myWriter.write("Found time: " + found + "\n");
    	// myWriter.write("Not_found time: " + not_found + "\n");
    	myWriter.close();
    	jsonl.close();
		errorWriter.close();
    	
    }
    
}

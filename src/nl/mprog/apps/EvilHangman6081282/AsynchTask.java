package nl.mprog.apps.EvilHangman6081282;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.util.Log;


public class AsynchTask extends AsyncTask<Void, Void, List<List>> {
    
	public List<List> highScoresData = new ArrayList<List>();
	public List<String> gameplays = new ArrayList<String>();
	public List<String> scores = new ArrayList<String>();
	public List<String> used_guesses = new ArrayList<String>();
	public List<String> words = new ArrayList<String>();
	
	@Override
	protected List<List> doInBackground(Void... params) {
			try {
				retreiveHighScores();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addListsToData();
		//parseXML();
		return highScoresData;
	}
    
    private void retreiveHighScores() throws XmlPullParserException 
	{
	    HttpClient httpClient = new DefaultHttpClient();
	    try
	    {
	      String url = "http://frozen-savannah-9932.herokuapp.com/high_scores?format=xml";
		  Log.d( "phobos", "performing get " + url );
		
		  
		  HttpGet method = new HttpGet(new URI(url));
		  HttpResponse response = httpClient.execute(method);
		  if (response != null)
		  {
			  String toBeParsed = getResponse(response.getEntity());
				//Log.i( "phobos", "received " + toBeParsed);
				parseXML(toBeParsed);
		  }
		  else
		  {
		    Log.i( "phobos", "got a null response" );
		  }
		} catch (IOException e) {
		  Log.e( "ouch", "!!! IOException " + e.getMessage() );
		} catch (URISyntaxException e) {
		  Log.e( "ouch", "!!! URISyntaxException " + e.getMessage() );
	    }
	}
	
	private String getResponse(HttpEntity entity)
	  {
	    String response = "";
	
		try
		{
		  int length = ( int ) entity.getContentLength();
		  StringBuffer sb = new StringBuffer( length );
		  InputStreamReader isr = new InputStreamReader( entity.getContent(), "UTF-8" );
	      char buff[] = new char[length];
	      int cnt;
	      while ( ( cnt = isr.read( buff, 0, length - 1 ) ) > 0 )
	      {
	        sb.append( buff, 0, cnt );
	      }
	
	        response = sb.toString();
	        isr.close();
	    } catch ( IOException ioe ) {
	      ioe.printStackTrace();
	    }
	
	    return response;
	}

	public void parseXML(String toBeParsed) throws XmlPullParserException, IOException{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser parser = factory.newPullParser();
		
		parser.setInput(new StringReader(toBeParsed));
		
		int eventtype = parser.getEventType();
		
		while(eventtype != XmlPullParser.END_DOCUMENT){
			if(eventtype == XmlPullParser.START_TAG){
				if(parser.getName().equals("gameplay")){
					eventtype = parser.next();
					gameplays.add(parser.getText());
					Log.d("warn", parser.getText());
				}
				else if(parser.getName().equals("score")){
					eventtype = parser.next();
					scores.add(parser.getText());
					Log.d("warn", parser.getText());
				}
				else if(parser.getName().equals("used-guesses")){
					eventtype = parser.next();
					used_guesses.add(parser.getText());
					Log.d("warn", parser.getText());
				}
				else if(parser.getName().equals("word")){
					eventtype = parser.next();
					words.add(parser.getText());
					Log.d("warn", parser.getText());
				}
			}
			eventtype = parser.next();
		}
	}
	
	public void addListsToData(){
		highScoresData.add(gameplays);
		highScoresData.add(used_guesses);
		highScoresData.add(scores);
		highScoresData.add(words);
	}
//	public List<String> getGameplays(){
//		return gameplays;
//	}
//	
//	public List<String> getUsedGuesses(){
//		return used_guesses;
//	}
//	
//	public List<String> getScores(){
//		return scores;
//	}
//	
//	public List<String> getWords(){
//		return words;
//	}
}
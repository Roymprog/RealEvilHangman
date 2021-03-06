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
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.util.Log;


@SuppressWarnings("rawtypes")
public class OnlineHighScoresModel extends AsyncTask<List<String>, Void, List<List>> {

	private final String url = "http://frozen-savannah-9932.herokuapp.com/high_scores?format=xml";
	
	public List<List> highScoresData = new ArrayList<List>();
	public List<String> gameplays = new ArrayList<String>();
	public List<String> scores = new ArrayList<String>();
	public List<String> used_guesses = new ArrayList<String>();
	public List<String> words = new ArrayList<String>();

	/* The task to perform in the background */
	@Override
	protected List<List> doInBackground(List<String>... highScoreData) {
		try {
			addToScores(highScoreData[0]);
			retreiveHighScores();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		addListsToHighScoresData();
		return highScoresData;
	}
	
	/* Connects with the RoR highScores server and gets the top 10 
	 * highScores from server as XML*/
	private void retreiveHighScores() throws XmlPullParserException 
	{
		HttpClient httpClient = new DefaultHttpClient();
		try
		{
			HttpGet method = new HttpGet(new URI(url));
			HttpResponse response = httpClient.execute(method);
			if (response != null)
			{
				String toBeParsed = getResponse(response.getEntity());
				parseXML(toBeParsed);
			}
			else
			{
				Log.i( "warn", "got a null response" );
			}
		} catch (IOException e) {
			Log.e( "warn", "!!! IOException " + e.getMessage() );
		} catch (URISyntaxException e) {
			Log.e( "warn", "!!! URISyntaxException " + e.getMessage() );
		}
	}

	/* Gets the data that is sent from the server */
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

	/* Parses the XML that is received from the server 
	 * and puts the XML in the corresponding lists */
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
				}
				else if(parser.getName().equals("score")){
					eventtype = parser.next();
					scores.add(parser.getText());
				}
				else if(parser.getName().equals("used-guesses")){
					eventtype = parser.next();
					used_guesses.add(parser.getText());
				}
				else if(parser.getName().equals("word")){
					eventtype = parser.next();
					words.add(parser.getText());
				}
			}
			eventtype = parser.next();
		}
	}

	/* Combines all the data and puts them in a big list of lists
	 * with all highScoresData */
	public void addListsToHighScoresData(){
		highScoresData.add(gameplays);
		highScoresData.add(used_guesses);
		highScoresData.add(scores);
		highScoresData.add(words);
	}

	/* Adds the achieved score to the server table of highScores*/
	public void addToScores(List<String> highScoreData){
		if (!highScoreData.isEmpty()){
			String url = "http://frozen-savannah-9932.herokuapp.com/high_scores";
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("high_score[gameplay]", highScoreData.get(0)));
				nameValuePairs.add(new BasicNameValuePair("high_score[used_guesses]", highScoreData.get(1)));
				nameValuePairs.add(new BasicNameValuePair("high_score[score]", highScoreData.get(2)));
				nameValuePairs.add(new BasicNameValuePair("high_score[word]", highScoreData.get(3)));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				httpClient.execute(httpPost);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
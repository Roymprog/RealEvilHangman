package nl.mprog.apps.EvilHangman6081282;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DisplayToast extends AsyncTask<DatabaseHelper, Context, Void> {

	private Context myContext;

	public DisplayToast(Context context){
		this.myContext = context;
	}

	@Override
	protected Void doInBackground(DatabaseHelper... dbhelper) {
		return null;
	}

	protected void onProgressUpdate(Context... context) {
		//Toast toast = Toast.makeText(context[0], "Loading game...", Toast.LENGTH_LONG);
		//toast.show();
	}

	protected void onPreExecute(Context...context) {
		Toast toast = Toast.makeText(myContext, "Loading game...", Toast.LENGTH_SHORT);
		toast.show();
	}
}
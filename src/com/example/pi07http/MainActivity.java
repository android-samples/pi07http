package com.example.pi07http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ローディング非表示
		findViewById(R.id.progressBar1).setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	HttpTask mTask;
	public void buttonMethod(View button){
		// タスク生成
		if(mTask != null)return;
		mTask = new HttpTask();
		// タスク実行
		mTask.execute("http://google.co.jp/");
	}
	
	// 結果表示（onPostExecuteから呼ばれる）
	private void showResult(String result){
		TextView t = (TextView)findViewById(R.id.textView1);
		t.setText(result);
	}
	
	// 通信タスク
	class HttpTask extends AsyncTask<String, Integer, String>{
		String mUrl = "";
		
		// オプション：事前準備
		@Override
		protected void onPreExecute() {
			// ローディング表示
			findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		// 必須：バッググラウンド処理を書く
		// ※AsyncTask の cancel を呼び出すと、doInBackground は InterruptedException がおきて終了して、その後 onPostExecute ではなく onCancelled が呼ばれました。
		@Override
		protected String doInBackground(String... params) {
			if(params.length == 0)return null;
			String result = null;
			try{
				mUrl = params[0];
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(mUrl);
				HttpResponse response = client.execute(request);
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					result = EntityUtils.toString(response.getEntity());
				}
				else{
					result = "NOT OK";
				}
			}
			catch(Exception ex){
				result = ex.toString();
			}
			return result;
		}

		// オプション：進捗状況をUIスレッドで表示する処理
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		// オプション：事後処理（バックグラウンド処理が完了し、UIスレッドに反映する処理を書く）（キャンセル時は呼ばれない）
		@Override
		protected void onPostExecute(String result) {
			// 結果表示
			showResult(result);
			mTask = null;
			// ローディング非表示
			findViewById(R.id.progressBar1).setVisibility(View.INVISIBLE);
			super.onPostExecute(result);
		}

		// オプション：キャンセルした状態（cancelが呼ばれた状態）でdoInBackgroundを抜けたときに呼ばれる
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}
}

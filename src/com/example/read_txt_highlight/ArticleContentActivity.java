package com.example.read_txt_highlight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ArticleContentActivity extends Activity implements
		SeekBar.OnSeekBarChangeListener {

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private TextView mContent;

	private TextView mSeekTv;
	private SeekBar mSeekBar;

	private ArrayList<ArrayList<String>> wordList = new ArrayList<ArrayList<String>>();

	private ArrayList<String> zeroList = new ArrayList<String>();
	private ArrayList<String> oneList = new ArrayList<String>();
	private ArrayList<String> twoList = new ArrayList<String>();
	private ArrayList<String> threeList = new ArrayList<String>();
	private ArrayList<String> fourList = new ArrayList<String>();
	private ArrayList<String> fiveList = new ArrayList<String>();
	private ArrayList<String> sixList = new ArrayList<String>();

	String contentString;
	String titleString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content);

		findViewAndSetAttributes();

		Intent intent = this.getIntent();
		contentString = intent.getStringExtra("content");
		titleString = intent.getStringExtra("title");
		if (contentString != null) {
			mContent.setText(contentString);
		}
		if (mModuleNameTxtv != null) {
			mModuleNameTxtv.setText(titleString);
		}

		mSeekBar.setEnabled(false);
		(new GetWordTask()).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int resId = R.string.content_title;
		mModuleNameTxtv.setText(resId);

		mContent = (TextView) findViewById(R.id.content_tv);

		mSeekTv = (TextView) findViewById(R.id.slide_tv);

		mSeekBar = (SeekBar) findViewById(R.id.slide_bar);
		mSeekBar.setOnSeekBarChangeListener(this);

	}

	private class GetWordTask extends
			AsyncTask<String, Integer, ArrayList<ArrayList<String>>> {
		protected ArrayList<ArrayList<String>> doInBackground(String... strs) {
			ArrayList<ArrayList<String>> result = getAllWords();

			return result;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(ArrayList<ArrayList<String>> result) {

			if (result != null) {
				mSeekBar.setEnabled(true);
			}

		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mBackRelati) {
				finish();
			}
		}
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		mSeekTv.setText(getResources().getString(R.string.slide_tv)
				+ String.valueOf(seekBar.getProgress()));
		spanWord(seekBar.getProgress());
	}

	/**
	 * 获取所有词库单词 并加入到指定列表中
	 * 
	 * 注释：这里可以利用数据库存储到本地
	 */
	public ArrayList<ArrayList<String>> getAllWords() {
		InputStream inputStream = getResources()
				.openRawResource(R.raw.ncewords);
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String line;
		int level;
		String wordString;
		try {

			while ((line = reader.readLine()) != null) {
				if (getNumbers(line) != null && !getNumbers(line).equals("")) {
					level = Integer.parseInt(getNumbers(line));
					wordString = splitNotNumber(line).trim();
					switch (level) {
					case 0:
						zeroList.add(wordString);
						break;
					case 1:
						oneList.add(wordString);

						break;
					case 2:
						twoList.add(wordString);

						break;
					case 3:
						threeList.add(wordString);

						break;
					case 4:
						fourList.add(wordString);

						break;
					case 5:
						fiveList.add(wordString);

						break;
					case 6:

						sixList.add(wordString);
						break;

					default:
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		wordList.add(zeroList);
		wordList.add(oneList);
		wordList.add(twoList);
		wordList.add(threeList);
		wordList.add(fourList);
		wordList.add(fiveList);
		wordList.add(sixList);
		return wordList;

	}

	// 截取数字
	public String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	// 截取非数字
	public String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * 根据等级匹配对应单词 并高亮显示
	 * 
	 * @param level
	 */
	private void spanWord(int level) {
		SpannableStringBuilder style = new SpannableStringBuilder(mContent
				.getText().toString());
		for (int i = 0; i < level; i++) {

			ArrayList<String> arrayList = wordList.get(i);
			for (int j = 0; j < arrayList.size(); j++) {
				Pattern p = Pattern.compile(arrayList.get(j));

				Matcher m = p.matcher(style);

				while (m.find()) {
					int start = m.start();
					int end = m.end();
					style.setSpan(new ForegroundColorSpan(Color.RED), start,
							end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

				}
			}
		}
		mContent.setText(style);

	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

}

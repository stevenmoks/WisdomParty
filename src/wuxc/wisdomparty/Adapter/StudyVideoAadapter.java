package wuxc.wisdomparty.Adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import single.wuxc.wisdomparty.R;
import wuxc.wisdomparty.Cache.StudyVideoCache;
import wuxc.wisdomparty.Internet.ImageLoader;
import wuxc.wisdomparty.Internet.ImageLoader.ImageCallback;
import wuxc.wisdomparty.Internet.URLcontainer;
import wuxc.wisdomparty.Model.StudyVideoModel;;

public class StudyVideoAadapter extends ArrayAdapter<StudyVideoModel> {
	private ListView listView;
	private ImageLoader ImageLoader;
	private String imageurl = "";
	private int screenwidth = 0;
	private float scale = 0;
	private float scalepx = 0;
	private float dp = 0;
	private Activity thisactivity;

	public StudyVideoAadapter(Activity activity, List<StudyVideoModel> imageAndTexts, ListView listView) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;
		this.thisactivity = activity;
		ImageLoader = new ImageLoader();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();

		// Inflate the views from XML
		View rowView = convertView;
		StudyVideoCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.item_video, null);
			viewCache = new StudyVideoCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (StudyVideoCache) rowView.getTag();
		}
		StudyVideoModel imageAndText = getItem(position);

		// Load the image and set it on the ImageView
		String imageUrl = imageAndText.getImageUrl();
		ImageView imageView = viewCache.getImageBackGround();
		imageView.setTag(URLcontainer.urlip + imageUrl);
		Log.e("imageUrl", imageUrl);
		if (imageUrl.equals(imageurl)) {
			imageView.setImageResource(R.drawable.knbz);
		} else {
			try {
				String imageName1 = getBitName(imageUrl);
				String temppath = Environment.getExternalStorageDirectory() + "/chat/" + imageName1 + ".png";
				Bitmap bm1 = null;
				bm1 = getBitmapByPath(temppath);
				if (bm1 == null) {
					imageUrl = URLcontainer.urlip + imageUrl;
					Log.e("imageUrl", imageUrl);
					Drawable cachedImage = ImageLoader.loadDrawable(imageUrl, new ImageCallback() {
						public void imageLoaded(Drawable imageDrawable, String imageUrl) {
							ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
							if (imageViewByTag != null) {
								imageViewByTag.setImageDrawable(imageDrawable);
							}
						}
					});
					if (cachedImage == null) {
						imageView.setImageResource(R.drawable.knbz);
					} else {
						Drawable d = cachedImage; // xxx�����Լ��������ȡdrawable

						BitmapDrawable bd = (BitmapDrawable) d;

						Bitmap bm = bd.getBitmap();
						bm = cutBmp(bm);
						imageView.setImageBitmap(bm);
					}
				} else {
					imageView.setImageBitmap(bm1);
				}
			} catch (Exception e) {
				// TODO: handle exception
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
			}

		}
		TextView TextLabel1 = viewCache.getTextLabel1();
		TextView TextLabel2 = viewCache.getTextLabel2();
		TextView TextLabel3 = viewCache.getTextLabel3();
		if (TextUtils.isEmpty(imageAndText.getLabel1())) {
			TextLabel1.setVisibility(View.GONE);
		} else {
			TextLabel1.setText(imageAndText.getLabel1());
		}
		if (TextUtils.isEmpty(imageAndText.getLabel2())) {
			TextLabel2.setVisibility(View.GONE);
		} else {
			TextLabel2.setText(imageAndText.getLabel2());
		}
		if (TextUtils.isEmpty(imageAndText.getLabel3())) {
			TextLabel3.setVisibility(View.GONE);
		} else {
			TextLabel3.setText(imageAndText.getLabel3());
		}
		TextView TextNUmberGreat = viewCache.getTextNumberGreat();
		TextNUmberGreat.setText(imageAndText.getNumberGreat());
		TextView TextNUmberCollect = viewCache.getTextNumberCollect();
		TextNUmberCollect.setText(imageAndText.getNumberCollect());
		TextView TextTitle = viewCache.getTextTitle();
		TextTitle.setText("" + imageAndText.getTitle());
		TextView TextTimeLong = viewCache.getTextTimeLong();
		TextTimeLong.setText("ʱ����" + imageAndText.getTimeLong());
		TextView TextTime = viewCache.getTextTime();
		TextTime.setText("" + imageAndText.getTime());
		RelativeLayout Half = viewCache.getRelaHalf();
		screenwidth = thisactivity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		DisplayMetrics mMetrics = new DisplayMetrics();
		thisactivity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

		scale = thisactivity.getResources().getDisplayMetrics().density;
		dp = screenwidth / scale + 0.5f;
		scalepx = screenwidth / dp;
		int height = (int) ((screenwidth - 60 * scalepx) / 2);
		LinearLayout.LayoutParams layoutParams1 = (android.widget.LinearLayout.LayoutParams) Half.getLayoutParams();
		layoutParams1.height = height;
		Half.setLayoutParams(layoutParams1);

		return rowView;
	}

	public Bitmap getBitmapByPath(String fileName) {
		// String myJpgPath =
		// Environment.getExternalStorageDirectory()+"pepper/" + fileName;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 12;
		Bitmap bm = BitmapFactory.decodeFile(fileName, options);
		return bm;
	}

	private String getBitName(String imageUrl) {
		// TODO Auto-generated method stub
		String[] temp = imageUrl.split("");
		String result = "";
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].equals("/") || temp[i].equals(".")) {
				temp[i] = "";
			}
			result = result + temp[i];
		}
		return result;
	}

	public void saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {
		String path = Environment.getExternalStorageDirectory() + "/chat/";
		String myJpgPath = Environment.getExternalStorageDirectory() + "/chat/" + bitName + ".png";
		File tmp = new File(path);
		if (!tmp.exists()) {
			tmp.mkdir();
		}
		File f = new File(myJpgPath);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Bitmap cutBmp(Bitmap bmp) {
		Bitmap result;
		int w = bmp.getWidth();// ���볤���ο�
		int h = bmp.getHeight();// ���볤���θ�
		int nw;// ��������ο�
		result = Bitmap.createBitmap(bmp, 15 * w / 100, 15 * h / 100, 7 * w / 10, 7 * h / 10);
		// }
		return result;
	}
}
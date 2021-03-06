package wuxc.wisdomparty.PartyManage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import single.wuxc.wisdomparty.R;
import wuxc.wisdomparty.PartyManage.AssistancePayActivity;
import wuxc.wisdomparty.layout.dialogfour;

public class AssistanceDetailActivity extends Activity implements OnClickListener {
	private ImageView ImageBackground;
	private int screenwidth = 0;
	private ImageView ImageBack;
	private ImageView ImageShare;
	private TextView TextTitle;
	private TextView TextAuthorAandTime;
	private TextView TextDetail;
	private Button BtnConfirm;
	private String Time;
	private String Title;
	private UMImage image = null;
	private String Tag = "";
	private String content;

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.assistance_detail_activity);
		initview();
		setonclicklistener();
		setlayout();
		Intent intent = this.getIntent(); // 获取已有的intent对象
		Bundle bundle = intent.getExtras(); // 获取intent里面的bundle对象

		Title = bundle.getString("Title");
		Time = bundle.getString("Time");
		content = bundle.getString("content");
		TextTitle.setText(Title);
		TextAuthorAandTime.setText(Time);
		TextDetail.setText(ToDBC(content));

		TextAuthorAandTime.setText(Time);
		String html = "<html>" + "<body>" + "<table>" + "<tr>" + "<td>成都天府</td>" + "</tr>" + "</table>" + "</body>"
				+ "</html>";
		webView = (android.webkit.WebView) findViewById(R.id.webview);
		// StringBuilder sb = new StringBuilder();
		// sb.append(detail);
		// webView.loadUrl("http://ww.baidu.com");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
		GetData();
	}

	public Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

				break;
			default:
				break;
			}
		}
	};

	private void GetData() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		new Thread(new Runnable() { // 开启线程上传文件
			@Override
			public void run() {
				try {
					content = getNewContent(content);
				} catch (Exception e) {
					// TODO: handle exception
				}
				Message msg = new Message();
				msg.obj = "1";
				msg.what = 1;
				uiHandler.sendMessage(msg);
			}
		}).start();

	}

	private String getNewContent(String htmltext) {

		Document doc = Jsoup.parse(htmltext);
		Elements elements = doc.getElementsByTag("img");
		for (Element element : elements) {
			element.attr("width", "100%").attr("height", "auto");
		}

		Log.d("VACK", doc.toString());
		return doc.toString();
	}

	private void initview() {
		// TODO Auto-generated method stub
		ImageBackground = (ImageView) findViewById(R.id.image_background);
		ImageBack = (ImageView) findViewById(R.id.image_back);
		ImageShare = (ImageView) findViewById(R.id.image_share);
		TextTitle = (TextView) findViewById(R.id.text_title);
		TextAuthorAandTime = (TextView) findViewById(R.id.text_time);
		TextDetail = (TextView) findViewById(R.id.text_detail);
		BtnConfirm = (Button) findViewById(R.id.btn_confirm);

	}

	private void setonclicklistener() {
		// TODO Auto-generated method stub
		ImageBackground.setOnClickListener(this);
		ImageBack.setOnClickListener(this);
		ImageShare.setOnClickListener(this);
		BtnConfirm.setOnClickListener(this);
	}

	public String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375) {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);

	}

	private void setlayout() {
		// TODO Auto-generated method stub
		screenwidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		int height = (int) (screenwidth / 1.52);
		LinearLayout.LayoutParams layoutParams1 = (android.widget.LinearLayout.LayoutParams) ImageBackground
				.getLayoutParams();
		layoutParams1.height = height;
		ImageBackground.setLayoutParams(layoutParams1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.image_share:
			ShareDialog();
			break;
		case R.id.image_back:
			finish();
			break;
		case R.id.btn_confirm:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), AssistancePayActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void ShareDialog() {
		final String title = "推荐一款很棒的APP";
		final String content = "这是一款集招聘、应聘、企业管理、即时通讯于一体的APP，是你生活工作的好帮手！";
		final String targeturl = "http://fir.im/j4zk";
		// String img = "";
		// String url = "";
		// if (!img.equals("")) {
		// url = URLcontainer.urlip + img;
		// image = new UMImage(AssistanceDetailActivity.this, url);
		// // image=new UMImage(AssistanceDetailActivity.this,
		// R.drawable.qidongye);
		// } else {
		image = new UMImage(AssistanceDetailActivity.this, R.drawable.image_share);
		// }

		dialogfour.Builder builder = new dialogfour.Builder(this);
		builder.setMessage("您未正确选择！");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setQQImage(new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "qq", Toast.LENGTH_SHORT).show();
				Tag = "QQ好友";
				if (image == null) {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.QQ)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.share();
				} else {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.QQ)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.withMedia(image).share();
				}
			}
		});
		builder.setQQZoneImage(new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Tag = "QQ空间";
				Toast.makeText(getApplicationContext(), "qqzone", Toast.LENGTH_SHORT).show();
				if (image == null) {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.QZONE)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.share();
				} else {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.QZONE)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.withMedia(image).share();
				}
			}
		});
		builder.setWeChatImage(new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Tag = "微信好友";
				Toast.makeText(getApplicationContext(), "wechat", Toast.LENGTH_SHORT).show();
				if (image == null) {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.share();
				} else {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.withMedia(image).share();
				}
			}
		});
		builder.setWeFriendsImage(new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Tag = "微信朋友圈";
				Toast.makeText(getApplicationContext(), "wefriends", Toast.LENGTH_SHORT).show();
				if (image == null) {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.share();
				} else {
					new ShareAction(AssistanceDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
							.setCallback(umShareListener).withText(content).withTitle(title).withTargetUrl(targeturl)
							.withMedia(image).share();
				}
			}
		});
		builder.create().show();

	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			// sendshareinfo();
			Toast.makeText(getApplicationContext(), Tag + "分享成功！", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(getApplicationContext(), Tag + "分享失败！", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(getApplicationContext(), Tag + "分享取消！", Toast.LENGTH_SHORT).show();
		}
	};
}

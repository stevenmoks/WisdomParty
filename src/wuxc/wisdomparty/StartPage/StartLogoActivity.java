package wuxc.wisdomparty.StartPage;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.socialize.utils.Log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;
import single.wuxc.wisdomparty.R;
import wuxc.wisdomparty.Internet.HttpGetData;
import wuxc.wisdomparty.Internet.URLcontainer;
import wuxc.wisdomparty.main.MainActivity;

public class StartLogoActivity extends Activity {
	private SharedPreferences PreGuidePage;// ����ȷ���Ƿ��ǵ�һ�ε�¼
	private SharedPreferences PreAccount;// �洢�û��������룬�����Զ���¼
	private SharedPreferences PreUserInfo;// �洢������Ϣ
	private int GuidePage;// ����ҳ��ʾ��־��
	private String LoginId;
	private String pwd;
	private String userPhoto;
	private String address;
	private String ticket;
	private String loginId;
	private String sex;
	private String sessionId;
	private String username;
	private static final String GET_SUCCESS_RESULT = "success";
	private static final int GET_LOGININ_RESULT_DATA = 1;
	private Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_LOGININ_RESULT_DATA:
				GetDataDetailFromLoginResultData(msg.obj);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start_logo_activity);
		PreUserInfo = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		PreAccount = getSharedPreferences("Account", Context.MODE_PRIVATE);
		PreGuidePage = getSharedPreferences("GuidePage", Context.MODE_PRIVATE);
		ReadGuidePage();
		if (GuidePage == 0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GuidePageActivity.class);
			startActivity(intent);
			finish();
		} else {
			ReadAccount();
			if (LoginId == null || LoginId.equals("")) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LoginAactivity.class);
				startActivity(intent);
				finish();
			} else {
				final ArrayList ArrayValues = new ArrayList();
				ArrayValues.add(new BasicNameValuePair("login_id", LoginId));
				ArrayValues.add(new BasicNameValuePair("pwd", pwd));
				new Thread(new Runnable() { // �����߳��ϴ��ļ�
					@Override
					public void run() {
						String LoginResultData = "";
						LoginResultData = HttpGetData.GetData(URLcontainer.LoginIn, ArrayValues);
						Message msg = new Message();
						msg.obj = LoginResultData;
						msg.what = GET_LOGININ_RESULT_DATA;
						uiHandler.sendMessage(msg);
					}
				}).start();

			}
		}
	}

	public void GetDataDetailFromLoginResultData(Object obj) {
		// TODO Auto-generated method stub
		String Type = null;
		String Data = null;
		try {
			JSONObject demoJson = new JSONObject(obj.toString());
			Type = demoJson.getString("type");
			Data = demoJson.getString("data");
			if (Type.equals(GET_SUCCESS_RESULT)) {
				Toast.makeText(getApplicationContext(), "��½�ɹ�", Toast.LENGTH_SHORT).show();
				GetDetailData(Data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void GetDetailData(String data) {
		// TODO Auto-generated method stub
		try {
			JSONObject demoJson = new JSONObject(data);

			userPhoto = demoJson.getString("userPhoto");
			address = demoJson.getString("address");
			ticket = demoJson.getString("ticket");
			loginId = demoJson.getString("loginId");
			sex = demoJson.getString("sex");
			sessionId = demoJson.getString("sessionId");
			username = demoJson.getString("username");
			WriteUserInfo();
			JumpPageToMainPage();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void JumpPageToMainPage() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void WriteUserInfo() {
		// TODO Auto-generated method stub
		Editor edit = PreUserInfo.edit();
		edit.putString("userPhoto", userPhoto);
		edit.putString("address", address);
		edit.putString("ticket", ticket);
		edit.putString("sex", sex);
		edit.putString("sessionId", sessionId);
		edit.putString("sex", sex);
		edit.commit();
	}

	private void ReadAccount() {
		// TODO Auto-generated method stub
		LoginId = PreAccount.getString("LoginId", null);
		pwd = PreAccount.getString("pwd", null);
	}

	private void ReadGuidePage() {
		// TODO Auto-generated method stub
		GuidePage = PreGuidePage.getInt("GuidePage", 0);
	}
}
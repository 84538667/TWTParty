package com.twt.party;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.twt.party.login.TWTLogin;
import com.twt.party.login.TWTLoginConst;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


/***
 * ���Activity�Ǽ̳е�sherlockActivity,���þ���ʹ��android 3.0��ǰ�ľ���actionBar ��
 * ���涥���Ķ�������������ο�ActionBarSherlock�Ĺٷ��ĵ���
 * @author boelroy
 *
 */
public class LoginActivity extends SherlockActivity{

	protected void onCreate(Bundle savedInstanceState) {
		//ʹ��sherlockActivity���������������⣬��Ȼ�޷�����
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginview);
		setSupportProgressBarIndeterminateVisibility(false);
		
		//����½��ť���õ���¼�������½����
		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				setSupportProgressBarIndeterminateVisibility(true);
				//��������еõ��û�������û���������
				EditText usernameEd = (EditText)findViewById(R.id.username);
				final String username = usernameEd.getText().toString();
				EditText passwordEd = (EditText)findViewById(R.id.password);
				final String password = passwordEd.getText().toString();
			   //���ڵ�¼ʱ��������������½��߳̽��е�½���߳̽����������UI
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Bundle b = new Bundle();
						Message msg = new Message();
						if(TWTLogin.loginToServer(LoginActivity.this, username, password))
						{
							//��ת����һ��Activity
							b.putString("message", TWTLogin.getUserRealName(LoginActivity.this));
						}
						else
						{
							b.putString("message", "������û���������");
						}
						msg.setData(b);
						handle.sendMessage(msg);
					}
				}).start();
			}
		});
	}
	
	//�첽��handler�����߳��е��������������߳���UI����ʾ
	private Handler handle = new Handler(){
		public void handleMessage(Message message){
				setSupportProgressBarIndeterminateVisibility(false);
				Toast.makeText(LoginActivity.this, message.getData().getString("message"), Toast.LENGTH_SHORT).show();
		}
	};
	
	//���menu(���ǵ��õ�Actionsherlock�е�menu)��Ҫ�Ǹ�actionBar�����menu
     public boolean onCreateOptionsMenu(Menu menu) {
    	 
    	 	//����subMenu ���ԡ�
    	 	SubMenu submenu = menu.addSubMenu("Other action");
    	 	submenu.add(R.string.register);
    	 	submenu.add(R.string.forgetten);
    	 	
    	 	MenuItem item = submenu.getItem();
    	 	item.setIcon(R.drawable.other_action);
    	 	//�������item������actionBar��һ����ʾ
    	 	item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        super.onCreateOptionsMenu(menu);
        return true;
    }

	@Override
	//���˵���ѡ��ʱ��������ʱ��
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		//���ô�Ĭ�ϵ������
		intent.setAction("android.intent.action.VIEW");
		if(item.getTitle().equals(this.getResources().getString(R.string.register))){
			//��ѡ�е���ע�ᰴť����ת��ע��ҳ��
			intent.setData(Uri.parse(TWTLoginConst.TWT_REGISTER_URL));
			this.startActivity(intent);
		}
		else if(item.getTitle().equals(this.getResources().getString(R.string.forgetten)))
		{
			//�����������������İ�ť,��ת����������Ľ���
			intent.setData(Uri.parse(TWTLoginConst.TWT_FORGGETEN_URL));
			this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
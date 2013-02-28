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
 * 这个Activity是继承的sherlockActivity,作用就是使得android 3.0以前的具有actionBar 即
 * 界面顶部的动作栏，详情请参考ActionBarSherlock的官方文档。
 * @author boelroy
 *
 */
public class LoginActivity extends SherlockActivity{

	protected void onCreate(Bundle savedInstanceState) {
		//使用sherlockActivity必须先设置其主题，不然无法编译
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginview);
		setSupportProgressBarIndeterminateVisibility(false);
		
		//给登陆按钮设置点击事件，即登陆动作
		findViewById(R.id.button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				setSupportProgressBarIndeterminateVisibility(true);
				//从输入框中得到用户输入的用户名和密码
				EditText usernameEd = (EditText)findViewById(R.id.username);
				final String username = usernameEd.getText().toString();
				EditText passwordEd = (EditText)findViewById(R.id.password);
				final String password = passwordEd.getText().toString();
			   //由于登录时网络操作，所以新建线程进行登陆等线程结束后更新主UI
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Bundle b = new Bundle();
						Message msg = new Message();
						if(TWTLogin.loginToServer(LoginActivity.this, username, password))
						{
							//跳转到下一个Activity
							b.putString("message", TWTLogin.getUserRealName(LoginActivity.this));
						}
						else
						{
							b.putString("message", "错误的用户名或密码");
						}
						msg.setData(b);
						handle.sendMessage(msg);
					}
				}).start();
			}
		});
	}
	
	//异步的handler，在线程中调用用来更改主线程中UI的显示
	private Handler handle = new Handler(){
		public void handleMessage(Message message){
				setSupportProgressBarIndeterminateVisibility(false);
				Toast.makeText(LoginActivity.this, message.getData().getString("message"), Toast.LENGTH_SHORT).show();
		}
	};
	
	//添加menu(这是调用的Actionsherlock中的menu)主要是给actionBar添加上menu
     public boolean onCreateOptionsMenu(Menu menu) {
    	 
    	 	//设置subMenu 属性。
    	 	SubMenu submenu = menu.addSubMenu("Other action");
    	 	submenu.add(R.string.register);
    	 	submenu.add(R.string.forgetten);
    	 	
    	 	MenuItem item = submenu.getItem();
    	 	item.setIcon(R.drawable.other_action);
    	 	//设置这个item总是在actionBar第一个显示
    	 	item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        super.onCreateOptionsMenu(menu);
        return true;
    }

	@Override
	//当菜单被选中时，触发的时间
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		//设置打开默认的浏览器
		intent.setAction("android.intent.action.VIEW");
		if(item.getTitle().equals(this.getResources().getString(R.string.register))){
			//当选中的是注册按钮，跳转到注册页面
			intent.setData(Uri.parse(TWTLoginConst.TWT_REGISTER_URL));
			this.startActivity(intent);
		}
		else if(item.getTitle().equals(this.getResources().getString(R.string.forgetten)))
		{
			//当点击的是忘记密码的按钮,跳转到忘记密码的界面
			intent.setData(Uri.parse(TWTLoginConst.TWT_FORGGETEN_URL));
			this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
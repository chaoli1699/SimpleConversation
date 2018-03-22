package cn.cienet.simpleconversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.cienet.simpleconversation.bean.Conversation;
import cn.cienet.simpleconversation.network.SocketHelper;
import cn.cienet.simpleconversation.utils.Constant;
import cn.cienet.simpleconversation.utils.FormatUtils;

public class MainActivity extends Activity {
	
	private ListView lvConversations;
	private EditText etContent;
	private List<Map<String, Object>> listItem;
	private SimpleAdapter simpleAdapter;
	private static final String MSG_DATE_FOMATE="yyyyƒÍMM‘¬dd»’ hh:mm:ss";
	private SocketHelper socketHelper;
	private Intent intentService;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.MESSAGE_SOCKET_COMPLATE:
//				Toast.makeText(MainActivity.this, "Socket is ready", Toast.LENGTH_SHORT).show();
				break;

			case Constant.MESSAGE_RECEIVE_NEW_MEG:
				updateConversationList(new Conversation("Server:",
						msg.obj.toString(),
						FormatUtils.formatDate(System.currentTimeMillis(), MSG_DATE_FOMATE)));
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		socketHelper=SocketHelper.getInstance();
		
		lvConversations=findViewById(R.id.main_conversitions);
		etContent=findViewById(R.id.main_content);
		
		listItem=new ArrayList<Map<String,Object>>();
	    simpleAdapter=new SimpleAdapter(this,
				listItem, R.layout.item_conversation,
				new String[]{"author", "content", "date"},
				new int[]{R.id.conversation_author, R.id.conversation_content, R.id.conversation_date});
		lvConversations.setAdapter(simpleAdapter);
		
		etContent.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg1==KeyEvent.KEYCODE_ENTER) {
//					Toast.makeText(MainActivity.this, "Enter", Toast.LENGTH_SHORT).show();
					if (arg2.getAction()==KeyEvent.ACTION_UP) {
						final String content=etContent.getText().toString();
						if (content!=null&&content.length()>0) {
							String date=FormatUtils.formatDate(System.currentTimeMillis(), MSG_DATE_FOMATE);
							updateConversationList(new Conversation("Local:", content, date));
							socketHelper.sendMessageToServer(content);
							etContent.setText("");
						}
					}
				}
				return false;
			}
		});
		
		intentService=new Intent(this, ConversationService.class);
		startService(intentService);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				socketHelper.connectToTCPServer(MainActivity.this, mHandler);
			}
		}).start();
		
	}
	
	private void updateConversationList(Conversation con) {
	
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("author", con.getAuthor());
		map.put("content", con.getContent());
		map.put("date", con.getDate());
		listItem.add(map);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		socketHelper.disConnectToServer();
		stopService(intentService);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

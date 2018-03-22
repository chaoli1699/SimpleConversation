package cn.cienet.simpleconversation.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import cn.cienet.simpleconversation.utils.Constant;

public class SocketHelper {
	
	private Socket mClientSocket;
	private PrintWriter mPrintWriter;
	
	private volatile static SocketHelper instance=null;
	private static ExecutorService cacheThreadPool=Executors.newCachedThreadPool();
	
	private SocketHelper(){}
	
	public static SocketHelper getInstance(){
		if (instance==null) {
			synchronized (SocketHelper.class) {
				if (instance==null) {
					instance=new SocketHelper();
				}
			}
		}
		return instance;
	}

	public void connectToTCPServer(Activity mActivity, Handler mHandler){
		
		Socket socket=null;
		while (socket==null) {
			try {
				socket=new Socket("localhost", 8689);
				mClientSocket=socket;
				mPrintWriter=new PrintWriter(
						new BufferedWriter(
								new OutputStreamWriter(socket.getOutputStream())), true);
				
				mHandler.sendEmptyMessage(Constant.MESSAGE_SOCKET_COMPLATE);
			} catch (IOException e) {
				// TODO: handle exception
				SystemClock.sleep(1000);
				connectToTCPServer(mActivity, mHandler);
			}	
		}
		
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(mClientSocket.getInputStream()));
			
			while(!mActivity.isFinishing()){
				String mString;
				mString = br.readLine();
				if (mString!=null) {
					mHandler.obtainMessage(Constant.MESSAGE_RECEIVE_NEW_MEG, mString).sendToTarget();
				}	
			}
			
			mPrintWriter.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void disConnectToServer(){
		
		if (mClientSocket!=null) {
			try {
				mClientSocket.shutdownInput();
				mClientSocket.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessageToServer(final String content){
		
		cacheThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mPrintWriter.println(content);
			}
		});
	}
}

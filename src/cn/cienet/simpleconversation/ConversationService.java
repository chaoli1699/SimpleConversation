package cn.cienet.simpleconversation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ConversationService extends Service {

	private boolean isServiceDestoried=false;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		new Thread(new TCPServer()).start();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isServiceDestoried=true;
		super.onDestroy();
	}
	
	private class TCPServer implements Runnable{
		
		private void responseClient(Socket client) throws IOException{
			BufferedReader in=new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			
			PrintWriter out=new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(client.getOutputStream())),true);
			
			out.println("Welcome to my conversation!");
			
			while (!isServiceDestoried) {
				String str=in.readLine();
				if (str==null) {
					break;
				}
				
				out.println("This is server port!");
			}
			
			in.close();
			out.close();
			client.close();
		}
		
		
		@SuppressWarnings("resource")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ServerSocket serverSocket=null;
		    try {
				serverSocket=new ServerSocket(8689);
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
				return ;
			}
		    
		    while(!isServiceDestoried){
		    	try {
					final Socket client=serverSocket.accept();
					
					new Thread(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
//							super.run();
							try {
								responseClient(client);
							} catch (IOException e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					}.start();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		    }
		}
		
	}

}

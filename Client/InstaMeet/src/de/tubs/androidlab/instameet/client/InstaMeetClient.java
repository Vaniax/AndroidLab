package de.tubs.androidlab.instameet.client;

import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.service.ReceivedMessageCallbacks;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class InstaMeetClient implements Runnable {
	
	private final static String TAG = "TCP-CLient";

	private LinkedBlockingQueue<ServerRequest> requestQueue = new LinkedBlockingQueue<ServerRequest>();
		
	private ReceivedMessageCallbacks callbacks = null;
	
	public InstaMeetClient(ReceivedMessageCallbacks callbacks) {
		this.callbacks = callbacks;
	}
	
	public void insertToQueue(ServerRequest request) {
		requestQueue.add(request);
	}
	
	@Override
	public void run() {
		EventLoopGroup workerEventGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap
				.group(workerEventGroup)
				.channel(NioSocketChannel.class)
				.handler(new LoggingHandler())
				.handler(new InstaMeetClientInitializer(callbacks));
			
			ChannelFuture future = bootstrap.connect("192.168.178.20",8080);
			
		    future.addListener(new FutureListener<Void>() {

		        @Override
		        public void operationComplete(Future<Void> future) throws Exception {
		            if (!future.isSuccess()) {
		            	Log.i(TAG,"Connection to Server cannot be established");
		            } else {
						Log.i(TAG,"Connection to Server established");
		            }
		        }
		    });
						
			while (future.channel().isOpen()) {
				ServerRequest request = requestQueue.take();
				Log.d(TAG,request.toString());
				future.channel().pipeline().writeAndFlush(request); // Wait until element is available
				Log.d(TAG,"Send request to server successful");
			}
			
			future.channel().close().sync();
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} finally {
			workerEventGroup.shutdownGracefully();
		}
	}
}

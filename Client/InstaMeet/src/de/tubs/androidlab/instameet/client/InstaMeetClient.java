package de.tubs.androidlab.instameet.client;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse.Type;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class InstaMeetClient implements Runnable {
	
	public ConcurrentLinkedQueue<ServerRequest> queue = new ConcurrentLinkedQueue<ServerRequest>();
	
	public InstaMeetClient() {
		
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
				.handler(new InstaMeetClientInitializer());
			
			ChannelFuture future = bootstrap.connect("192.168.178.25",8080).sync();
						
			while (future.channel().isOpen()) {
				if(!queue.isEmpty()) {
					Log.i("Client","queue not empty");
					future.channel().pipeline().writeAndFlush(queue.poll());
				}
			}
			
			future.channel().close().sync();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			workerEventGroup.shutdownGracefully();
		}
	}

}

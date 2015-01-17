package instameet.client.netty.tests;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class InstaMeetClientTest {

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		EventLoopGroup workerEventGroup = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap
				.group(workerEventGroup)
				.channel(NioSocketChannel.class)
				.handler(new LoggingHandler())
				.handler(new InstaMeetClientInitializerTest());
			
//			ChannelFuture future = bootstrap.connect(InetAddress.getLocalHost(),8080).sync();
			ChannelFuture future = bootstrap.connect("54.93.64.1",8080).sync();
			
		    future.addListener(new FutureListener<Void>() {

		        @Override
		        public void operationComplete(Future<Void> future) throws Exception {
		            if (!future.isSuccess()) {
		            	System.out.println("Connection to Server cannot be established");
		            } else {
		            	System.out.println("Connection to Server established");
		            }
		        }
		    });
			while (true) {
			
			}
//			future.channel().close().sync();
			
		} finally {
			workerEventGroup.shutdownGracefully();
		}
	}

}

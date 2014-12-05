package instameet.client.netty;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class InstaMeetClient {

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		EventLoopGroup workerEventGroup = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap
				.group(workerEventGroup)
				.channel(NioSocketChannel.class)
				.handler(new LoggingHandler())
				.handler(new InstaMeetClientInitializer());
			
			ChannelFuture future = bootstrap.connect(InetAddress.getLocalHost(),8080).sync();
			
			future.channel().close().sync();
			
		} finally {
			workerEventGroup.shutdownGracefully();
		}
	}

}

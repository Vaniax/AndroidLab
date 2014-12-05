package instameet.server.netty;

import java.net.InetAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class InstaMeetServer {

	public static void main(String[] args) throws InterruptedException {
		
		EventLoopGroup bossEventGroup = new NioEventLoopGroup();
		EventLoopGroup workerEventGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap
				.group(bossEventGroup, workerEventGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler())
				.childHandler(new InstaMeetServerInitializer());
			
			ChannelFuture future = bootstrap.bind(8080).sync().channel().closeFuture().sync();;
		} finally {
			workerEventGroup.shutdownGracefully();
			bossEventGroup.shutdownGracefully();
		}
		
	}

}

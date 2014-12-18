package instameet.server.netty.tests;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class InstaMeetServerTest  {

	public static void main(String[] args) throws InterruptedException {
		
		EventLoopGroup bossEventGroup = new NioEventLoopGroup();
		EventLoopGroup workerEventGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap
				.group(bossEventGroup, workerEventGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(new InstaMeetServerInitializerTest());
			
//			ChannelFuture future = bootstrap.bind(8080).sync().channel().closeFuture().sync();
			ChannelFuture future = bootstrap.bind(8080).sync();
			
			while (future.channel().isOpen()) {
				
			}

		} finally {
			workerEventGroup.shutdownGracefully();
			bossEventGroup.shutdownGracefully();
		}
	}

}

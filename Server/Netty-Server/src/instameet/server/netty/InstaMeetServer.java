package instameet.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import service.InstaMeetService;
import service.ServiceInterface;

public class InstaMeetServer implements Runnable {

	private ServiceInterface service;
	static private InstaMeetServer server = null;
	
	InstaMeetServer(ServiceInterface service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		
		EventLoopGroup bossEventGroup = new NioEventLoopGroup();
		EventLoopGroup workerEventGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap
				.group(bossEventGroup, workerEventGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(new InstaMeetServerInitializer(service));
			
//			ChannelFuture future = bootstrap.bind(8080).sync().channel().closeFuture().sync();
			ChannelFuture future = bootstrap.bind(8080).sync();
			
			while (future.channel().isOpen()) {

			}
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerEventGroup.shutdownGracefully();
			bossEventGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		server = new InstaMeetServer(new InstaMeetService());
		new Thread(server).start();
	}

}

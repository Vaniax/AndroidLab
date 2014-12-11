package de.tubs.androidlab.instameet.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class InstaMeetClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		
		p.addLast(new ProtobufVarint32LengthFieldPrepender());
		p.addLast(new ProtobufEncoder());
		p.addLast(new ClientHandlerTest());
	}
	
}

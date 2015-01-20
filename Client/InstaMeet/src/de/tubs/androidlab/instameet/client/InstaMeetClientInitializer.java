package de.tubs.androidlab.instameet.client;

import de.tubs.androidlab.instameet.service.ReceivedMessageCallbacks;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse;

public class InstaMeetClientInitializer extends ChannelInitializer<SocketChannel> {
	private ReceivedMessageCallbacks cb = null;
		
	public InstaMeetClientInitializer(ReceivedMessageCallbacks cb) {
		this.cb = cb;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
			
		p.addLast(new ProtobufVarint32LengthFieldPrepender());
		p.addLast(new ProtobufEncoder());
		
		p.addLast(new ProtobufVarint32FrameDecoder());
		p.addLast(new ProtobufDecoder(ClientResponse.getDefaultInstance()));
		
		p.addLast(new ClientHandler(cb));
	}
	
}

package instameet.server.netty;

import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

public class InstaMeetServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline p = channel.pipeline();
		
		p.addLast(new ProtobufVarint32FrameDecoder());
		p.addLast(new ProtobufDecoder(ServerRequest.getDefaultInstance()));
		p.addLast(new ServerHandlerTest());
	}

}

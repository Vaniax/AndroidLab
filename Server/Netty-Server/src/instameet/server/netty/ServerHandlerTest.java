package instameet.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import instameet.server.protobuf.ServerRequests.ServerRequest;

public class ServerHandlerTest extends SimpleChannelInboundHandler<ServerRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ServerRequest msg)
			throws Exception {
		System.out.println("Message:\n" + msg.toString());
	}

}

package instameet.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;

public class ServerHandlerTest extends SimpleChannelInboundHandler<ServerRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ServerRequest msg)
			throws Exception {
		System.out.println("Message:\n" + msg.toString());
	}

}

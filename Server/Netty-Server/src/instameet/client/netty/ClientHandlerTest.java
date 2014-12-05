package instameet.client.netty;

import instameet.server.protobuf.ServerRequests.Login;
import instameet.server.protobuf.ServerRequests.ServerRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ClientHandlerTest extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		Login login = Login.newBuilder().setName("Matthias").setPassword("123").build();

		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(ServerRequest.Type.LOGIN)
				.setLogin(login)
				.build();
		ctx.writeAndFlush(request);
		super.channelActive(ctx);
	}


}

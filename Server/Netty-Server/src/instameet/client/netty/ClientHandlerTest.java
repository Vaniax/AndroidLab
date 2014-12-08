package instameet.client.netty;

import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
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

package instameet.client.netty.tests;

import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandlerTest extends SimpleChannelInboundHandler<ClientResponse> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		Login login = Login.newBuilder().setName("Matthias").setPassword("123").build();

		ServerRequest request = ServerRequest
				.newBuilder()
				.setType(ServerRequest.Type.LOGIN)
				.setLogin(login)
				.build();
		ctx.writeAndFlush(request);
		System.out.println("Send Login Message");
		super.channelActive(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ClientResponse msg)
			throws Exception {
		System.out.println("Message:\n" + msg.toString());
		switch (msg.getType()) {
		case SECURITY_TOKEN:
			System.out.println("Token:\n" + msg.getToken().getToken());
			break;
		default:
			break;
		}
	}
}

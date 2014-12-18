package instameet.server.netty.tests;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SecurityToken;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse.Type;

public class ServerHandlerTest extends SimpleChannelInboundHandler<ServerRequest> {

	static public ConcurrentMap<Integer, Channel> channels = new ConcurrentHashMap<>();
	
	// For simple test
	static public int i = 0;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ServerRequest msg)
			throws Exception {
		System.out.println("Message:\n" + msg.toString());
		switch (msg.getType()) {
		case LOGIN:
			// TODO: Call Service
			Login login = msg.getLogin();
			// service.login(login) -> <id,bool>
			
			if (!channels.containsKey(i)) { // TODO: also test for successful login
				channels.put(i, ctx.channel());
				i += 1;
				System.out.println("Add new channel");
			}
			SecurityToken token = SecurityToken.newBuilder().setToken("testtoken").build();
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.SECURITY_TOKEN).setToken(token).build());
			break;
		case SEND_CHAT_MESSAGE:
			ChatMessage message = msg.getMessage();
			
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.CHAT_MESSAGE).setMessage(message).build());
			break;	
		default:
			break;
		}
	}

}

package instameet.server.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse;
import de.tubs.androidlab.instameet.server.protobuf.Messages.Login;
import de.tubs.androidlab.instameet.server.protobuf.Messages.SecurityToken;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ChatMessage;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ServerRequest;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse.Type;
import service.ServiceInterface;
import simpleEntities.LoginData;

public class InstaMeetServerHandler extends SimpleChannelInboundHandler<ServerRequest> {

	static public ConcurrentMap<Integer, Channel> channels = new ConcurrentHashMap<>();
		
	static private ServiceInterface service = null;
	
	InstaMeetServerHandler(ServiceInterface service) {
		InstaMeetServerHandler.service = service;
	}
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ServerRequest msg)
			throws Exception {
		System.out.println("Message:\n" + msg.toString());
		switch (msg.getType()) {
		
		case LOGIN:
			// TODO: Call Service
			Login login = msg.getLogin();
			LoginData data = service.login(login.getPassword(), login.getName());

			int userID = data.getUserId();
			if (!channels.containsKey(userID)) { // TODO: also test for successful login
				channels.put(userID, ctx.channel());
				System.out.println("Add new channel for user id: " + userID);
			}
			SecurityToken token = SecurityToken.newBuilder().setToken(data.getToken()).build();
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.SECURITY_TOKEN).setToken(token).build());
			break;
		
		// TODO: Change simple echo reply to correct one
		case SEND_CHAT_MESSAGE:
			ChatMessage message = msg.getMessage();
			
			ctx.writeAndFlush(ClientResponse.newBuilder().setType(Type.CHAT_MESSAGE).setMessage(message).build());
			break;

		default:
			break;
		}
	}

}

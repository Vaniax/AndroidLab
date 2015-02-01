package de.tubs.androidlab.instameet.client;

import android.util.Log;
import de.tubs.androidlab.instameet.server.protobuf.Messages.ClientResponse;
import de.tubs.androidlab.instameet.service.ReceivedMessageCallbacks;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler<ClientResponse> {

	private final static String TAG = "CLientHandler";
	ReceivedMessageCallbacks cb = null;
	
	public ClientHandler(ReceivedMessageCallbacks cb) {
		this.cb = cb;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ClientResponse msg)
			throws Exception {
		Log.i("ClientHandler","Message incomming");
		Log.i("ClientHandler",msg.getType().toString());
		
		ClientResponse.Type type = msg.getType();
		switch (type) {
		case CHAT_MESSAGE:
			Log.i(TAG,"Message type" + type);
			cb.chatMessage(msg.getMessage());
			break;
		case LIST_FRIENDS:
			Log.i(TAG,"Message type" + type);
			cb.listFriends(msg.getListFriends());
			break;
//		case LIST_LOCATIONS:
//			Log.i("ClientHandler","Message type" + type);
//			break;
		case LIST_CHAT_MESSAGES:
			Log.i(TAG,"Message type" + type);
			cb.listChatMessages(msg.getListChatMessages());
			break;
		case SECURITY_TOKEN:
			Log.i(TAG,"Message type" + type);
			cb.securityToken(msg.getToken());
			break;
		case BOOL:
			Log.i(TAG,"Message type" + type);
			cb.bool(msg.getBoolReply());
			break;
		case NO_MESSAGE:
			Log.i(TAG,"Message type" + type);
			break;
		case OWN_DATA:
			Log.i(TAG,"Message type" + type);
			cb.ownData(msg.getUserData());
			break;
		case LIST_NEAREST_APPOINTMENTS:
			Log.i(TAG,"Message type" + type);
			cb.listNearAppointments(msg.getListNearestAppointments());
			break;
		case LIST_VISITING_APPOINTMENTS:
			Log.i(TAG,"Message type" + type);
			cb.listVisitingAppointments(msg);
			break;
		case LIST_USERS: {
			Log.i(TAG,"Message type" + type);
			cb.listUsers(msg.getListUsers());
		} break;
		case SIMPLE_APPOINTMENT: {
			Log.i(TAG,"Message type" + type);
			cb.simpleAppointment(msg.getAppointment());
		} break;
		case ADD_FRIEND_REQUEST: {
			Log.i(TAG,"Message type" + type);
			cb.addFriendRequest(msg.getUser());
		} break;
		default:
			break;
		}
	}


}

package com.milkbartube.tracy;

import io.socket.SocketIO;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

public class Chat extends Thread {
    
    private ChatCallback callback;
    private SocketIO socket;
    private String server;

    public Chat(ChatCallbackAdapter callback, String server) {
	this.callback = new ChatCallback(callback);
	this.server = server;
    }

    @Override
    public void run() {
	try {
	    socket = new SocketIO(server, callback);
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void disconnectChat() {
	if (socket.isConnected())
	    socket.disconnect();
    }

    public void sendMessage(String message) {
	try {
	    JSONObject json = new JSONObject();
	    json.putOpt("msg", message);
	    socket.emit("chatMsg", json);
	} catch (JSONException ex) {
	    ex.printStackTrace();
	}
    }

    public void login(String username, String password) {
	try {
	    JSONObject json1 = new JSONObject();
	    json1.putOpt("name", username);
	    json1.putOpt("pw", password);
	    socket.emit("login", callback, json1);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void join(String room) {
	try {
	    JSONObject json = new JSONObject();
	    json.putOpt("name", room);
	    socket.emit("initChannelCallbacks");
	    socket.emit("joinChannel", json);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    public void privateMessage(JSONObject json) {
	socket.emit("pm", json);
    }

    public void sendRoomPassword(String password) {
	socket.emit("channelPassword", password);
    }

    public String getServer() {
	return server;
    }

    public void setServer(String server) {
	this.server = server;
    }

    public SocketIO getSocket() {
	return socket;
    }

    public void setSocket(SocketIO socket) {
	this.socket = socket;
    }
}

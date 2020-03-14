package com.bonc.test.akka;

import akka.actor.UntypedActor;

@SuppressWarnings("deprecation")
public class Greeter extends UntypedActor{
	public static enum Msg {
		LTL,LKK;
	}
	@Override
	public void onReceive(Object msg) throws Throwable {
		if(msg==Msg.LKK) {
			System.out.println(msg);
			getSender().tell(Msg.LTL, getSelf());
		}else {
			System.out.println(msg);
			unhandled(msg);
		}
	}

}

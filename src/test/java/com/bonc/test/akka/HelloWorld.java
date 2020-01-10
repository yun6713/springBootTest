package com.bonc.test.akka;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;

@SuppressWarnings("deprecation")
public class HelloWorld extends UntypedActor{
	ActorRef ar;
	
	@Override
	public void preStart() throws Exception {
		ar=getContext().actorOf(Props.create(Greeter.class), "greeter");
		System.out.println("Greeter actor path: "+ar.path());
		ar.tell(Greeter.Msg.LKK, getSelf());
	}
	@Override
	public void onReceive(Object msg) throws Throwable {
		if(msg==Greeter.Msg.LTL) {
			ar.tell(PoisonPill.getInstance(), ActorRef.noSender());
			ar.tell(Greeter.Msg.LTL, getSelf());
			getContext().stop(getSelf());
		}else {
			unhandled(msg);
		}
	}
	
}

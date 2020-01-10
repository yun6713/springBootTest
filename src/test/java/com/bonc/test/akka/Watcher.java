package com.bonc.test.akka;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;

@SuppressWarnings("deprecation")
public class Watcher extends UntypedActor{
	
	public Watcher(ActorRef ar) {
		getContext().watch(ar);
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if(msg instanceof Terminated) {
			System.out.println("terminated");
			
		}else {
			unhandled(msg);
		}
	}

}

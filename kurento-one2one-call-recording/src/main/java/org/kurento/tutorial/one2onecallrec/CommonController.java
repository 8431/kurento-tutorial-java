package org.kurento.tutorial.one2onecallrec;

import org.kurento.client.KurentoConnectionListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController implements KurentoConnectionListener {
	
	private static int healthNum = -1;
	
	@RequestMapping("/healthCheck")
	public int healthCheck(){
	  	return healthNum;
	}

	@Override
	public void connected() {
		healthNum = 1;
	}

	@Override
	public void connectionFailed() {
		healthNum = 0;
	}

	@Override
	public void disconnected() {
		healthNum = 2;
	}

	@Override
	public void reconnected(boolean sameServer) {
		healthNum = 1;
	}

}

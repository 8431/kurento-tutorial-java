package org.kurento.tutorial.monitor;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {
	
	@Value("${server.addrs}")
    private String serverAddrs;
	
	@RequestMapping("/kurento/queryAvailableServices")
	public AvailableServices queryAvailableServices(){
		String[] addrs = serverAddrs.split(";");
		AvailableServices availableServices = new AvailableServices();
		int max=addrs.length;
	    Random random = new Random();
    	int s = random.nextInt(max);
    	for(int i=0;i<max;i++){
    		String url = "https://" + addrs[s] + "/healthCheck";
    		String state = HttpsRequestUtils.httpRequest(url, "GET", null);
    		if("1".equals(state)){
    			availableServices.setId(String.valueOf(s));
    			availableServices.setUrl(addrs[s]);
    			availableServices.setMessage("SUCCUEE!");
    			return availableServices;
    		}
    		s++;
    		if(s==max) s = 0;
    	}
    	availableServices.setId("-1");
		availableServices.setUrl("");
		availableServices.setMessage("No Available Service!");
		return availableServices;
	}

}

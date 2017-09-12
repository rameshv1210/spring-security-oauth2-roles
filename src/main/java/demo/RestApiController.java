package demo;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {
	@RequestMapping("/home")
	public String home() {
		return "Hello World";
	}
	
	@RequestMapping("/reg/a")
	public String reg() {
		return "REGISTERED";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String create(@RequestBody MultiValueMap<String, String> map) {
		return "OK";
	}
}

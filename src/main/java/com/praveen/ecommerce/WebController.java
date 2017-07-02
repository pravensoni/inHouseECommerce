package com.praveen.ecommerce;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

	@RequestMapping("/test")
	public Test test() {
		return new Test(1, "content");
	}
}

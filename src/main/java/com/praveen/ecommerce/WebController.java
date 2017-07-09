package com.praveen.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.ecommerce.dao.CommonDao;
import com.praveen.ecommerce.models.Product;

@RestController
public class WebController {

	@Autowired
	CommonDao commonDao;

	
	@CrossOrigin
	@RequestMapping("/product/{productId}")
	public String getProductDetail(@PathVariable("productId") int productId) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(commonDao.getProduct(productId));
	}
	
	@CrossOrigin
	@RequestMapping("product/homepage/{siteId}")
	public String getHomePageProducts(@PathVariable("siteId") int siteId) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(commonDao.getHomePageProducts(siteId));
	}

}

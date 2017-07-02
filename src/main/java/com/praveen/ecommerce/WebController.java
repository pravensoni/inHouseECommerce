package com.praveen.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.praveen.ecommerce.dao.CommonDao;
import com.praveen.ecommerce.models.Product;

@RestController
public class WebController {

	@Autowired
	CommonDao commonDao;

	@RequestMapping("/product/{productId}")
	public Product test(@PathVariable("productId") int productId) {
		return commonDao.getProduct(productId);
	}
}

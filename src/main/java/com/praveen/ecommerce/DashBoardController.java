package com.praveen.ecommerce;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.ecommerce.dao.OrderDao;
import com.praveen.ecommerce.dao.ProductDao;
import com.praveen.ecommerce.dao.UserDao;
import com.praveen.ecommerce.models.Order;
import com.praveen.ecommerce.models.OrderStatus.OrderStatusEnum;
import com.praveen.ecommerce.models.Product;
import com.praveen.ecommerce.models.UserInfo;

@RestController
public class DashBoardController {

	@Autowired
	ProductDao productDao;

	@Autowired
	OrderDao orderDao;

	@Autowired
	UserDao userDao;
	
	@Autowired
	HttpSession httpSession;

	@CrossOrigin
	@RequestMapping(value = "/login")
	public String login(@RequestParam(name = "userName") String userName,
			@RequestParam(name = "password") String pass) throws Exception {

		String returnStr = "";
		// validate user
		UserInfo userInfo = userDao.validateUser(userName, pass);
		// create session
		if (userInfo != null) {
			httpSession.setAttribute("user", userInfo);
			returnStr = "Login Successful.";
			// return message string
		} else {
			returnStr = "Login Failed. User name and password does not match";
		}
return returnStr;
		

	}

	// method to get order list
	@CrossOrigin
	@RequestMapping(value = "/orderList")
	public String getOrders(@RequestParam(name = "offset", defaultValue = "0") int offset,
			@RequestParam(name = "status", defaultValue = "") String orderStatus) {
		if(httpSession.getAttribute("user")==null) return "session expired";
		OrderStatusEnum os = null;
		if ("".equals(orderStatus)) {
			orderStatus = null;
		} else {
			os = OrderStatusEnum.valueOf(orderStatus);
		}

		List<Order> list = orderDao.getOrderList(offset, os, 20);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			return e.getMessage();
		}

	}

	// method to get product list
	@CrossOrigin
	@RequestMapping(value = "/productList")
	public String getProducts(@RequestParam(name = "offset", defaultValue = "0") int offset) {
		if(httpSession.getAttribute("user")==null) return "session expired";
		List<Product> list = productDao.getAllProducts(offset);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			return e.getMessage();
		}

	}

	// method to update product info
	@CrossOrigin
	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	public String updateProducts(@RequestBody Product product) {
		if(httpSession.getAttribute("user")==null) return "session expired";
		productDao.addProduct(product);
		return "success";
	}

	// method to add product
	@CrossOrigin
	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public String insertProducts(@RequestBody Product product) {
		if(httpSession.getAttribute("user")==null) return "session expired";
		productDao.addProduct(product);
		return "success";
	}

}

package com.praveen.ecommerce;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praveen.ecommerce.dao.CommonDao;
import com.praveen.ecommerce.dao.OrderDao;
import com.praveen.ecommerce.dao.PaymentDao;
import com.praveen.ecommerce.dao.ProductDao;
import com.praveen.ecommerce.models.Order;
import com.praveen.ecommerce.utils.SendMail;

@RestController
public class WebController {

	@Autowired
	CommonDao commonDao;

	@Autowired
	ProductDao productDao;

	@Autowired
	SendMail sendMail;

	@Autowired
	PaymentDao paymentDao;

	@Autowired
	OrderDao orderDao;

	@CrossOrigin
	@RequestMapping("/product/{productId}")
	public String getProductDetail(@PathVariable("productId") int productId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(productDao.getProduct(productId));
	}

	@CrossOrigin
	@RequestMapping("product/homepage/{siteId}")
	public String getHomePageProducts(@PathVariable("siteId") int siteId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(productDao.getHomePageProducts(siteId));
	}

	@CrossOrigin
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public ResponseEntity<String> persistPerson(@RequestBody Order order) throws Exception {
		if (true) {
			System.out.println(order.toString());
			Order resOrder = orderDao.placeOrder(order);
			ObjectMapper mapper = new ObjectMapper();
			String resJson = mapper.writeValueAsString(resOrder);
			return new ResponseEntity<String>(resJson, HttpStatus.CREATED);
		}
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
	}

	@CrossOrigin
	@RequestMapping("order/{dispOrderId}")
	public String getorderDetails(@PathVariable("dispOrderId") String dispOrderId) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(orderDao.getOrder(dispOrderId));
	}

	@CrossOrigin
	@RequestMapping("/payment/{status}")
	public String updatePayment(@RequestParam Map<String, String> params, @PathVariable("status") String status)
			throws Exception {
		System.out.println(params.toString());
		return paymentDao.processPayment(params);
	}

	@CrossOrigin
	@RequestMapping("/mail")
	public String updatePaymentTest(@RequestParam Map<String, String> params) {
		String body = "Name: " + params.get("name") + "<br> Email: " + params.get("email") + "<br> phone: "
				+ params.get("phone") + "<br> message: " + params.get("message");
		sendMail.sendEmail("psoni10001@gmail.com", params.get("email"), "contact Form", body);
		return "";
	}

	@CrossOrigin
	@RequestMapping("/order/status")
	public String getOrderStatus(@RequestParam Map<String, String> params) {
		return orderDao.getOrderStatus(params.get("dispOrderId"));
	}

}

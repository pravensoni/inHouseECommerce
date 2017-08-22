package com.praveen.ecommerce.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mysql.cj.api.jdbc.Statement;
import com.praveen.ecommerce.models.Order;
import com.praveen.ecommerce.models.Order.PaymentType;
import com.praveen.ecommerce.models.PayInfo;
import com.praveen.ecommerce.utils.JavaIntegrationKit;
import com.praveen.ecommerce.utils.SendMail;

@Component
public class PaymentDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	JavaIntegrationKit kit;

	@Autowired
	SendMail sendMail;

	@Autowired
	OrderDao orderDao;

	public void setPayInfo(Order order) {
		PayInfo payInfo = new PayInfo("", order.getDispOrderId(), order.getSubTotal() + "",
				order.getCustomerInfo().getFname(), order.getCustomerInfo().getEmail(),
				order.getCustomerInfo().getPhone(), "testProductInfo");
		Map<String, String> params = new HashMap<>();
		params.put("key", payInfo.getKey());
		params.put("txnid", payInfo.getTxnid());
		params.put("amount", payInfo.getAmount());
		params.put("firstname", payInfo.getFirstname());
		params.put("email", payInfo.getEmail());
		params.put("phone", payInfo.getPhone());
		params.put("productinfo", payInfo.getProductinfo());
		params.put("surl", payInfo.getSurl());
		params.put("furl", payInfo.getFurl());
		params.put("service_provider", payInfo.getServiceProvider());
		String hash = null;
		try {
			hash = kit.hashCalMethod(params).get("hash");
			payInfo.setHash(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		order.setPayInfo(payInfo);
	}

	public Long insertPaymentDetail(PaymentType pt, String pst) {
		KeyHolder holder = new GeneratedKeyHolder();
		Long paymentId = null;
		int i = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "INSERT INTO payments (payment_type, payment_status)  VALUES (?,?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, pt.name());
				ps.setString(2, pst);
				return ps;
			}
		}, holder);
		if (i == 1) {
			paymentId = holder.getKey().longValue();
		}
		return paymentId;
	}

	public boolean updatePaymentStatus(String ps, String dispOrderId) {
		int i = jdbcTemplate.update("UPDATE order_details o left join payments py on o.payment_id = py.id "
				+ "SET payment_status = ? WHERE o.disp_order_id=?", new Object[] { ps, dispOrderId });
		return i == 1;
	}

	public String processPayment(Map<String, String> paymentParams) {
		String calHash = null;
		String receivedHash = paymentParams.get("hash");

		try {
			calHash = kit.hashCalMethod(paymentParams).get("hash");
			if (receivedHash.equals(calHash)) {
				updatePaymentStatus(paymentParams.get("status"), paymentParams.get("txnid"));
			} else {
				// return failure page
			}

		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ("success".equals(paymentParams.get("status"))) {
			// return success page
			sendMail.orderConfirmation(paymentParams.get("email"), orderDao.getOrder(paymentParams.get("txnid")));
			return getPaymntSuccessPage(paymentParams.get("txnid"));
		} else {
			// return failure page
			return getPaymntFailurePage(paymentParams.get("txnid"), paymentParams.get("error_Message"));
		}

	}

	public String getPaymntSuccessPage(String dispOrderId) {
		// TODO: success url in properties
		String response = "";
		String url = "http://test.fidgetcube.in/#/order/";
		response = "<html><script language=javascript>function redirect(){  window.location = \"" + url + dispOrderId
				+ "\";}</script><body onload=\"redirect()\"></body></html>";
		return response;
	}

	public String getPaymntFailurePage(String dispOrderId, String errorMsg) {
		// TODO: failure url in properties
		String response = "";
		String url = "http://test.fidgetcube.in/#/order/";
		response = "<html><body><h4>Your Payment Failed.</h4><p>" + errorMsg + "</p></body></html>";
		return response;
	}
}

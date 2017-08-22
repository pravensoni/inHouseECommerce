package com.praveen.ecommerce.utils;

import com.praveen.ecommerce.models.Order;
import com.praveen.ecommerce.models.Product;
import com.praveen.ecommerce.models.Order.PaymentType;

public class htmlStore {
	public static String getOrderConfirmationHtml(Order order) {
		String html = "<html lang=\"en\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;font-size:10px;-webkit-tap-highlight-color:rgba(0, 0, 0, 0);\"><head></head><body style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin:0;font-family:&quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif;font-size:14px;line-height:1.42857143;color:#333;background-color:#fff;\">"
				+ "   <div class=\"container\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;padding-right:15px;padding-left:15px;margin-right:auto;margin-left:auto;width:1100px;\">"
				+ "  <div class=\"col-md-offset-2 col-md-8 col-xs-12\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:66.66666667%;\">"
				+ "  <h2 style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-family:inherit;font-weight:500;line-height:1.1;color:inherit;margin-top:20px;margin-bottom:10px;font-size:30px;\">Thank You, Your order has been placed!</h2>"
				+ "  <h4 style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-family:inherit;font-weight:500;line-height:1.1;color:inherit;margin-top:10px;margin-bottom:10px;font-size:18px;\">Order Number: <b style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-weight:bold;\">"
				+ order.getDispOrderId() + "</b>" + "</h4>" + "<br style=\"-webkit-box-sizing: border-box;"
				+ "     -moz-box-sizing: border-box;"
				+ "          box-sizing: border-box;\"><div class=\"panel-group\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin-bottom:20px;\">"
				+ "    <div class=\"panel panel-default\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin-bottom:0;background-color:#fff;border:1px solid transparent;border-radius:4px;-webkit-box-shadow:0 1px 1px rgba(0, 0, 0, .05);box-shadow:0 1px 1px rgba(0, 0, 0, .05);border-color:#ddd;\">"
				+ "      <div class=\"panel-heading\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;padding:10px 15px;border-bottom:0;border-top-left-radius:3px;border-top-right-radius:3px;color:#333;background-color:#f5f5f5;border-color:#ddd;\"><b style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-weight:bold;\">Order Summary</b></div>"
				+ "      <div class=\"panel-body\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;padding:15px;overflow: hidden;\">"
				+ " " + "        <!--Order Summary Start-->"
				+ "        <div class=\"col-md-12\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:100%;overflow: hidden;\">"
				+ "          <div style=\"font-size:18px;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin-right:-15px;margin-left:-15px;border-bottom:1px solid #ccc;padding:10px 0px 5px 5px;overflow: hidden;\" class=\"row cart-row\">"
				+ "            <div class=\"col-md-6\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:50%;\"></div>"
				+ "            <div class=\"col-md-2\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:16.66666667%;\">Price</div>"
				+ "            <div class=\"col-md-2\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:16.66666667%;\">Quantity</div>"
				+ "            <div class=\"col-md-2\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:16.66666667%;\">Total</div>"
				+ "          </div>" + " ";

		for (Product product : order.getProducts()) {
			html += "          <div product=\"\" of=\"\" order=\"\" class=\"row cart-row\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin-right:-15px;margin-left:-15px;border-bottom:1px solid #ccc;padding:10px 0px 5px 5px;/* float: left; */overflow: hidden;\">"
					+ "            <div class=\"col-md-2\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:16.66666667%;\"><img width=\"100%\" src=\""+product.getImageLinks().get(0)+"\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;border:0;vertical-align:middle;\"></div>"
					+ "            <div class=\"col-md-4\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:33.33333333%;\">"+product.getTitle()
					+ "              <br style=\"-webkit-box-sizing: border-box;" + "     -moz-box-sizing: border-box;"
					+ "          box-sizing: border-box;\">"+"" + "            </div>"
					+ "            <div class=\"col-md-2 col-xs-4\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:16.66666667%;\">"+product.getPrice()+"</div>"
					+ "            <div class=\"col-md-2 col-xs-4\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:16.66666667%;\">"+product.getQty()+"</div>"
					+ "            <div class=\"col-md-2 col-xs-4\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:16.66666667%;\">"+(product.getPrice()*product.getQty())+"</div>"
					+ "          </div>";
		}
		html += "          <div class=\"row \" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin-right:-15px;margin-left:-15px;\">"
				+ "            <div class=\"col-md-offset-9 col-md-3\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;position:relative;min-height:1px;padding-right:15px;padding-left:15px;float:left;width:25%;margin-left:75%;\">"
				+ "              <h4 style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-family:inherit;font-weight:500;line-height:1.1;color:inherit;margin-top:10px;margin-bottom:10px;font-size:18px;\">Subtotal:<b style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-weight:bold;\"> ₹ "+order.getSubTotal()+"</b>"
				+ "</h4>" + "            </div>" + "          </div>" + " " + " " + "        </div>"
				+ "        <!--Order Summary End-->" + " " + "      </div>" + "    </div>"
				+ "    <div class=\"panel panel-default\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin-bottom:0;background-color:#fff;border:1px solid transparent;border-radius:4px;-webkit-box-shadow:0 1px 1px rgba(0, 0, 0, .05);box-shadow:0 1px 1px rgba(0, 0, 0, .05);border-color:#ddd;margin-top:5px;\">"
				+ "      <div class=\"panel-heading\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;padding:10px 15px;border-bottom:0;border-top-left-radius:3px;border-top-right-radius:3px;color:#333;background-color:#f5f5f5;border-color:#ddd;\"><b style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;font-weight:bold;\">Customer Information</b></div>"
				+ "      <div class=\"panel-body\" style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;padding:15px;\">"
				+ "        <p style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin:0 0 10px;\">Shipping Address</p>"
				+ "        <p style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin:0 0 10px;\">"+order.getCustomerInfo().getFname()+" "+order.getCustomerInfo().getLname()+"<br style=\"-webkit-box-sizing: border-box;"
				+ "     -moz-box-sizing: border-box;" + "          box-sizing: border-box;\">"
				+ "        "+order.getCustomerInfo().getAddress()+"<br style=\"-webkit-box-sizing: border-box;"
				+ "     -moz-box-sizing: border-box;" + "          box-sizing: border-box;\">"
				+ "        "+order.getCustomerInfo().getCity()+" "+order.getCustomerInfo().getState()+" "+order.getCustomerInfo().getPincode()+"<br style=\"-webkit-box-sizing: border-box;"
				+ "     -moz-box-sizing: border-box;" + "          box-sizing: border-box;\">"
				+ "       Phone: "+order.getCustomerInfo().getPhone()+"</p>"
				+ "       <p style=\"-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;margin:0 0 10px;\">Payment Method:";
			if(order.getPaymentType()==PaymentType.COD){
				html+= "         <span style=\"-webkit-box-sizing: border-box;" + "     -moz-box-sizing: border-box;"
				+ "          box-sizing: border-box;\">Cash On Delivery</span>";
			}else if(order.getPaymentType()==PaymentType.ONLINE){
				html+= "         <span style=\"-webkit-box-sizing: border-box;" + "     -moz-box-sizing: border-box;"
				+ "          box-sizing: border-box;\">Online</span></p>" + "      </div>" + "    </div>" + "</div>"
				+ "</div>" + " " + "</div>" + "" + "</body></html>";
			}

		return html;
	}

}

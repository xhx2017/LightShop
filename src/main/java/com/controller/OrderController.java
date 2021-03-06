package com.controller;

import com.entity.Orders;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fuxi on 2017/5/13.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    //获取所有订单
    @RequestMapping("/getMyOrders")
    public ModelAndView getOrdersById(int state, HttpSession session){
        Integer userId= (Integer) session.getAttribute("userId");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("myOrders",orderService.getOrdersById(userId,state));
        modelAndView.addObject("state",state);
        modelAndView.setViewName("myOrders");
        return modelAndView;
    }

    @RequestMapping("/showMyOrder")
    public ModelAndView showOrderById(String orderId,HttpSession session){
        Integer userId= (Integer) session.getAttribute("userId");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("myOrder",orderService.getOrderById(orderId,userId));
        modelAndView.setViewName("showOrder");
        return modelAndView;
    }

    //添加订单
    @RequestMapping("/addOrder")
    public String addOrder(@RequestParam(value = "checkbox") Integer[] checkbox, Orders orders,String s_province,String s_city,String s_county,HttpSession session){
        Integer userId= (Integer) session.getAttribute("userId");
        System.out.println("checkbox"+checkbox.length);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String ordersId = df.format(new Date()) + String.valueOf(userId);
        orders.setOrdersId(ordersId);
        orders.setOrderStateId(1);
        orders.setUserId(userId);
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        orders.setDate(ts);
        orders.setAddress(s_province+s_city+s_county+orders.getAddress());
        orderService.addOrder(orders);
        int n = checkbox.length;
        //if (checkbox[0] == 0) n++;
        for (int i = 0 ; i < n ; i++){
            if (checkbox[i] > 0){
                addOrderLight(ordersId,checkbox[i]);
            }
        }
        return "newOrder";
    }

    //订单中的商品
    public void addOrderLight(String orderId,int shopCartId){
        orderService.addOrderLight(orderId,shopCartId);
    }
}

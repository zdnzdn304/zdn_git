package org.csu.shopping.controller;
import org.csu.shopping.domain.Account;
import org.csu.shopping.domain.Order;
import org.csu.shopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.csu.shopping.domain.CartProduct;
import org.csu.shopping.service.CartService;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/Cart")
    public String cart(HttpServletRequest request,Model model)
    {
        HttpSession session = request.getSession();
        Account account=new Account();
        account=(Account)session.getAttribute("account");
        List<CartProduct> productList= cartService.getProductListByUserId(account.getUserId());
        int totalPrice=0;
        for(CartProduct cartProduct:productList)
            totalPrice=totalPrice+cartProduct.cartProductPrice*cartProduct.productQuantity;
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("productList",productList);
        return "cart/shopcar";
    }
    @GetMapping("/addCartProduct")
    public String addCartProduct(@RequestParam("productId") String productId,Model model,HttpServletRequest request){
        HttpSession session=request.getSession();
        Account account=new Account();
        account=(Account) session.getAttribute("account");
        cartService.insertCartProduct(account.getUserId(),productId);
        List<CartProduct> productList= cartService.getProductListByUserId(account.getUserId());
        int totalPrice=0;
        for(CartProduct cartProduct:productList)
            totalPrice=totalPrice+cartProduct.cartProductPrice*cartProduct.productQuantity;
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("productList",productList);
        return "cart/shopcar";
    }
    @GetMapping("/removeCartProduct")
    public String removeCartProduct(@RequestParam("productId") String productId,Model model,HttpServletRequest request){
        HttpSession session=request.getSession();
        Account account=new Account();
        account=(Account)session.getAttribute("account");
        cartService.removeCartProduct(account.getUserId(),productId);
        List<CartProduct> productList= cartService.getProductListByUserId(account.getUserId());
        int totalPrice=0;
        for(CartProduct cartProduct:productList)
            totalPrice=totalPrice+cartProduct.cartProductPrice*cartProduct.productQuantity;
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("productList",productList);
        return "cart/shopcar";
    }
    @GetMapping("/removeCart")
    public String removeCart(HttpServletRequest request,Model model)
    {
        HttpSession session=request.getSession();
        Account account=new Account();
        account=(Account)session.getAttribute("account");
        cartService.removeUserCart(account.getUserId());
        List<CartProduct> productList= cartService.getProductListByUserId(account.getUserId());
        int totalPrice=0;
        for(CartProduct cartProduct:productList)
            totalPrice=totalPrice+cartProduct.cartProductPrice*cartProduct.productQuantity;
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("productList",productList);
        return "cart/shopcar";
    }
    @GetMapping("/order")
    public String order(HttpServletRequest request,Model model)
    {
        HttpSession session=request.getSession();
        Account account=new Account();
        account=(Account)session.getAttribute("account");
        List<CartProduct> productList= cartService.getProductListByUserId(account.getUserId());
        int totalPrice=0;
        for(CartProduct cartProduct:productList)
            totalPrice=totalPrice+cartProduct.cartProductPrice*cartProduct.productQuantity;
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("productList",productList);
        session.setAttribute("productList",productList);
        model.addAttribute("account",account);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String date=df.format(new Date());
        String orderId=date+account.getUserId();
        model.addAttribute("date",date);
        model.addAttribute("orderId",orderId);
        session.setAttribute("orderId",orderId);
        session.setAttribute("orderTime",date);
        session.setAttribute("priceTotal",totalPrice);

        return "order/order";
    }

    @GetMapping("/pay")
    public String payOrder(HttpServletRequest request,Model model)
    {

        HttpSession session=request.getSession();
        Account account=new Account();
        account=(Account)session.getAttribute("account");
        Order order=new Order();
        order.setUserId(account.getUserId());
        order.setOrderId((String)session.getAttribute("orderId"));
        order.setOrderTime((String)session.getAttribute("orderTime"));
        order.setOrderAddress(account.getAddress());
        order.setPriceTotal((int)session.getAttribute("priceTotal"));
        order.setExpressId("暂无");
        order.setOrderStatus("0");
        orderService.insertIntoOrderPlus(order);
        List<CartProduct> productList= cartService.getProductListByUserId(account.getUserId());
        for(CartProduct cartProduct:productList)
        {
            order.setProductId(cartProduct.productId);
            order.setProductQuantity(cartProduct.getProductQuantity());
            orderService.insertIntoOrders(order);
        }
        cartService.removeUserCart(account.getUserId());
        return "index";
    }
}

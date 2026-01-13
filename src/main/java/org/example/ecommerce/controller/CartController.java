package org.example.ecommerce.controller;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.model.dto.CartDto;
import org.example.ecommerce.service.CartService;
import org.example.ecommerce.service.ProductService;
import org.example.ecommerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    public CartController(CartService cartService, ProductService productService, UserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String viewCart(@RequestParam(required = false) Long userId, Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("selectedUserId", userId);
        if (userId != null) {
            model.addAttribute("cart", cartService.getCart(userId));
            model.addAttribute("selectedUser", userService.findById(userId));
        } else {
            // Provide an empty cart to avoid null pointers in template evaluation
            model.addAttribute("cart", new org.example.ecommerce.model.Cart());
        }
        return "cart/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("products", productService.findAll());
		CartDto cartDto = new CartDto();
		model.addAttribute("cart", cartDto);
        return "cart/add";
    }

    @PostMapping("/add")
	public String addToCart(@ModelAttribute(name = "cart") CartDto cartDto, RedirectAttributes redirectAttributes) {
		Product product = productService.findById(cartDto.getProductId());
		if (product != null && cartDto.getUserId() != null) {
			cartService.addToCart(cartDto.getUserId(), product, cartDto.getQuantity());
            redirectAttributes.addFlashAttribute("message", "Product added to cart!");
			return "redirect:/cart?userId=" + cartDto.getUserId();
        }
        return "redirect:/cart/add";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, @RequestParam(required = false) Long userId,
            RedirectAttributes redirectAttributes) {
        if (userId != null) {
            cartService.removeFromCart(userId, productId);
            redirectAttributes.addFlashAttribute("message", "Product removed from cart!");
            return "redirect:/cart?userId=" + userId;
        }
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(@RequestParam(required = false) Long userId, RedirectAttributes redirectAttributes) {
        if (userId != null) {
            cartService.clearCart(userId);
            redirectAttributes.addFlashAttribute("message", "Cart cleared!");
            return "redirect:/cart?userId=" + userId;
        }
        return "redirect:/cart";
    }
}

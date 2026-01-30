package org.example.ecommerce.controller;

import org.example.ecommerce.model.Cart;
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
            model.addAttribute("cart", new Cart());
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
        Product product = productService.findEntityById(cartDto.getProductId());
        cartService.addToCart(cartDto.getUserId(), product, cartDto.getQuantity());
        redirectAttributes.addFlashAttribute("message", "Product added to cart!");
        return "redirect:/cart?userId=" + cartDto.getUserId();
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long userId, @RequestParam Long productId,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(userId, productId, quantity);
        redirectAttributes.addFlashAttribute("message", "Cart updated!");
        return "redirect:/cart?userId=" + userId;
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(userId, productId);
        redirectAttributes.addFlashAttribute("message", "Product removed from cart!");
        return "redirect:/cart?userId=" + userId;
    }

    @GetMapping("/clear")
    public String clearCart(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        cartService.clearCart(userId);
        redirectAttributes.addFlashAttribute("message", "Cart cleared!");
        return "redirect:/cart?userId=" + userId;
    }
}

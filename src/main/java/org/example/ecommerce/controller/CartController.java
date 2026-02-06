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
    public String viewCart(java.security.Principal principal, Model model) {
        if (principal != null) {
            org.example.ecommerce.model.User user = userService.findByUsername(principal.getName());
            model.addAttribute("cart", cartService.getCart(user.getId()));
            model.addAttribute("selectedUser", user);
        } else {
            model.addAttribute("cart", new Cart());
        }
        return "cart/view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("products", productService.findAll());
        CartDto cartDto = new CartDto();
        model.addAttribute("cart", cartDto);
        return "cart/add";
    }

    @PostMapping("/add")
    public String addToCart(@ModelAttribute(name = "cart") CartDto cartDto, java.security.Principal principal,
            RedirectAttributes redirectAttributes) {
        org.example.ecommerce.model.User user = userService.findByUsername(principal.getName());
        Product product = productService.findEntityById(cartDto.getProductId());
        cartService.addToCart(user.getId(), product, cartDto.getQuantity());
        redirectAttributes.addFlashAttribute("message", "Product added to cart!");
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId, @RequestParam Integer quantity,
            java.security.Principal principal, RedirectAttributes redirectAttributes) {
        org.example.ecommerce.model.User user = userService.findByUsername(principal.getName());
        cartService.updateQuantity(user.getId(), productId, quantity);
        redirectAttributes.addFlashAttribute("message", "Cart updated!");
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, java.security.Principal principal,
            RedirectAttributes redirectAttributes) {
        org.example.ecommerce.model.User user = userService.findByUsername(principal.getName());
        cartService.removeFromCart(user.getId(), productId);
        redirectAttributes.addFlashAttribute("message", "Product removed from cart!");
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(java.security.Principal principal, RedirectAttributes redirectAttributes) {
        org.example.ecommerce.model.User user = userService.findByUsername(principal.getName());
        cartService.clearCart(user.getId());
        redirectAttributes.addFlashAttribute("message", "Cart cleared!");
        return "redirect:/cart";
    }
}

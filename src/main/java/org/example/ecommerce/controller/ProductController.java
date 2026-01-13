package org.example.ecommerce.controller;

import java.util.List;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public String listProducts(Model model) {
		List<Product> productList = productService.findAll();
		model.addAttribute("products", productList);

		return "products/list";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		return "products/form";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Product product = productService.findById(id);
		model.addAttribute("product", product);
		return "products/form";
	}

	@PostMapping("/save")
	public String saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
		productService.save(product);
		redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
		return "redirect:/products";
	}

	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		productService.delete(id);
		redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
		return "redirect:/products";
	}
}

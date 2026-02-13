package org.example.ecommerce.controller;

import java.util.List;

import org.example.ecommerce.model.Product;
import org.example.ecommerce.model.dto.ProductDTO;
import org.example.ecommerce.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public String listProducts(Model model) {
		List<ProductDTO> productList = productService.findAll();
		model.addAttribute("products", productList);

		return "products/list";
	}

	@GetMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public String showAddForm(Model model) {
		ProductDTO product = new ProductDTO();
		model.addAttribute("product", product);
		return "products/form";
	}

	@GetMapping("/edit/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String showEditForm(@PathVariable Long id, Model model) {
		ProductDTO product = productService.findById(id);
		model.addAttribute("product", product);
		return "products/form";
	}

	@PostMapping("/save")
	@PreAuthorize("hasRole('ADMIN')")
	public String saveProduct(@ModelAttribute("product") @Valid ProductDTO product, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "products/form";
		}
		productService.save(product);
		redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
		return "redirect:/products";
	}

	@GetMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		productService.delete(id);
		redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
		return "redirect:/products";
	}
}

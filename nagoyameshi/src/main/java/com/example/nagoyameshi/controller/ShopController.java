package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Shop;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.ShopRepository;

@Controller
@RequestMapping("/shops")
public class ShopController {
	private final ShopRepository shopRepository;
	private final CategoryRepository categoryRepository;
	
	public ShopController(ShopRepository shopRepository, CategoryRepository categoryRepository) {
		this.shopRepository = shopRepository;
		this.categoryRepository = categoryRepository;
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			            @RequestParam(name = "categoryName", required = false) String categoryName,
			            @RequestParam(name = "price", required = false) Integer price,
			            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			            Model model)
	{
		Page<Shop> shopPage;
		
		if (keyword != null && !keyword.isEmpty()) {
			shopPage = shopRepository.findByNameLikeOrAddressLikeOrCategory_CategoryNameLike("%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", pageable);
			
		} else if(categoryName != null && !categoryName.isEmpty()) {
			shopPage = shopRepository.findByCategory_CategoryNameLike("%" + categoryName + "%", pageable);
			
		} else if (price != null) {
			shopPage = shopRepository.findByHighestPriceLessThanEqual(price, pageable);
			
		} else {
			shopPage = shopRepository.findAll(pageable);
		}
		
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		
		model.addAttribute("shopPage", shopPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("price", price);
		
		return "shops/index";
	}
	
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
    	Shop shop = shopRepository.getReferenceById(id);
    	
    	model.addAttribute("shop", shop);
    	model.addAttribute("reservationInputForm", new ReservationInputForm());
    	
    	return "shops/show";
    }

}

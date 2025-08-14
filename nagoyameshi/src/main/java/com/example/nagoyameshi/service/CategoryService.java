package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	// コントローラから移動
	public CategoryEditForm toCategoryEditForm(Category category) {
		return new CategoryEditForm (
		category.getId(), category.getCategoryName());
	}
	
	@Transactional
	public void create(CategoryRegisterForm categoryRegisterForm) {
		Category category = new Category();
		
		category.setCategoryName(categoryRegisterForm.getCategoryName());
		
		categoryRepository.save(category);
	}
	
	@Transactional
	public void update(CategoryEditForm categoryEditForm) {
		Category category = categoryRepository.getReferenceById(categoryEditForm.getId());
		
		category.setCategoryName(categoryEditForm.getCategoryName());
		
		categoryRepository.save(category);
	}
}

package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
	public Page<Shop> findByNameLike(String keyword, Pageable pageable);
	
    public Page<Shop> findByNameLikeOrAddressLikeOrCategory_CategoryNameLike(String nameKeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable);
	public Page<Shop> findByCategory_CategoryNameLike(String categoryName, Pageable pageable);
	public Page<Shop> findByHighestPriceLessThanEqual(Integer price, Pageable pageable);
	
	/*
	public Page<Shop> findByNameLikeOrAddressLikeOrCategory_CategoryNameLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable);
	public Page<Shop> findByNameLikeOrAddressLikeOrCategory_CategoryNameLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable);
	public Page<Shop> findByNameLikeOrAddressLikeOrCategory_CategoryNameLikeOrderByPriceDesc(String nameKeyword, String addressKeyword, String categoryNameKeyword, Pageable pageable);
	
	public Page<Shop> findByCategory_CategoryNameLikeOrderByCreatedAtDesc(String categoryName, Pageable pageable);
	public Page<Shop> findByCategory_CategoryNameLikeOrderByPriceAsc(String categoryName, Pageable pageable);
	public Page<Shop> findByCategory_CategoryNameLikeOrderByPriceDesc(String categoryName, Pageable pageable);
	
	public Page<Shop> findByHighestPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	public Page<Shop> findByHighestPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);
	public Page<Shop> findByHighestPriceLessThanEqualOrderByPriceDesc(Integer price, Pageable pageable);
	
	public Page<Shop> findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<Shop> findAllByOrderByPriceAsc(Pageable pageable);
	public Page<Shop> findAllByOrderByPriceDesc(Pageable pageable);
	*/
	
	public List<Shop> findTop10ByOrderByCreatedAtDesc();

}

package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Shop;
import com.example.nagoyameshi.entity.ShopHoliday;
import com.example.nagoyameshi.form.ShopEditForm;
import com.example.nagoyameshi.form.ShopRegisterForm;
import com.example.nagoyameshi.repository.ShopHolidayRepository;
import com.example.nagoyameshi.repository.ShopRepository;

@Service
public class ShopService {
	private final ShopRepository shopRepository;
	private final ShopHolidayRepository shopHolidayRepository;

	public ShopService(ShopRepository shopRepository, ShopHolidayRepository shopHolidayRepository) {
		this.shopRepository = shopRepository;
		this.shopHolidayRepository = shopHolidayRepository;
	}
	
	// コントローラから移動
	public ShopEditForm toShopEditForm(Shop shop) {
		List<ShopHoliday> holidays = shopHolidayRepository.findByShop(shop);
		List<String> holidayDays = new ArrayList<>();
		for (ShopHoliday holiday : holidays) {
			holidayDays.add(holiday.getDayOfWeek());
		}

		return new ShopEditForm(shop.getId(), shop.getCategory().getId(), shop.getName(), null,
				shop.getDescription(), shop.getLowestPrice(), shop.getHighestPrice(),
				shop.getOpeningTime().toString(), shop.getClosingTime().toString(), holidayDays,
				shop.getPostalCode(), shop.getAddress(), shop.getPhoneNumber(), shop.getSeatingCapacity());
	}

	@Transactional
	public void create(Category category, ShopRegisterForm shopRegisterForm) {
		Shop shop = new Shop();
		MultipartFile imageFile = shopRegisterForm.getImageFile();

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			shop.setImageName(hashedImageName);
		}

		shop.setCategory(category);
		shop.setName(shopRegisterForm.getName());
		shop.setDescription(shopRegisterForm.getDescription());
		shop.setLowestPrice(shopRegisterForm.getLowestPrice());
		shop.setHighestPrice(shopRegisterForm.getHighestPrice());
		shop.setOpeningTime(LocalTime.parse(shopRegisterForm.getOpeningTime()));
		shop.setClosingTime(LocalTime.parse(shopRegisterForm.getClosingTime()));
		shop.setPostalCode(shopRegisterForm.getPostalCode());
		shop.setAddress(shopRegisterForm.getAddress());
		shop.setPhoneNumber(shopRegisterForm.getPhoneNumber());
		shop.setSeatingCapacity(shopRegisterForm.getSeatingCapacity());

		shopRepository.save(shop);

		if (shopRegisterForm.getShopHolidays() != null) {
			for (String day : shopRegisterForm.getShopHolidays()) {
				ShopHoliday shopHoliday = new ShopHoliday();
				shopHoliday.setShop(shop);
				shopHoliday.setDayOfWeek(day);
				shopHolidayRepository.save(shopHoliday);
			}
		}
	}

	@Transactional
	public void update(Category category, ShopEditForm shopEditForm) {
		Shop shop = shopRepository.getReferenceById(shopEditForm.getId());
		MultipartFile imageFile = shopEditForm.getImageFile();

		if (!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			shop.setImageName(hashedImageName);
		}

		shop.setCategory(category);
		shop.setName(shopEditForm.getName());
		shop.setDescription(shopEditForm.getDescription());
		shop.setLowestPrice(shopEditForm.getLowestPrice());
		shop.setHighestPrice(shopEditForm.getHighestPrice());
		shop.setOpeningTime(LocalTime.parse(shopEditForm.getOpeningTime()));
		shop.setClosingTime(LocalTime.parse(shopEditForm.getClosingTime()));
		shop.setPostalCode(shopEditForm.getPostalCode());
		shop.setAddress(shopEditForm.getAddress());
		shop.setPhoneNumber(shopEditForm.getPhoneNumber());
		shop.setSeatingCapacity(shopEditForm.getSeatingCapacity());

		shopRepository.save(shop);

		// 定休日をすべて削除して初期化
		shopHolidayRepository.deleteByShop(shop);

		// 定休日が存在すれば、新しい内容を登録
		if (shopEditForm.getShopHolidays() != null) {
			for (String day : shopEditForm.getShopHolidays()) {
				ShopHoliday shopHoliday = new ShopHoliday();
				shopHoliday.setShop(shop); // 外部キーとしてShopをセット
				shopHoliday.setDayOfWeek(day); // 曜日をセット
				shopHolidayRepository.save(shopHoliday);
			}
		}
	}

	// UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for (int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}

		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;
	}

	// 画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

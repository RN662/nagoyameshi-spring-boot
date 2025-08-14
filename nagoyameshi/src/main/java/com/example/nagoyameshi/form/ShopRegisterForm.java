package com.example.nagoyameshi.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShopRegisterForm {
	@NotNull(message = "カテゴリを選択してください。")
	private Integer categoryId;
	
	@NotBlank(message = "店舗名を入力してください。")
	private String name;
	
	// 画像
	private MultipartFile imageFile;
	
	@NotBlank(message = "説明を入力してください。")
	private String description;
	
	@NotNull(message = "最低価格を入力してください。")
	@Min(value = 1, message = "最低価格は1円以上に設定してください。")
	private Integer lowestPrice;
	
	@NotNull(message = "最高価格を入力してください。")
	@Min(value = 1, message = "最高価格は1円以上に設定してください。")
	private Integer highestPrice;
	
	@NotBlank(message = "開店時間を入力してください。")
	private String openingTime;
	
	@NotBlank(message = "閉店時間を入力してください。")
	private String closingTime;
	
	// 定休日（バリデーションチェックなし）
	private List<String> shopHolidays;
	
	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;
	
	@NotBlank(message = "住所を入力してください。")
	private String address;
	
	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;
	
	@NotNull(message = "座席数を入力してください。")
	@Min(value = 1, message = "座席数は1席以上に設定してください。")
	private Integer seatingCapacity;

}

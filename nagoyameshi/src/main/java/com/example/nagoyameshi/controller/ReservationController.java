package com.example.nagoyameshi.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
	private final ReservationRepository reservationRepository;
	
	public ReservationController(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}
	
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		User user = userDetailsImpl.getUser();
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
		
		LocalDate today = LocalDate.now();
		
		model.addAttribute("reservationPage", reservationPage);
		model.addAttribute("today", today);
		
		return "reservations/index";
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		reservationRepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "予約をキャンセルしました。");
		
		return "redirect:/reservations";
	}
}

package com.sluv.admin.router.celeb;

import com.sluv.admin.celeb.dto.CelebCategoryResponse;
import com.sluv.admin.celeb.dto.NewCelebPostDto;
import com.sluv.admin.celeb.service.CelebCategoryService;
import com.sluv.admin.celeb.service.NewCelebService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class NewCelebRoute {

    private final NewCelebService newCelebService;
    private final CelebCategoryService celebCategoryService;

    @GetMapping("/new-celebs")
    public String testMap(Model model) {
        List<NewCelebPostDto> newCelebs = newCelebService.findAllNewCelebPostDto();
        model.addAttribute("newCelebs", newCelebs);
        return "celeb/newCelebs";
    }

    @GetMapping("/new-celeb-register")
    public String celebPostPage(Model model, @RequestParam("newCelebId") Long newCelebId) {
        NewCelebPostDto newCeleb = newCelebService.findByNewCelebId(newCelebId);
        List<CelebCategoryResponse> celebCategories = celebCategoryService.findAllAvailableCategory();
        celebCategories.add(0, CelebCategoryResponse.of(5L, "방송인"));
        celebCategories.add(1, CelebCategoryResponse.of(6L, "인플루언서"));
        model.addAttribute("celebCategories", celebCategories);
        model.addAttribute("newCelebId", newCeleb.getNewCelebId());
        model.addAttribute("name", newCeleb.getName());
        model.addAttribute("createdAt", newCeleb.getCreatedAt());
        return "celeb/newCelebRegister";
    }
}

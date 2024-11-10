package com.sluv.admin.router.celeb;

import com.sluv.admin.celeb.dto.CelebCategoryResponse;
import com.sluv.admin.celeb.dto.CelebPageResponse;
import com.sluv.admin.celeb.service.CelebCategoryService;
import com.sluv.admin.celeb.service.CelebService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CelebRouter {

    private final CelebService celebService;
    private final CelebCategoryService celebCategoryService;

    @GetMapping("/celebs")
    public String getCelebListPage(Model model, @Nullable @RequestParam("keyword") String keyword,
                                   @Nullable @RequestParam("page") Integer page) {
        CelebPageResponse celebPage = celebService.findAllCelebResponsePage(keyword, page);
        model.addAttribute("celebs", celebPage.getContent());
        model.addAttribute("pageNumber", celebPage.getPageNumber());
        model.addAttribute("totalPageSize", celebPage.getTotalPageSize());
        model.addAttribute("keyword", keyword);
        return "celeb/celebs";
    }

    @GetMapping("/celeb-register")
    public String getCelebRegisterPage(Model model) {
        List<CelebCategoryResponse> celebCategories = celebCategoryService.findAllAvailableCategory();
        celebCategories.add(0, CelebCategoryResponse.of(5L, "방송인"));
        celebCategories.add(1, CelebCategoryResponse.of(6L, "인플루언서"));
        model.addAttribute("celebCategories", celebCategories);
        model.addAttribute("celebCategorySize", celebCategories.size());
        return "celeb/celebRegister";
    }

}

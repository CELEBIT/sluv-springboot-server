package com.sluv.admin.router.brand;

import com.sluv.admin.brand.dto.BrandPageResponse;
import com.sluv.admin.brand.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BrandRouter {

    private final BrandService brandService;

    @GetMapping("/brands")
    public String getBrandListPage(Model model, @Nullable @RequestParam("keyword") String keyword,
                                   @Nullable @RequestParam("page") Integer page) {
        System.out.println("size: " + page);
        BrandPageResponse newBrands = brandService.findAllNewBrandRegisterDto(keyword, page);
        model.addAttribute("brands", newBrands.getContent());
        model.addAttribute("pageNumber", newBrands.getPageNumber());
        model.addAttribute("totalPageSize", newBrands.getTotalPageSize());
        model.addAttribute("keyword", keyword);
        return "brands";
    }

    @GetMapping("/brand-register")
    public String getBrandRegisterPage(Model model) {
        return "brandRegister";
    }

}

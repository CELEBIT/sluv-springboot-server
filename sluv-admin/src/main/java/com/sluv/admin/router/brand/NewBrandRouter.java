package com.sluv.admin.router.brand;

import com.sluv.admin.brand.dto.NewBrandResponse;
import com.sluv.admin.brand.service.NewBrandService;
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
public class NewBrandRouter {

    private final NewBrandService newBrandService;

    @GetMapping("/new-brands")
    public String getNewBrandListPage(Model model) {
        List<NewBrandResponse> newBrands = newBrandService.findAllNewBrandRegisterDto();
        model.addAttribute("newBrands", newBrands);
        return "newBrands";
    }

    @GetMapping("/new-brand-register")
    public String getNewBrandRegisterPage(Model model, @RequestParam("newBrandId") Long newBrandId) {
        NewBrandResponse newBrand = newBrandService.findByNewBrandId(newBrandId);
        model.addAttribute("newBrandId", newBrand.getNewBrandId());
        model.addAttribute("name", newBrand.getName());
        model.addAttribute("createdAt", newBrand.getCreatedAt());
        return "newBrandRegister";
    }
}

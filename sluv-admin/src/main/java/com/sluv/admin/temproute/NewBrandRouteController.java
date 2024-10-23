package com.sluv.admin.temproute;

import com.sluv.admin.brand.dto.NewBrandRegisterDto;
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
public class NewBrandRouteController {

    private final NewBrandService newBrandService;

    @GetMapping("/new-brands")
    public String testMap(Model model) {
        List<NewBrandRegisterDto> newBrands = newBrandService.findAllNewBrandRegisterDto();
        model.addAttribute("newBrands", newBrands);
        return "newBrands";
    }

    @GetMapping("/brand-register")
    public String celebPostPage(Model model, @RequestParam("brandId") Long brandId) {
        NewBrandRegisterDto newBrand = newBrandService.findByNewBrandId(brandId);
        model.addAttribute("brandId", newBrand.getBrandId());
        model.addAttribute("name", newBrand.getName());
        model.addAttribute("createdAt", newBrand.getCreatedAt());
        return "brandRegister";
    }
}

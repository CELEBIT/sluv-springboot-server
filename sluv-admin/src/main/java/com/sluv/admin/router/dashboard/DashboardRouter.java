package com.sluv.admin.router.dashboard;

import com.sluv.admin.visit.dto.VisitHistoryCountResDto;
import com.sluv.admin.visit.service.VisitHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DashboardRouter {

    private final VisitHistoryService visitHistoryService;

    @GetMapping("/dashboard/visit")
    public String getVisitCounts(Model model) {
        VisitHistoryCountResDto visitHistoryCount = visitHistoryService.getVisitHistoryCount();

        model.addAttribute("day", visitHistoryCount.getDates());
        model.addAttribute("dailyVisitCount", visitHistoryCount.getCountGraph());
        return "visit/visit";
    }

}

package com.nouh.pomotrackerapi.web;

import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dao.repositories.UserRepository;
import com.nouh.pomotrackerapi.dto.StatisticsDTO;
import com.nouh.pomotrackerapi.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final UserRepository userRepository;


    @GetMapping("/today")
    public ResponseEntity<StatisticsDTO> getTodayStats(@AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(statisticsService.getTodayStats(user));
    }

    @GetMapping("/monthly")
    public ResponseEntity<StatisticsDTO> getMonthlyStats(@AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(statisticsService.getMonthlyStats(user));
    }
}
/*
package com.nouh.pomotrackerapi.web;

import com.nouh.pomotrackerapi.dao.entities.User;
import com.nouh.pomotrackerapi.dto.StatisticsDTO;
import com.nouh.pomotrackerapi.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/today")
    public ResponseEntity<StatisticsDTO> getTodayStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(statisticsService.getTodayStats(user));
    }
    @GetMapping("/monthly")
    public ResponseEntity<StatisticsDTO> getMonthlyStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(statisticsService.getMonthlyStats(user));
    }

}*/

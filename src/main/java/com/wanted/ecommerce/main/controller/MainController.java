package com.wanted.ecommerce.main.controller;

import com.wanted.ecommerce.common.constants.MessageConstants;
import com.wanted.ecommerce.common.response.ApiResponse;
import com.wanted.ecommerce.main.dto.response.MainPageResponse;
import com.wanted.ecommerce.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping
    public ResponseEntity<ApiResponse<MainPageResponse>> getMainPageContents() {
        MainPageResponse response = mainService.getMainPageContents();
        return ResponseEntity.ok(
            ApiResponse.success(response, MessageConstants.READ_ALL_MAIN_CONTENTS.getMessage()));
    }
}

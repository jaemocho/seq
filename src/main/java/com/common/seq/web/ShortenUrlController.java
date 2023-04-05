package com.common.seq.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.seq.data.dto.RespShortenUrlDto;
import com.common.seq.service.ShortenUrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shorten-url")
public class ShortenUrlController {
    
    private final ShortenUrlService shortenUrlService;

    @Operation(summary = "단축 url 생성", description = "단축 url 생성", tags = { "ShortenUrl Controller" })
    @ApiResponses({
            @ApiResponse(
                responseCode = "200"
                , description = "OK"
                , content = @Content(schema = @Schema(implementation = RespShortenUrlDto.class))
                )
    })
    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<RespShortenUrlDto> getShortenUrl(String originUrl) {
        
        return new ResponseEntity<RespShortenUrlDto>(shortenUrlService.genShortenUrl(originUrl), HttpStatus.OK);
	}
}

package com.common.seq.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.seq.common.Constants.ExceptionClass;
import com.common.seq.common.exception.SequenceException;
import com.common.seq.data.dto.ReqGUID;
import com.common.seq.data.dto.RespGUID;
import com.common.seq.data.entity.Sequence;
import com.common.seq.id.IDHandler;
import com.common.seq.id.impl.GUID;
import com.common.seq.service.SequenceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seq")
public class SeqController {
	
    @Qualifier("GUIDHandler")
    private final IDHandler ih;

    private final SequenceService sequenceService;
    @Qualifier("SequenceServiceImpl")

    @Operation(summary = "GUID 발급 요청", description = "GUID가 신규로 생성 또는 업데이트 됩니다.", tags = { "Seq Controller" })
    @ApiResponses({
            @ApiResponse(
                responseCode = "201"
                , description = "OK"
                , content = @Content(schema = @Schema(implementation = RespGUID.class))
                )
    })
    @PostMapping(path = "/guid", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createGUID(@RequestBody ReqGUID requestGUID){
        
        RespGUID resp = RespGUID.builder().build();
        
        if ("".equals(requestGUID.getGuid()) || requestGUID.getGuid() == null)  {
            // request에 guid가 없는 경우 최초 생성 하여 0번째 idx에 from server를 입력하여 반환
            resp.setGuid(ih.update(ih.genNewID(), requestGUID.getFromServer()).getId());
        } else {
            // request에 guid가 있는 경우 update 하여 반환 
            resp.setGuid(ih.update( new GUID().setId(requestGUID.getGuid()), requestGUID.getFromServer()).getId());
        }
        return new ResponseEntity<>(resp, HttpStatus.CREATED); 

	}

    @Operation(summary = "현재 sequence 조회", description = "현재 sequence 조회", tags = { "Seq Controller" })
    @ApiResponses({
            @ApiResponse(
                responseCode = "200"
                , description = "OK"
                , content = @Content(schema = @Schema(implementation = Sequence.class))
                )
    })
    @GetMapping(path = "/sequence", produces = "application/json")
    public ResponseEntity<?> getSEQ(){
        return new ResponseEntity<>(sequenceService.get(), HttpStatus.OK); 
	}

    @Operation(summary = "sequence 발급 요청", description = "sequnce 발급", tags = { "Seq Controller" })
    @ApiResponses({
            @ApiResponse(
                responseCode = "200"
                , description = "OK"
                , content = @Content(schema = @Schema(implementation = Sequence.class))
                )
    })
    @PostMapping(path = "/sequence", produces = "application/json")
    public ResponseEntity<?> increseSEQ(){
        return new ResponseEntity<>(sequenceService.update(), HttpStatus.OK); 

	}

    @PostMapping(path = "/execption")
    public void exceptionTest() throws SequenceException{
        
        log.error("Exception {}, {}");  
        throw new SequenceException(ExceptionClass.SEQUENCE, HttpStatus.FORBIDDEN, "접근이 금지되었습니다.");

	}
}

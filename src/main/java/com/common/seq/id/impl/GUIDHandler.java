package com.common.seq.id.impl;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.common.seq.id.ID;
import com.common.seq.id.IDHandler;


@Component
public class GUIDHandler implements IDHandler {

    // 끝 4자리 랜덤 값 seed (16*16*16*16)
    final public int minRandomValue = 4096;
    final public int maxRandomValue = 65535-minRandomValue;
    
    // guid 에 입력할 수 있는 서버 id 최대 개수 
    final public int serverListSize = 4;

    // server id 자리 수 
    final public int serverIdSize = 3;

    // guid에 현재 몇 번째 index까지 서버가 입력되었는지 표기하는 위치(13번재 자리)
    final public int nextIndexPos = serverListSize * serverIdSize;
    
    /**
     * Generate New GUID
     * [서버 고유 ID 3자리 총 4개] + [다음 서버 입력될 IDX] + [현재 시간(최초 생성 시간)] + [16진수 Random value 0000~ffff]
     * ex) 001 002 003 004 0 1679124038022 a1f1
     * @return GUID
     * @throws NULL
     */
    public ID genNewID() {
        Random r = new Random();
        String ranStr = Integer.toHexString(r.nextInt(maxRandomValue)+minRandomValue);

        ID newId = new GUID();
        long time = System.currentTimeMillis();
        newId.setId("xxxxxxxxxxxx"+ "0" + String.valueOf(time) + ranStr);
        newId.setLastModifyDate(new Date(time));
        
        return newId;
    }

    /**
     * GUID validate check 
     * 
     * guid null, empty check
     * guid length check(only 30)
     * next idx check(server id input pos)
     * 
     * @return vaildate(true/false)
     * @throws NULL
     */
    public Boolean validate(ID id) {

        if ( id == null ) return false;
        
        String guid = id.getId();

        // null empty check
        if ( guid == null || "".equals(guid) ) {
            return false;
        }

        // length check
        if ( guid.length() != 30 ) {
            return false;
        }

        // 0 ~ server list size check 
        char nextIdx = guid.charAt(nextIndexPos);
        if ( !(nextIdx >= '0' 
            &&  nextIdx <= (char)(serverListSize)+'0')) {
                return false;
        }

        return true;
    }

    /**
     * GUID update
     * 
     * update server id
     * update nextIndexPos (max 값에 도달하면 0으로)
     * 
     * @return GUID
     * @throws NULL
     */
    public ID update(ID id, Object... param) {

        // update param 정합성 검증 
        String serverId;
        
        if (param.length == 0 ) {
            serverId = "NEW";
        } else {
            if ( param[0] instanceof String) {
                serverId = (String)param[0];
            } else {
                serverId = "NEW";
            }
        }

        if ( serverId == null || "".equals(serverId.trim())) {
            serverId = "NEW";
        }

        serverId = serverId.trim();

        serverId = serverId.replaceAll(" ", "x");

        if ( serverId.length() < 3 ) {
            int currentLen = serverId.length();
            for (int i = 0; i < 3 - currentLen; i++) {
                serverId += "x";
            }
        }

        // id 정합성 검증
        if (!validate(id)) {
            id = genNewID();
        }

        // id update
        String guid = id.getId();

        int insertIdx = Integer.parseInt(guid.charAt(nextIndexPos)+"");
        
        int startidx = insertIdx * serverIdSize;
        int endIdx = startidx + serverIdSize - 1;

        char[] updatedGUID =  guid.toCharArray();

        // 입력 가능한 서버 수를 초과 하였을 경우 
        if ( insertIdx == serverListSize ) {
            int idx = serverIdSize * (serverListSize-1);
            // 제일 처음 서버를 삭제 
            for ( int i = 0; i < idx; i++) {
                updatedGUID[i] = updatedGUID[i+serverIdSize];
            }

            // 마지막에 신규 서버 입력 
            for (int i = idx, j = 0; i < idx + serverIdSize && j < serverIdSize; i++, j++) {
                updatedGUID[i] = serverId.charAt(j);
            }
        } else {

            // 지정된 idx에 서버 입력 
            for (int i = startidx, j = 0; i <= endIdx && j < serverIdSize; i++, j++) {
                updatedGUID[i] = serverId.charAt(j);
            }

            updatedGUID[nextIndexPos] = (char)(insertIdx + 1 +'0');
        }

        id.setLastModifyDate(new Date(System.currentTimeMillis()));
        id.setId(String.valueOf(updatedGUID));
        
        return id;
    }

    
}

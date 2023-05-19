package com.common.seq.service.id.impl;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.common.seq.service.id.ID;
import com.common.seq.service.id.IDHandler;


@Component
public class GUIDHandler implements IDHandler {

    // 끝 4자리 랜덤 값 seed (16*16*16*16)
    private final int MIN_RANDOM_VALUE = 4096;
    private final int MAX_RANDOM_VALUE = 65535-MIN_RANDOM_VALUE;
    
    // guid 에 입력할 수 있는 서버 id 최대 개수 
    private final int SERVER_LIST_SIZE = 4;

    // server id 자리 수 
    private final int SERVER_ID_SIZE = 3;

    // guid에 현재 몇 번째 index까지 서버가 입력되었는지 표기하는 위치(13번재 자리)
    private final int NEXT_IDX_POS = SERVER_LIST_SIZE * SERVER_ID_SIZE;
    
    /**
     * Generate New GUID
     * [서버 고유 ID 3자리 총 4개] + [다음 서버 입력될 IDX] + [현재 시간(최초 생성 시간)] + [16진수 Random value 0000~ffff]
     * ex) 001 002 003 004 0 1679124038022 a1f1
     * @return GUID
     * @throws NULL
     */
    public ID genNewID() {
        String randomString = createRandomString();
        return createNewId(randomString);
    }

    private String createRandomString() {
        Random r = new Random();
        return Integer.toHexString(r.nextInt(MAX_RANDOM_VALUE)+MIN_RANDOM_VALUE);
    }

    private ID createNewId(String randomString) {
        ID newId = new GUID();
        long time = System.currentTimeMillis();
        newId.setId("xxxxxxxxxxxx"+ "0" + String.valueOf(time) + randomString);
        newId.setLastModifyDate(new Date(time));
        return newId;
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
        
        if (!validate(id)) {
            id = genNewID();
        }
        String serverId = createServerId(param);
        String updatedGUID = updateGUID(id, serverId);
        id.setLastModifyDate(new Date(System.currentTimeMillis()));
        id.setId(updatedGUID);
        
        return id;
    }

    private String createServerId(Object... param) {
        String serverId;
        if (vaildateParam(param)) {
            serverId = (String)param[0];
        } else{
            return "NEW";
        }

        serverId = serverId.trim();
        serverId = serverId.replaceAll(" ", "x");

        if ( serverId.length() < 3 ) {
            serverId = addXToPost(serverId);
        }

        return serverId;
    }

    private String updateGUID(ID id, String serverId) {
        
        String guid = id.getId();
        int insertIdx = Integer.parseInt(guid.charAt(NEXT_IDX_POS)+"");        
        int startidx = insertIdx * SERVER_ID_SIZE;
        
        char[] updatedGUID =  guid.toCharArray();

        // 입력 가능한 서버 수를 초과 하였을 경우 
        if ( insertIdx == SERVER_LIST_SIZE ) {
            int lastIdxPos = SERVER_ID_SIZE * (SERVER_LIST_SIZE-1);
            deleteFirstServerInGUID(updatedGUID, lastIdxPos);
            addNewServerInGUID(updatedGUID, serverId, lastIdxPos);
        } else {
            addNewServerInGUID(updatedGUID, serverId, startidx);
            updateNextIdxPos(updatedGUID, insertIdx);
        }

        return String.valueOf(updatedGUID);
    }

    private boolean vaildateParam(Object... param) {
        if (param.length == 0 ) {
            return false;
        } else {
            if ( isString(param[0])) {
                if(isNull((String)param[0])) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private String addXToPost(String serverId) {
        int currentLen = serverId.length();
        for (int i = 0; i < 3 - currentLen; i++) {
            serverId += "x";
        }
        return serverId;
    }

    private void deleteFirstServerInGUID(char[] guid, int lastIdxPos) {
        for ( int i = 0; i < lastIdxPos; i++) {
            guid[i] = guid[i+SERVER_ID_SIZE];
        }
    }

    private void addNewServerInGUID(char[] guid, String serverId, int startIdx) {
        for (int i = startIdx, j = 0; i < startIdx + SERVER_ID_SIZE && j < SERVER_ID_SIZE; i++, j++) {
            guid[i] = serverId.charAt(j);
        }
    }

    private void updateNextIdxPos(char[] guid, int currentIdx) {
        guid[NEXT_IDX_POS] = (char)(currentIdx + 1 +'0');
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
        if ( isNull(guid) ) {
            return false;
        }
        if ( guid.length() != 30 ) {
            return false;
        }
        if (!vaildateNextIdxPos(guid.charAt(NEXT_IDX_POS)) ) {
            return false;
        }
        return true;
    }

    private boolean vaildateNextIdxPos(char nextIdx) {
        if ( !(nextIdx >= '0' 
            &&  nextIdx <= (char)(SERVER_LIST_SIZE)+'0')) {
                return false;
        }
        return true;
    }



    private boolean isString(Object object) {
        if( object instanceof String ) return true;
        return false;
    }

    private boolean isNull(String string) {
        return string == null || "".equals(string.trim());
    }



    
}

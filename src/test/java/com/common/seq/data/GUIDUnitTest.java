package com.common.seq.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.common.seq.id.ID;
import com.common.seq.id.IDHandler;
import com.common.seq.id.impl.GUID;
import com.common.seq.id.impl.GUIDHandler;

public class GUIDUnitTest {
    
    @Test
    public void genNewID_test() {
        IDHandler ih = new GUIDHandler();
        ID id = ih.genNewID();

        String newGUID = id.getId();

        assertNotNull(newGUID);
        assertEquals(30, newGUID.length());
        assertEquals('0', newGUID.charAt(12));
    }

    // @Test
    // public void genNewID_test2() {
    //     IDHandler ih = new GUIDHandler();
        
    //     String newGUID;
    //     // 1 억번 
    //     for (int i = 0; i < 100000000; i++ ) {
    //         newGUID = ih.genNewID().getId();
    //         assertNotNull(newGUID);
    //         assertEquals(30, newGUID.length());
    //         assertEquals('0', newGUID.charAt(12));
    //     }
    // }

    @Test 
    public void validate_test() {
        IDHandler ih = new GUIDHandler();
        
        Boolean result;

        result = ih.validate(null);
        assertEquals(false, result);

        // 29자리 테스트 
        result = ih.validate(new GUID().setId("00100200300401679124038022a1f"));
        assertEquals(false, result);

        // 31자리 테스트 
        result = ih.validate(new GUID().setId("00100200300401679124038022a1f11"));
        assertEquals(false, result);

        // 12번째 자리 경계 값 테스트 (숫자 0)
        result = ih.validate(new GUID().setId("00100200300401679124038022a1f1"));
        assertEquals(true, result);

        // 12번째 자리 경계 값 테스트 (숫자 4)
        result = ih.validate(new GUID().setId("00100200300441679124038022a1f1"));
        assertEquals(true, result);

        // 12번째 자리 0~4 사이 아닌 값 테스트 (숫자 5)
        result = ih.validate(new GUID().setId("00100200300451679124038022a1f1"));
        assertEquals(false, result);

        // 12번째 자리 0~4 사이 아닌 값 테스트 (문자 a)
        result = ih.validate(new GUID().setId("001002003004a1679124038022a1f1"));
        assertEquals(false, result);

        // 12번째 자리 0~4 사이 아닌 값 테스트 (특수 문자 *)
        result = ih.validate(new GUID().setId("001002003004*1679124038022a1f1"));
        assertEquals(false, result);

        // 정상 case 테스트
        result = ih.validate(new GUID().setId("00100200300401679124038022a1f1"));
        assertEquals(true, result);
    }

    @Test
    public void update_test() {
        IDHandler ih = new GUIDHandler();

        ID updatedGUID = new GUID();
        
        //  idx 0 에 kkk 서버 insert test 
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"), "kkk");
        assertEquals("kkk00200300411679124038022a1f1", updatedGUID.getId());
        //  idx 1 에 kkk 서버 insert test 
        updatedGUID = ih.update(new GUID().setId("00100200300411679124038022a1f1"), "kkk");
        assertEquals("001kkk00300421679124038022a1f1", updatedGUID.getId());
        //  idx 3 에 kkk 서버 insert test 
        updatedGUID = ih.update(new GUID().setId("00100200300421679124038022a1f1"), "kkk");
        assertEquals("001002kkk00431679124038022a1f1", updatedGUID.getId());
        //  idx 3 에 kkk 서버 insert test 
        updatedGUID = ih.update(new GUID().setId("00100200300431679124038022a1f1"), "kkk");
        assertEquals("001002003kkk41679124038022a1f1", updatedGUID.getId());
        //  idx 4가 된 경우 idx1의 값을 삭제 하고 한칸 씩 앞으로 잘 당겨지는 지 테스트 
        updatedGUID = ih.update(new GUID().setId("00100200300441679124038022a1f1"), "kkk");
        assertEquals("002003004kkk41679124038022a1f1", updatedGUID.getId());
        //  idx 4가 된 경우 idx1의 값을 삭제 하고 한칸 씩 앞으로 잘 당겨지는 지 테스트 두번재 
        updatedGUID = ih.update(new GUID().setId("002003004kkk41679124038022a1f1"), "jjj");
        assertEquals("003004kkkjjj41679124038022a1f1", updatedGUID.getId());


        // server id 3글자 초과 테스트
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"), "kkkkkkk");
        assertEquals("kkk00200300411679124038022a1f1", updatedGUID.getId());

        // server id 2글자 테스트 ( 모자란 1글자는 x로 대체 )
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"), "kk");
        assertEquals("kkx00200300411679124038022a1f1", updatedGUID.getId());

        // server id 2글자 + 공백 테스트 ( 모자란 1글자는 x로 대체 )
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"), "kk   ");
        assertEquals("kkx00200300411679124038022a1f1", updatedGUID.getId());

        // server id 2글자 + 공백 테스트 ( 모자란 1글자는 x로 대체 )
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"), "   kk   ");
        assertEquals("kkx00200300411679124038022a1f1", updatedGUID.getId());

        // server id 2글자 가운데 공백 테스트 ( 공백 1글자는 x로 대체 )
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"), "k k");
        assertEquals("kxk00200300411679124038022a1f1", updatedGUID.getId());

        // server id 1글자 테스트 ( 공백 2글자는 x로 대체 )
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"), "k");
        assertEquals("kxx00200300411679124038022a1f1", updatedGUID.getId());


        // guid validate 오류 테스트, 새로운 id 발급 후 첫번 째 idx에 서버 입력 
        updatedGUID = ih.update(new GUID().setId("00100200300471679124038022a1f1"), "kkk");
        assertEquals("kkkxxxxxxxxx1", updatedGUID.getId().substring(0, 13));

        // server id null test, server id 대신 NEW입력 
        updatedGUID = ih.update(new GUID().setId("00100200300401679124038022a1f1"));
        assertEquals("NEW00200300411679124038022a1f1", updatedGUID.getId());
    }
}

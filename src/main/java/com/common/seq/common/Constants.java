package com.common.seq.common;

public class Constants {

    public enum ExceptionClass {

        SEQUENCE("Sequence"), SHORTENURL("ShortenUrl");

        private String exceptionClass;

        ExceptionClass(String exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public String getExceptionClass() {
            return exceptionClass;
        }

        @Override
        public String toString() {
            return getExceptionClass() + " Exception. ";
        }
        
    }

    public enum JWTException {

        MALFORMED_JWT("잘못된 JWT 서명입니다.")
        ,EXPIRED_JWT("만료된 JWT 입니다.")
        ,UNSUPPORTED_JWT("지원되지 않는 JWT 입니다.")
        ,ILLEGALARGUMENT("JWT가 잘못되었습니다.")
        ,NOTFOUND_JWT("JWT를 찾을 수 없습니다.");

        private String jwtException;

        JWTException(String jwtException) {
            this.jwtException = jwtException;
        }

        public String getJwtException() {
            return this.jwtException;
        }

    }

}
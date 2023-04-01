package com.common.seq.common;

public class Constants {

    public enum ExceptionClass {

        SEQUENCE("Sequence"), SHORTENURL("ShortenUrl");; 

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

}
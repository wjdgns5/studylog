package com.junghoon.studylog.global.error;

public class Exception400 extends RuntimeException{
    // RuntimeException : 개발자의 논리적 오류나
    // RuntimeException : 실행 중 발생할 수 있는 예측 불가능한 상황에 대한 예외 처리를 강제하지 않기 위해 사용 한다.

    // throw new Exception 400
    public Exception400(String message) {
        super(message);
    }
}

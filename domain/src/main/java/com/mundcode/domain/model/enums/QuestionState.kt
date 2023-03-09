package com.mundcode.domain.model.enums

enum class QuestionState {
    READY, // 만나기도 전
    RUNNING, // 현재 푸는 중
    PAUSE, // 이 문제에서 멈춘 경우
    END // 풀었다고 체크한 경우
}

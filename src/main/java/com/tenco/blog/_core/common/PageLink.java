package com.tenco.blog._core.common;

/**
 * 페이지 네비게이션 링크를 위한 데이터 클래스
 * 게시글 목록, 댓글 리스트 등 다양한 곳에서 재사용 가능
 */
public class PageLink {
    private int pageNumber; // 내부 페이지 번호 (0부터, Spring pageable용)
    private int displayNumber; // 사용자에게 표시할 페이지 번호 (1부터)
    private boolean active; // 현재 페이지인지 여부

    public PageLink(int pageNumber, int displayNumber, boolean active) {
        this.pageNumber = pageNumber;
        this.displayNumber = displayNumber;
        this.active = active;
    }
}

package com.tenco.blog.board;

import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.reply.Reply;
import com.tenco.blog.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  Board 관련 비즈니스 로직을 처리하는 Service 계층,
 *
 *  모든 메서드를 읽기 전용 트랜잭션으로 실행(findAll, findById 최적화), 성능최적화(변경 감지 비활성화), 데이터 수정 방지
 *  데이터베이스 락(lock)을 최소화 하여 동시성 성능 개선
 */
@Service // IoC 대상
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    private final BoardJpaRepository boardJpaRepository;

    /**
     * 게시글 저장
     * @param saveDTO
     * @param sessionUser
     * @return Board
     */
    @Transactional // -> Service 측에서 Transactional 처리 무조건 필수!!(Repository가 아님)
    public Board save(BoardRequest.saveDTO saveDTO, User sessionUser) {

        // 1. 로그 기록
        log.info("게시글 저장 서비스 처리 시작 - 제목: {}, 작성자: {}",
                saveDTO.getTitle(), sessionUser.getUsername());

        // 2. DTO를 Entity로 변환 -> (작성자 정보 포함)
        Board board = saveDTO.toEntity(sessionUser);

        // 3. 데이터 베이스에 게시글 저장
        boardJpaRepository.save(board);

        // 4. 저장 완료 로그 기록
        log.info("게시글 저장 완료 - ID: {}, 제목: {}",
                board.getId(), board.getTitle());

        // 5.  저장된 Board를 Controller로 반환
        return board;
    }

    /**
     * 게시글 목록 조회
     * @return List<Board>
     */
    public Page<Board> findAll(Pageable pageable) {
        // 1. 로그 기록
        log.info("게시글 조회 서비스 처리 시작");

        // 2. 데이터 베이스 게시글 조회
        Page<Board> boardPage = boardJpaRepository.findAllJoinUser(pageable);

        // 3. 로그 기록
        log.info("게시글 조회 서비스 처리 완료 - 총 게시글 수 : {}, 총 페이지 수 : {}",
                boardPage.getTotalElements(), boardPage.getTotalPages());

        // 4. 조회된 게시글 반환
        return boardPage;
    }

    public Board findByIdWithReplies(Long id, User sessionUser) {
        // 1. 게시글 조회
        Board board = boardJpaRepository.findByIdJoinUser(id).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        // 2. 게시글의 작성자
        // 3. 게시글 소유권 설정(수정 / 삭제 표시용)
        if (sessionUser != null) {
            boolean isBoardOwner = board.isOwner(sessionUser.getId());
            board.setBoardOwner(isBoardOwner);
        }

        // 댓글 정보 --> Board로 댓글 가져옴
        List<Reply> replies = board.getReplies();

        // 댓글 소유권 설정 (삭제 버튼 표시용)
        if (sessionUser != null) {
            replies.forEach(reply -> {
                boolean isReplyOwner = reply.isOwner(sessionUser.getId());
                reply.setReplyOwner(isReplyOwner);
            });
        }
        return board;
    }

    /**
     * 게시글 상세조회
     * @param id
     * @return Board
     */
    public Board findById(Long id) {
        // 1. 로그 기록
        log.info("게시글 조회 서비스 처리 시작 - ID: {}", id);

        // 2. 데이터베이스 게시글 조회 & 3. 게시글이 없다면 404 에러 처리
        Board board = boardJpaRepository.findByIdJoinUser(id).orElseThrow(() -> {
            log.warn("게시글 조회 실패 - ID: {}", id);
            return new Exception404("게시물을 찾을 수 없습니다.");
        });

        // 4. 조회 성공시 로그 기록
        log.info("게시글 조회 완료 - 제목: {}",
                board.getTitle());

        // 5. 조회된 게시글 반환 처리
        return board;
    }

    /**
     * 게시글 수정하기 (권한 체크 포함)
     * @param id
     * @param updateDTO
     * @param sessionUser
     * @return Board
     */
    @Transactional
    public Board updateById(Long id, BoardRequest.UpdateDTO updateDTO, User sessionUser) {
        // 1. 로그 기록
        log.info("게시글 수정 서비스 처리 시작 - ID : {}", id);

        // 2. 수정하려는 게시글 조회
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            log.warn("게시글 조회 실패 - ID: {}", id);
            return new Exception404("게시물을 찾을 수 없습니다.");
        });

        // 3. 권한 체크 & 4. 권한이 없으면 403 예외 발생
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("게시글 수정 권한이 없습니다.");
        }

        // 5. Board 엔티티에 상태값 변경(Dirty 체킹)
        // FIXME Board Entity에 Update 메서드 만들기
        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());

        // 6. 로그 기록 - 수정 완료
        log.info("게시글 수정 완료 - 게시글 ID: {}, 제목: {}",
                id, board.getTitle());

        // 7. 수정된 게시글 반환
        return board;
    }

    /**
     * 게시글 삭제
     * @param id
     * @param sessionUser
     */
    @Transactional
    public void deleteById(Long id, User sessionUser) {
        // 1. 로그 기록
        log.info("게시글 삭제 서비스 처리 시작 - ID : {}", id);

        // 2. 삭제 하려는 게시글 조회
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            log.warn("게시글 찾기 오류 - ID: {}", id);
            return new Exception404("게시글을 찾을 수 없습니다.");
        });

        // 3. 권한 체크 & 4. 권한이 없으면 403 예외 처리
        if (!board.isOwner(sessionUser.getId())) {
            throw new Exception403("삭제 권한이 없습니다.");
        }

        // 5. 데이터 베이스 삭제 처리
        boardJpaRepository.deleteById(id);

        // 6. 로그 기록
        log.info("게시글 삭제 서비스 처리 완료 - ID : {}", id);
    }

    public void checkBoardOwner(Long boardId, Long userId) {
        Board board = findById(boardId);

        if (!board.isOwner(userId)) {
            throw new Exception403("게시글 수정 권한이 없습니다.");
        }
    }
}

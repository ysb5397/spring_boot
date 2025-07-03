package com.tenco.blog.reply;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.board.Board;
import com.tenco.blog.board.BoardJpaRepository;
import com.tenco.blog.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private static final Logger log = LoggerFactory.getLogger(ReplyService.class);
    private final ReplyJpaRepository replyJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

    // 댓글 조회 기능
    public List<Reply> findByBoard(Long id) {
        return replyJpaRepository.findByBoard(id);
    }

    // 댓글 저장 기능
    @Transactional
    public void save(ReplyRequest.SaveDTO saveDTO, User sessionUser) {
        log.info("댓글 저장 기능 시작 - 게시글 ID: {}, 작성자: {}", saveDTO.getBoardId(), sessionUser.getUsername());

        Board board = boardJpaRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() -> new Exception404("존재하지 않는 게시글입니다."));
        replyJpaRepository.save(saveDTO.toEntity(sessionUser, board));
    }

    @Transactional
    public void deleteById(Long id, User sessionUser) {
        log.info("삭제 서비스 시작 : 댓글 ID - {}", id);

        Reply reply = replyJpaRepository.findById(id).orElseThrow(() ->
            new Exception404("삭제할 댓글이 없습니다.")
        );

        // 현재 로그인한 사용자와 댓글 소유자 확인
        if (!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("삭제 권한이 없습니다.");
        }

        replyJpaRepository.deleteById(id);
    }
}

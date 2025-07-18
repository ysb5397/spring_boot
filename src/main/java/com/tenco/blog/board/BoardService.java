package com.tenco.blog.board;

import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.user.SessionUser;
import com.tenco.blog.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Board 관련 비즈니스 로직을 처리하는 Service 계층
 */
@RequiredArgsConstructor
@Service // IoC 대상
@Transactional(readOnly = true)
public class BoardService {
    private final BoardJpaRepository boardJpaRepository;

    /**
     * 게시글 저장
     */
    @Transactional
    public BoardResponse.SaveDTO save(BoardRequest.SaveDTO saveDTO, SessionUser sessionUser) {
        User user = User.builder()
                .id(sessionUser.getId())
                .username(sessionUser.getUsername())
                .email(sessionUser.getEmail())
                .build();

        Board board = saveDTO.toEntity(user);
        boardJpaRepository.save(board);
        return new BoardResponse.SaveDTO(board);
    }

    /**
     * 게시글 목록 조회
     */
    public List<BoardResponse.MainDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Board> boardPage = boardJpaRepository.findAllJoinUser(pageable);
        List<BoardResponse.MainDTO> mainDTOList = new ArrayList<>();

        for (Board board : boardPage.getContent()) {
            mainDTOList.add(new BoardResponse.MainDTO(board));
        }

        return mainDTOList;
    }

    // 게시글 상세 조회
    // Request -> 컨트롤러((JWT)토큰정보 <- User)
    public BoardResponse.DetailDTO detail(Long id, SessionUser sessionUser) {
        Board board = boardJpaRepository.findByIdJoinUser(id).orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        return new BoardResponse.DetailDTO(board, sessionUser);
    }

    /**
     *  게시글 수정 DTO 변환(권한 체크 포함)
     */
    @Transactional
    public BoardResponse.UpdateDTO update(Long id, BoardRequest.UpdateDTO updateDTO,
                                          SessionUser sessionUser) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> new Exception404("해당 게시글이 존재하지 않습니다"));
        if(!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정 가능");
        }

        board.update(updateDTO);
        return new BoardResponse.UpdateDTO(board);
    }

    /**
     * 게시글 삭제 (권한 체크)
     */
    @Transactional
    public void deleteById(Long id, SessionUser sessionUser) {
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> new Exception404("삭제하려는 게시글이 없습니다"));
        if(!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
        boardJpaRepository.deleteById(id);
    }
}

package com.tenco.blog.board;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardRestController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("/api/boards")
    public ResponseEntity<?> save(@RequestBody BoardRequest.SaveDTO saveDTO,
                                                               HttpSession session) {

        log.info("게시글 저장 API 호출 - title : {}", saveDTO.getTitle());
        saveDTO.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        BoardResponse.SaveDTO saveBoard = boardService.save(saveDTO, sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiUtil<>(saveBoard));
    }

    // 모든 게시글 조회
    @GetMapping("/api/boards")
    public ResponseEntity<ApiUtil<List<BoardResponse.MainDTO>>> allBoardInfo(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("모든 게시글 조회 API 호출");
        List<BoardResponse.MainDTO> boardList = boardService.findAll(page, size);
        return ResponseEntity.ok().body(new ApiUtil<>(boardList));
    }

    // 게시글 상세 조회
    @GetMapping("/api/boards/{id}/detail")
    public ResponseEntity<ApiUtil<BoardResponse.DetailDTO>> boardInfo(@PathVariable(name = "id") Long id,
                                                                      HttpSession session) {
        log.info("단건 게시글 조회 API 호출 - ID : {}", id);
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);

        BoardResponse.DetailDTO boardDetail = boardService.detail(id, sessionUser);
        return ResponseEntity.ok().body(new ApiUtil<>(boardDetail));
    }

    // 게시글 수정
    @PutMapping("/api/boards/{id}/update")
    public ResponseEntity<ApiUtil<BoardResponse.UpdateDTO>> updateBoard(@PathVariable(name = "id") Long id,
                                                                        @RequestBody BoardRequest.UpdateDTO updateDTO,
                                                                        HttpSession session) {
        log.info("게시글 수정 API 호출 - ID : {}", id);
        updateDTO.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        BoardResponse.UpdateDTO updateBoard = boardService.update(id, updateDTO, sessionUser);
        return ResponseEntity.ok().body(new ApiUtil<>(updateBoard));
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/{id}/delete")
    public ResponseEntity<ApiUtil<String>> delete(@PathVariable(name = "id") Long id,
                                                  HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.deleteById(id, sessionUser);
        return ResponseEntity.ok().body(new ApiUtil<>("게시글 삭제 완료"));
    }
}

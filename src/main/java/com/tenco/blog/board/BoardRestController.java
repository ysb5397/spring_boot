package com.tenco.blog.board;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog._core.utils.Define;
import com.tenco.blog.user.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardRestController {

    private final BoardService boardService;

    // 게시글 생성
    @PostMapping("/api/boards")
    public ResponseEntity<?> save(@RequestBody @Valid BoardRequest.SaveDTO saveDTO,
                                  Errors errors,
                                  @RequestAttribute(name = Define.SESSION_USER) SessionUser sessionUser) {


        BoardResponse.SaveDTO saveBoard = boardService.save(saveDTO, sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiUtil<>(saveBoard));
    }

    // 모든 게시글 조회
    @GetMapping("/api/boards")
    public ResponseEntity<?> allBoardInfo(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        List<BoardResponse.MainDTO> boardList = boardService.findAll(page, size);
        return ResponseEntity.ok().body(new ApiUtil<>(boardList));
    }

    // 게시글 상세 조회
    @GetMapping("/api/boards/{id}/detail")
    public ResponseEntity<?> boardInfo(@PathVariable(name = "id") Long id,
                                                                      @RequestAttribute(name = Define.SESSION_USER, required = false) SessionUser sessionUser) {

        BoardResponse.DetailDTO boardDetail = boardService.detail(id, sessionUser);
        return ResponseEntity.ok().body(new ApiUtil<>(boardDetail));
    }

    // 게시글 수정
    @PutMapping("/api/boards/{id}/update")
    public ResponseEntity<?> updateBoard(@PathVariable(name = "id") Long id,
                                                                        @RequestBody @Valid BoardRequest.UpdateDTO updateDTO,
                                                                        Errors errors,
                                                                        @RequestAttribute(name = Define.SESSION_USER) SessionUser sessionUser) {
        BoardResponse.UpdateDTO updateBoard = boardService.update(id, updateDTO, sessionUser);
        return ResponseEntity.ok().body(new ApiUtil<>(updateBoard));
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id,
                                                  @RequestAttribute(name = Define.SESSION_USER) SessionUser sessionUser) {

        boardService.deleteById(id, sessionUser);
        return ResponseEntity.ok().body(new ApiUtil<>("게시글 삭제 완료"));
    }
}

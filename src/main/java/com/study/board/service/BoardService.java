package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;  // 객체 생성 (dependency injection) 의존성 주입
    public void write(Board board) {
        boardRepository.save(board);  // 이렇게 생성한 서비스는 다시 컨트롤러에서 사용함
    }
}

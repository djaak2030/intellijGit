package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;  // 객체 생성 (dependency injection) 의존성 주입

    // 글작성 처리
    public void write(Board board, MultipartFile file) throws Exception {

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  //저장경로

        UUID uuid = UUID.randomUUID(); // 파일이름 중복제거 (랜덤생성)

        String filename = uuid + "_" + file.getOriginalFilename(); // 랜덤이름을 파일네임 앞에 붙인 뒤 , 원래 파일이름으로 파일이름 생성

        File saveFile = new File(projectPath, filename); // 파일생성 하기던에 projectPath에 담기고, name이름으로 담긴다.

        file.transferTo(saveFile); //예외처리가 필요.. throws로 이용

        board.setFilename(filename);
        board.setFilepath("/files/" + filename);

        boardRepository.save(board);  // 이렇게 생성한 서비스는 다시 컨트롤러에서 사용함
    }

    // 게시글 리스트 불러오기 처리
    public Page boardlist(Pageable pageable) {
        return boardRepository.findAll(pageable);
        //Board 라는 class가 담긴 list를 찾아 반환한다.
    }

    // 특정 게시글 불러오기  (임의 1개 게시글)
    public Board boardview(Integer id) {
        return boardRepository.findById(id).get();
        // Integer 형의 변수를 통해 불러오기에 위의 인자인 Integer자료형의 id를 준다.
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

    // 검색창 기능 구현
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
    //JPA에서 findBy(컬럼이름)Containing은 컬럼에서 키워드가 포함된 것을 찾는 것이고, findBy(칼럼이름)의 경우 컬럼에서 키워드와 동일한 것을 찾는 것이다.


}

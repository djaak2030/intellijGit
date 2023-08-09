package com.study.board.controller;


import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {
    @Autowired BoardService boardService;

    @GetMapping ("/board/write") //localhost:8080/board/write
    public String boardWriteForm () {
        return "boardwrite";
    }

    @PostMapping ("/board/writedo") // localhost:8080/board/writedo
    @ResponseBody
    public String boardWritePro (Board board , Model model , MultipartFile file) throws Exception {
        boardService.write(board, file);

        model.addAttribute("message", "글작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping("/board/list")
    public String boardlist (Model model ,  @PageableDefault(page = 0 , size = 10 ,
            sort = "id" , direction = Sort.Direction.DESC) Pageable pageable
                        , String searchKeyword) {
                        //데이터를 담아 페이지로 보내기 위해 Model 자료형을 인자로... 검색할때 (searchKeyword가 있을때)안할때 구문해서 if문 사용

        Page<Board> list = null;

        if(searchKeyword != null) {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }
        else {
            list = boardService.boardlist(pageable);
        }
        // searchKeyword가 들어오느냐, 마느냐에 따라 if문을 사용하여 분리해 처리할 수 있도록 설정해주었다.
        // 만약 searchKeyword != null (검색을 하면)  boardService.boardSearchList로 불러와 출력해주고 아닐 경우 기존의 list를 불러온다.

        int nowPage = list.getPageable().getPageNumber() + 1;  // 현재 페이지 가져오기 ( 0에서 시작하니까 0~9 보단 1~10으로 표현)

        int startPage = Math.max(nowPage - 4, 1); // Math.max(a,b) --> a와 b중 가장 큰값반환.

        int endPage = Math.min(nowPage + 5 , list.getTotalPages());  // totalPage보다 크면 안되기때문에 두개중 최소값 반환하는 min 사용

        model.addAttribute("list",list); // boardService에서 생성한 boardlist메소드를 통해서 list가 반환되는대..
                                                     // 해당 list를 "list"라는 이름으로 넘겨주겟다는 것 (html에 나오게끔...)
        model.addAttribute("nowPage" , nowPage);
        model.addAttribute("startPage" , startPage);
        model.addAttribute("endPage" , endPage);

        return "boardlist";
    }


    @GetMapping("board/view")
    public String boardView (Model model , Integer id) { // localhost:8080/board/view?id=1 약간 쿼리스트링이랑 비슷;?
        model.addAttribute("board" , boardService.boardview(id));
        return "boardView";
    }

    @GetMapping("board/delete")
    public String boardDelete (Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list"; //삭제 처리이후, list로 이동 - redirect 해준다.
                                       // 그냥 view를 보여주냐, url요청(redirect)을 새로하느냐 의 차이인 것 같다.
    }

    @GetMapping("board/modify/{id}")                                             //  id는 path variable(주소 변수 , 경로 변수)
    public String boardModify ( @PathVariable("id") Integer id , Model model) {  //  PathVariable => 변수로 경로를 지정해놓는 것
        model.addAttribute("board" , boardService.boardview(id) );
        return "boardModify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id , Board board , Model model , MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardview(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        // 수정한 내용을 board 자료형의 boardTemp변수의 title과 content로 설정해준다.

        boardService.write(boardTemp , file);
        // 수정한 내용을 boardTemp의 내용을 다시 해당 게시물 title과 content에 덮어써주는 코드(수정)

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }


}

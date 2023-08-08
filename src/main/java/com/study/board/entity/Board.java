package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //PK생성시 JPA로 자동생성 하는방법.
                                                        // id 값을 설정하지 않고(null) INSERT Query를 날리면 그때 id의 값을 세팅한다.
    private Integer id;
    private String title;
    private String content;
}

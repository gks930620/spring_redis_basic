package com.prac.prac.entity;

import lombok.Getter;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Member  implements Persistable<String> {
    public Member(){

    }
    public Member(String id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }

    @Id
    private String id;
    private LocalDateTime createdDate;
    private String password;
    private String username;

    @Override
    public boolean isNew() {
        return createdDate==null;
    }
}
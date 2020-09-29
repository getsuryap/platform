package com.context.springsecurity.domain;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 20)
    private String name;

    @NotBlank
    @Column(length = 20)
    private String mssIdn;

    @NotBlank
    @Column(length = 20)
    private String author;

    @NotBlank
    @Column(length = 20)
    private String publisher;

    @Column(length = 20)
    private  LocalDateTime addedAt = LocalDateTime.now();

    public Books(){}

    public Books(String name, String mssIdn, String author, String publisher) {
        this.name = name;
        this.mssIdn = mssIdn;
        this.author = author;
        this.publisher = publisher;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setMssIdn(String mssIdn) {
        this.mssIdn = mssIdn;
    }

    public String getMssIdn() {
        return mssIdn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }
}

package com.example.author.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Author {
    private Long id;

    @NotNull
    private String name;
}

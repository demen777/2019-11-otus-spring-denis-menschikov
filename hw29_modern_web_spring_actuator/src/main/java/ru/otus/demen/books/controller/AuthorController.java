package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.otus.demen.books.controller.dto.AuthorDto;
import ru.otus.demen.books.controller.dto.mapper.AuthorDtoMapper;
import ru.otus.demen.books.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorDtoMapper authorDtoMapper;

    @GetMapping(path = "/api/authors")
    public List<AuthorDto> getAuthorList() {
        return
            authorService.getAll().stream().map(authorDtoMapper::toAuthorDto).collect(Collectors.toList());
    }

    @PostMapping("/api/author")
    public AuthorDto addAuthor(@RequestBody AuthorDto authorDto)
    {
        log.info("addAuthor authorDto={}", authorDto);
        return authorDtoMapper.toAuthorDto(authorService.add(authorDto.getFirstName(), authorDto.getSurname()));
    }
}


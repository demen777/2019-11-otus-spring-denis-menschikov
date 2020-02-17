package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    public Flux<AuthorDto> getAuthorList() {
        return authorService.getAll().map(authorDtoMapper::toAuthorDto);
    }

    @PostMapping("/api/author")
    public Mono<AuthorDto> addAuthor(@RequestBody AuthorDto authorDto)
    {
        log.info("addAuthor authorDto={}", authorDto);
        return authorService.add(authorDto.getFirstName(), authorDto.getSurname()).map(authorDtoMapper::toAuthorDto);
    }
}


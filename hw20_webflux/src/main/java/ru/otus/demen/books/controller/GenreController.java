package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.controller.dto.GenreDto;
import ru.otus.demen.books.controller.dto.mapper.GenreDtoMapper;
import ru.otus.demen.books.service.GenreService;

@SuppressWarnings("SameReturnValue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final GenreDtoMapper genreDtoMapper;

    @GetMapping("/api/genres")
    public Flux<GenreDto> genres() {
        return genreService.getAll().map(genreDtoMapper::toGenreDto);
    }

    @PostMapping("/api/genre")
    public Mono<GenreDto> addGenrePost(@RequestBody GenreDto genreDto)
    {
        log.info("addGenrePost genreDto={}", genreDto);
        return genreService.add(genreDto.getName()).map(genreDtoMapper::toGenreDto);
    }
}


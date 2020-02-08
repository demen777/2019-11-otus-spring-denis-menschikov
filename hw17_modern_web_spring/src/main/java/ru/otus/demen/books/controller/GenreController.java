package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.demen.books.controller.dto.GenreDto;
import ru.otus.demen.books.controller.dto.mapper.GenreDtoMapper;
import ru.otus.demen.books.service.GenreService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final GenreDtoMapper genreDtoMapper;

    @GetMapping("/api/genres")
    public List<GenreDto> genres() {
        return genreService.getAll().stream().map(genreDtoMapper::toGenreDto).collect(Collectors.toList());
    }

    @PostMapping("/api/genre/add")
    public GenreDto addGenrePost(@RequestBody GenreDto genreDto)
    {
        log.info("addGenrePost genreDto={}", genreDto);
        return genreDtoMapper.toGenreDto(genreService.add(genreDto.getName()));
    }
}


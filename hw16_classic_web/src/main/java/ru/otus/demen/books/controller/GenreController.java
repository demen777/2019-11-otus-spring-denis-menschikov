package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.demen.books.controller.dto.mapper.GenreDtoMapper;
import ru.otus.demen.books.service.GenreService;

import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final GenreDtoMapper genreDtoMapper;

    @GetMapping("/genres")
    public ModelAndView authors() {
        ModelAndView modelAndView = new ModelAndView("genres");
        modelAndView.addObject("genres",
            genreService.getAll().stream().map(genreDtoMapper::toGenreDto).collect(Collectors.toList()));
        return modelAndView;
    }
}


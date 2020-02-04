package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
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

    @GetMapping("/genre/add")
    public ModelAndView addGenreGet() {
        return new ModelAndView("add_genre");
    }

    @PostMapping("/genre/add")
    public RedirectView addGenrePost(@RequestParam("name") String name)
    {
        log.info("addGenrePost name={}", name);
        genreService.add(name);
        return new RedirectView("/genres");
    }
}


package lab_8.controller;

import lab_8.entity.Action;
import lab_8.entity.Song;
import lab_8.repository.ActionRepository;
import lab_8.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Controller
public class SongController {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("/list")
    public ModelAndView getAllSongs() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-songs");
        Action action = new Action();
        mav.addObject("songs", songRepository.findAll());
        mav.addObject("actions", songRepository.findAll());
        mav.addObject(action);
        return mav;
    }

    @GetMapping("/actionList")
    public ModelAndView getAllActions() {
        log.info("/actionList-> connection");
        ModelAndView mav = new ModelAndView("list-actions");
        mav.addObject("actions", actionRepository.findAll());
        return mav;
    }

    @PostMapping("/showUpdateForm?{songId}")
    public String updateSong(@ModelAttribute Action action){
        actionRepository.save(action);
        return "redirect:list";
    }

    @PostMapping("/saveSong")
    public String saveSong(@ModelAttribute Song song, @ModelAttribute Action action ) {
        songRepository.save(song);
        actionRepository.save(action);
        return "redirect:/list";
    }

    @GetMapping("/addSongForm")
    public ModelAndView addSongsForm() {
        ModelAndView mav = new ModelAndView("add-song-form");
        Song song = new Song();
        mav.addObject(song);
        Action action = new Action(getTime());
        mav.addObject(action);
        return mav;
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long songId, @ModelAttribute Action action) {
        ModelAndView mav = new ModelAndView("add-song-form-for-update");
        Optional<Song> optionalSong = songRepository.findById(songId);
        Song song = new Song();
        Action action2 = new Action();
        if (optionalSong.isPresent()) {
            song = optionalSong.get();
        }
        mav.addObject("action", action2);
        mav.addObject("song", song);
        return mav;
    }

    @GetMapping("/deleteSong")
    public String deleteSong(@RequestParam Long songId, @ModelAttribute Action action) {
        ModelAndView mav = new ModelAndView("list-songs");
        Action action1 = new Action();
        mav.addObject(action1);
        actionRepository.save(action);
        songRepository.deleteById(songId);
        return "redirect:/list";
    }

    @PostMapping("/deleteSong?songId")
    public String deleteSong(@ModelAttribute Action action){
        actionRepository.save(action);
        return "redirect:list";
    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }


}

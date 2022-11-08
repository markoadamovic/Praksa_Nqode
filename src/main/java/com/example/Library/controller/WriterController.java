package com.example.Library.controller;

import com.example.Library.model.dto.WriterDto;
import com.example.Library.service.WriterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/writer")
public class WriterController {

    private final WriterService writerService;

    public WriterController(WriterService writerService) {
        this.writerService = writerService;
    }

    @PostMapping
    public ResponseEntity<?> createWriter(@RequestBody WriterDto writerDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(writerService.createWriter(writerDto));
    }

    @PutMapping(path = "/{writerId}")
    public ResponseEntity<?> updateWriter(@RequestBody WriterDto writerDto, @PathVariable Long writerId){

        return ResponseEntity.status(HttpStatus.OK).body(writerService.updateWriter(writerDto, writerId));
    }

    @DeleteMapping(value = "/{writerId}")
    public ResponseEntity<?> deleteWriter(@PathVariable Long id){

        writerService.delete(id);

        return ResponseEntity.ok().build();
    }
    @GetMapping(path = "/{writerId}")
    public ResponseEntity<?> getWriter(@PathVariable Long writerId){

        WriterDto writerDto = writerService.getWriter(writerId);
        return ResponseEntity.status(HttpStatus.OK).body(writerDto);

    }
}

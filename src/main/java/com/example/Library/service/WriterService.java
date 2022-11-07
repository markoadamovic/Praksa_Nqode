package com.example.Library.service;

import com.example.Library.model.Writer;
import com.example.Library.model.dto.WriterDto;
import org.springframework.stereotype.Service;

@Service
public interface WriterService {
    WriterDto createWriter(WriterDto writerDto);

    WriterDto updateWriter(WriterDto writerDto, Long writerId);

    void delete(Long id);

   Writer getWriterModel(Long id);

   WriterDto getWriter(Long writerId);
}

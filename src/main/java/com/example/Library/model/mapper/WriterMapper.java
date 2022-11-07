package com.example.Library.model.mapper;

import com.example.Library.model.Writer;
import com.example.Library.model.dto.WriterDto;

public class WriterMapper {

    public static Writer toEntity(WriterDto writerDto){
        Writer writer = new Writer(writerDto.getFirstName(), writerDto.getLastName());

        return writer;
    }


    public static WriterDto toDto(Writer writer){
        WriterDto writerDto = new WriterDto(writer.getId(), writer.getFirstName(), writer.getLastName());

        return  writerDto;
    }
}

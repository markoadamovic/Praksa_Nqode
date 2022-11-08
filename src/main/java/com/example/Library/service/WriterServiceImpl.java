package com.example.Library.service;

import com.example.Library.model.Writer;
import com.example.Library.model.dto.WriterDto;
import com.example.Library.model.mapper.WriterMapper;
import com.example.Library.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WriterServiceImpl implements WriterService{

    @Autowired
    WriterRepository writerRepository;

    @Override
    public WriterDto createWriter(WriterDto writerDto) {

        Writer writer = WriterMapper.toEntity(writerDto);

        return WriterMapper.toDto(writerRepository.save(writer));
    }

    @Override
    public WriterDto updateWriter(WriterDto writerDto, Long writerId) {
        Optional<Writer> writerOptional = writerRepository.findById(writerId);
        if(writerOptional.isPresent()){
            Writer writer = writerOptional.get();
            writer.setFirstName(writerDto.getFirstName());
            writer.setLastName(writerDto.getLastName());

            return WriterMapper.toDto(writerRepository.save(writer));
        }else{
            return null; // toDo Exception handling
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Writer> writerOptional = writerRepository.findById(id);
        if(writerOptional.isPresent()){
            Writer writer = writerOptional.get();
            writerRepository.delete(writer);
        }else{
            // toDo NotFoundException
        }
    }

    @Override
    public Writer getWriterModel(Long id) {
        if(writerRepository.findById(id).isPresent()) {
            Writer writer = writerRepository.findById(id).get();
            return writer;
        }
        else throw new RuntimeException("no writer");
    }

    @Override
    public WriterDto getWriter(Long writerId) {
        Optional<Writer> writerOptional = writerRepository.findById(writerId);
        if(writerOptional.isPresent()){
            Writer writer = writerOptional.get();
            return WriterMapper.toDto(writer);
        }else{
            return null; //toDo EXception
        }
    }

    @Override
    public List<WriterDto> getWriters() {
        List<Writer> writers = writerRepository.findAll();
        List<WriterDto> writerDtos = new ArrayList<>();
        for(Writer writer : writers) {
            WriterDto writerDto = WriterMapper.toDto(writer);
            writerDtos.add(writerDto);
        }
        return writerDtos;
    }
}

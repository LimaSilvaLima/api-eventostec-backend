package com.eventostec.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.UUID;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.domain.repositories.EventRepository;

@Service
public class EventService {

    @Autowired 
    private AmazonS3 s3Client;
    //private final EventRepository repository;

    public Event createEvent(EventRequestDTO data){
        
       String imgUrl = null;
       if (data.image() != null) {
            imgUrl = this.uploadImg(data.image());
       }

       Event newEvent = new Event();
       newEvent.setTitle(data.title());
       newEvent.setDescription(data.description());
       newEvent.setEventUrl(data.eventUrl());
       newEvent.setDate(new Date(data.date()));
       newEvent.setImgUrl(imgUrl);
       newEvent.setRemote(data.remote());

       repository.save(newEvent);

       return newEvent;
      
        
    }
    private String uploadImg (MultipartFile multipartFile) {
        String filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

    try {
        File file = this.convertMultipartToFile(multipartFile);
        s3Client.putObject(bucketName, filename, file);
        file.delete();
        return s3Client.getUrl(bucketName, filename).toString();

    } catch (Exception e) {
        System.out.println("erro ao subir arquivo");
        return null;
    }

    
      
    }

    private File convertMultipartToFile(MultipartFile multipartFile)throws IOException{
        File convFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    @GetMapping
    public List<EventResponseDTO> getUpcomingEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Event> eventsPage = this.repository.findUpcomingEvents(new Date() , pageable);
        return eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                         "",
                         "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList();
    }
}

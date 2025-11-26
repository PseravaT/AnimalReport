package com.example.AminalReport.service;

import com.example.AminalReport.entities.logs.Log;
import com.example.AminalReport.repository.logs.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void saveLog (Log log){logRepository.save(log);}
}

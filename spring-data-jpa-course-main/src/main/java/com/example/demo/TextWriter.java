package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public interface TextWriter {
    String writeText(String s);
}

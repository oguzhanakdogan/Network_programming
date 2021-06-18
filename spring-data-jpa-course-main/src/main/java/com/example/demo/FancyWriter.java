package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class FancyWriter implements TextWriter{
    @Override
    public String writeText(String s) {
        return "Hello world " + s;
    }
}

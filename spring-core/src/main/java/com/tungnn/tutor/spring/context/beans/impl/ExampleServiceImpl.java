package com.tungnn.tutor.spring.context.beans.impl;

import com.tungnn.tutor.spring.context.beans.ExampleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("exampleServiceImpl")
public class ExampleServiceImpl implements ExampleService {}

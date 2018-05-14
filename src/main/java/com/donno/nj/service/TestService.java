package com.donno.nj.service;

import org.springframework.transaction.annotation.Transactional;

//@Transactional
public interface TestService extends Runnable
{
    void run() ;
}
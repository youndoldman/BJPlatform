package com.donno.nj.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constant {

    @Value("${mode}")
    public String mode;

    public Boolean isTestMode() {
        return "test".equals(mode);
    }

    public static final String PAGE_SIZE = "20";



}

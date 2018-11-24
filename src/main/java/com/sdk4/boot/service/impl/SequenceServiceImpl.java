package com.sdk4.boot.service.impl;

import com.sdk4.boot.service.SequenceService;
import org.springframework.stereotype.Service;

/**
 * @author sh
 */
@Service("BootSequenceService")
public class SequenceServiceImpl implements SequenceService {
    @Override
    public Long nextVal(String type) {
        return null;
    }
}
